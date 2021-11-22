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
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
package com.meowool.sweekt.info

import com.meowool.sweekt.SweektNames
import com.meowool.sweekt.SweektNames.InfoSynthetic
import com.meowool.sweekt.SweektNames.Root
import com.meowool.sweekt.info.InfoClassDescriptorResolver.CopyFunctionName
import com.meowool.sweekt.info.InfoClassDescriptorResolver.createComponentFunctionDescriptor
import com.meowool.sweekt.info.InfoClassDescriptorResolver.createComponentName
import com.meowool.sweekt.info.InfoClassDescriptorResolver.createCopyFunctionDescriptor
import com.meowool.sweekt.info.InfoClassDescriptorResolver.getComponentProperties
import com.meowool.sweekt.info.InfoClassDescriptorResolver.getCopyProperties
import com.meowool.sweekt.info.InfoClassDescriptorResolver.getInfo
import com.meowool.sweekt.info.InfoClassDescriptorResolver.getInfoVariables
import com.meowool.sweekt.info.InfoClassDescriptorResolver.getPsi
import com.meowool.sweekt.info.InfoClassDescriptorResolver.isComponentLike
import com.meowool.sweekt.iteration.contains
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.findClassAcrossModuleDependencies
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.KotlinTypeFactory.simpleNotNullType

/**
 * @author 凛 (https://github.com/RinOrz)
 */
class InfoClassSynthetic : SyntheticResolveExtension {
  private val tempComponentFunctionName = Name.identifier("component$")
  private val componentNames = mutableListOf<Name>()

  override fun getSyntheticFunctionNames(thisDescriptor: ClassDescriptor): List<Name> {
    val info = getInfo(thisDescriptor) ?: return super.getSyntheticFunctionNames(thisDescriptor)
    return buildList {
      if (info.generateCopy) add(CopyFunctionName)
      if (info.generateComponentN) addAll(
        thisDescriptor.unsubstitutedMemberScope
          .getVariableNames().indices
          .map { createComponentName(it + 1) }
      )
    }
  }

  override fun generateSyntheticMethods(
    thisDescriptor: ClassDescriptor,
    name: Name,
    bindingContext: BindingContext,
    fromSupertypes: List<SimpleFunctionDescriptor>,
    result: MutableCollection<SimpleFunctionDescriptor>
  ) {
    val info = getInfo(thisDescriptor) ?: return
    val thisClass = getPsi(thisDescriptor)
    val functions = thisClass.body?.functions.orEmpty()
    val variables = getInfoVariables(thisClass, thisDescriptor, info, bindingContext)
    when {
      isComponentLike(name) -> getComponentProperties(variables).forEachIndexed { index, valueDescriptor ->
        val componentName = createComponentName(index + 1)
        // Do not re-synthesize the same function that has been manually declared
        if (componentName == name && functions.none { it.name == name.asString() && it.valueParameters.isEmpty() }) {
          result += createComponentFunctionDescriptor(thisDescriptor, valueDescriptor, componentName)
        }
      }
      name == CopyFunctionName -> {
        val copyFunction = createCopyFunctionDescriptor(thisDescriptor, getCopyProperties(variables))
        // Do not re-synthesize the same function that has been manually declared
        if (functions.none { func ->
          func.name == copyFunction.name.asString() && func.valueParameters.map {
            bindingContext[BindingContext.TYPE, it.typeReference]
          } == copyFunction.valueParameters.map { it.type }
        }
        ) {
          result += copyFunction
        }
      }
    }
  }

  override fun addSyntheticSupertypes(thisDescriptor: ClassDescriptor, supertypes: MutableList<KotlinType>) {
    getInfo(thisDescriptor) ?: return
    val syntheticClass = thisDescriptor.module.findClassAcrossModuleDependencies(
      ClassId(SweektNames.root(), InfoSynthetic, false)
    )
      ?: error("Can't locate class '$Root.$InfoSynthetic', please make sure you have added the dependency of sweekt's runtime.")
    supertypes += simpleNotNullType(Annotations.EMPTY, syntheticClass, emptyList())
  }
}
