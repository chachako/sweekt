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
import com.meowool.sweekt.castOrNull
import com.meowool.sweekt.ifNull
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtSecondaryConstructor
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.annotations.argumentValue
import org.jetbrains.kotlin.resolve.checkers.DeclarationChecker
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext

/**
 * @author 凛 (https://github.com/RinOrz)
 */
object InfoClassChecker : DeclarationChecker {
  override fun check(
    declaration: KtDeclaration,
    descriptor: DeclarationDescriptor,
    context: DeclarationCheckerContext
  ) {
    if (descriptor !is ClassDescriptor) return
    if (declaration !is KtClassOrObject) return
    if (!descriptor.annotations.hasAnnotation(Info)) return

    if (declaration.castOrNull<KtClass>()?.isInterface() == true) {
      context.trace.report(
        InfoErrors.UnsupportedSpecialClass.on(declaration.nameIdentifier ?: declaration, "interface")
      )
      return
    }
    declaration.checkSpecialModifier(context.trace, KtTokens.ANNOTATION_KEYWORD) ?: return
    declaration.checkSpecialModifier(context.trace, KtTokens.ENUM_KEYWORD) ?: return
    declaration.checkSpecialModifier(context.trace, KtTokens.DATA_KEYWORD) ?: return

    val generateCopyArg = descriptor.annotations.findAnnotation(Info)!!.argumentValue("generateCopy")
    if (generateCopyArg == null || generateCopyArg.value == true) {
      val primaryConstructor = declaration.primaryConstructor
      val secondaryConstructors = declaration.secondaryConstructors
      checkPrimaryConstructor(context.trace, primaryConstructor, secondaryConstructors) ?: return
      declaration.checkParamsAndProperties(context.trace, primaryConstructor, secondaryConstructors.singleOrNull())
    }
  }

  private fun checkPrimaryConstructor(
    trace: BindingTrace,
    primaryConstructor: KtPrimaryConstructor?,
    secondaryConstructors: List<KtSecondaryConstructor>
  ): Unit? {
    if (primaryConstructor == null && secondaryConstructors.size > 1) {
      secondaryConstructors.forEach { trace.report(InfoErrors.NeedPrimaryConstructor.on(it)) }
      return null
    }
    return Unit
  }

  private fun KtClassOrObject.checkParamsAndProperties(
    trace: BindingTrace,
    primaryConstructor: KtPrimaryConstructor?,
    secondaryConstructor: KtSecondaryConstructor?
  ) {
    val params = primaryConstructor.ifNull { secondaryConstructor }?.valueParameters?.map { it.name } ?: return
    body?.properties?.forEach {
      val name = it.name!!
      if (params.contains(name)) {
        trace.report(InfoErrors.PropertyAndParamNameConflict.on(it.nameIdentifier ?: it, name))
        return
      }
    }
  }

  private fun KtClassOrObject.checkSpecialModifier(trace: BindingTrace, modifier: KtModifierKeywordToken): Unit? {
    if (hasModifier(modifier)) {
      trace.report(
        InfoErrors.UnsupportedSpecialClass.on(
          modifierList?.getModifier(modifier) ?: nameIdentifier ?: this, modifier.value
        )
      )
      return null
    }
    return Unit
  }
}
