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
package com.meowool.sweekt

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtProperty

/**
 * ```
 * abstract class Example {
 *   abstract val foo: String
 *   @LazyInit val bar = wrap(foo)
 *                  ^^^ Accessing non-final property 'foo' in constructor
 * }
 * ```
 *
 * @author 凛 (RinOrz)
 */
class LeakingSuppressor : InspectionSuppressor {
  override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
    if (element.language != KotlinLanguage.INSTANCE && toolId != "LeakingThis") return false
    return getParentOfType(element, KtProperty::class.java)?.isLazyInitProperty() == true
  }

  override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> =
    SuppressQuickFix.EMPTY_ARRAY
}
