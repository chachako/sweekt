/*
 * Copyright (c) 2021. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.

 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
package com.meowool.sweekt.info

import com.meowool.sweekt.SweektNames.Info
import com.meowool.sweekt.SweektNames.InfoInvisible
import com.meowool.sweekt.castOrNull
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.descriptors.TypeParameterDescriptor
import org.jetbrains.kotlin.descriptors.ValueDescriptor
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.descriptors.VariableDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotated
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.descriptors.impl.ValueParameterDescriptorImpl
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.isPrivate
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.DescriptorToSourceUtils.descriptorToDeclaration
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.secondaryConstructors

/**
 * @author 凛 (https://github.com/RinOrz)
 */
object InfoClassDescriptorResolver {
  val CopyFunctionName = Name.identifier("copy")
  private const val ComponentFunctionNamePrefix = "component"

  fun createComponentName(index: Int): Name = Name.identifier(ComponentFunctionNamePrefix + index)

  /** Is it something like 'component1' */
  fun isComponentLike(name: Name): Boolean = isComponentLike(name.asString())

  /** Is it something like 'component1' */
  fun isComponentLike(name: String): Boolean {
    if (!name.startsWith(ComponentFunctionNamePrefix)) return false

    return try {
      name.removePrefix(ComponentFunctionNamePrefix).toInt()
      true
    } catch (e: NumberFormatException) {
      false
    }
  }

  fun createCopyFunctionDescriptor(
    classDescriptor: ClassDescriptor,
    parametersOrProperties: Collection<VariableDescriptor>,
  ): SimpleFunctionDescriptor {
    val functionDescriptor = SimpleFunctionDescriptorImpl.create(
      classDescriptor,
      Annotations.EMPTY,
      CopyFunctionName,
      CallableMemberDescriptor.Kind.DECLARATION,
      classDescriptor.source
    )

    val parameterDescriptors = arrayListOf<ValueParameterDescriptor>()

    parametersOrProperties.forEachIndexed { index, variable ->
      val propertyDescriptor = variable as? PropertyDescriptor
      // If the variable is not a property, it must not have a default value in the 'copy' function
      val hasDefaultValue = propertyDescriptor != null

      parameterDescriptors += ValueParameterDescriptorImpl(
        functionDescriptor, null, index, Annotations.EMPTY, variable.name, variable.type, hasDefaultValue,
        isCrossinline = false,
        isNoinline = false,
        varargElementType = variable.castOrNull<ValueParameterDescriptor>()?.varargElementType,
        source = variable.source
      )
    }

    functionDescriptor.initialize(
      null,
      classDescriptor.thisAsReceiverParameter,
      emptyList<TypeParameterDescriptor>(),
      parameterDescriptors,
      classDescriptor.defaultType,
      Modality.FINAL,
      DescriptorVisibilities.PUBLIC
    )

    return functionDescriptor
  }

  fun createComponentFunctionDescriptor(
    classDescriptor: ClassDescriptor,
    value: ValueDescriptor,
    componentName: Name,
  ) = SimpleFunctionDescriptorImpl.create(
    classDescriptor,
    Annotations.EMPTY,
    componentName,
    CallableMemberDescriptor.Kind.DECLARATION,
    value.source
  ).apply {
    isOperator = true
    initialize(
      null,
      classDescriptor.thisAsReceiverParameter,
      emptyList<TypeParameterDescriptor>(),
      emptyList<ValueParameterDescriptor>(),
      value.type,
      Modality.FINAL,
      DescriptorVisibilities.PUBLIC
    )
  }

  fun getInfo(descriptor: ClassDescriptor) = descriptor.annotations.findAnnotation(Info)?.let(::Info)

  fun getInfoVariables(
    psi: KtClassOrObject,
    descriptor: ClassDescriptor,
    info: Info,
    bindingContext: BindingContext,
  ): List<VariableDescriptor> {
    val constructorEntry = descriptor.unsubstitutedPrimaryConstructor ?: descriptor.secondaryConstructors.singleOrNull()
    val constructorParametersOrProperties = constructorEntry
      ?.takeIf { info.joinPrimaryProperties }?.valueParameters
      ?.mapNotNull {
        when (val property = bindingContext[BindingContext.VALUE_PARAMETER_AS_PROPERTY, it]) {
          null -> it
          else -> when {
            info.joinPrivateProperties.not() && DescriptorVisibilities.isPrivate(property.visibility) -> null
            else -> property
          }
        }
      }.orEmpty()

    val bodyProperties = psi.body?.properties
      ?.takeIf { info.joinBodyProperties }
      ?.mapNotNull {
        when {
          info.joinPrivateProperties.not() && it.isPrivate() -> null
          else -> bindingContext[BindingContext.VARIABLE, it]
        }
      }.orEmpty()

    return constructorParametersOrProperties + bodyProperties
  }

  fun getCopyProperties(allVariables: List<VariableDescriptor>): List<VariableDescriptor> {
    // For example:
    //   class Foo(required: Int, private val isInit: Boolean) {
    //     lateinit var name: String
    //
    // The 'copy' function signature:
    //   fun copy(
    //     required: Int,
    //     isInit: Boolean = this.isInit,
    //     name: String = this.name
    //   ): Foo
    //
    // =================================================================================
    // "Non-property" parameters in the constructor are treated as required parameters
    val requiredParams = mutableListOf<VariableDescriptor>()
    val declaredProperties = mutableListOf<VariableDescriptor>()

    allVariables.forEach {
      when (it) {
        is PropertyDescriptor -> declaredProperties += it
        else -> requiredParams += it
      }
    }

    return requiredParams + declaredProperties.filterByInfoInvisible { generateCopy }
  }

  fun getComponentProperties(allVariables: List<VariableDescriptor>) =
    allVariables.filterIsInstance<PropertyDescriptor>().filterByInfoInvisible { generateComponentN }

  fun getPsi(classDescriptor: ClassDescriptor) = descriptorToDeclaration(classDescriptor)?.castOrNull<KtClassOrObject>()
    ?: error("${classDescriptor.fqNameSafe} cannot convert to 'KtClass'.")

  private fun <E : Annotated> List<E>.filterByInfoInvisible(isVisible: InfoInvisible.() -> Boolean) = filter {
    val infoInvisible = it.annotations.findAnnotation(InfoInvisible)?.let(::InfoInvisible)
    infoInvisible == null || isVisible(infoInvisible)
  }
}
