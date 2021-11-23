package com.meowool.sweekt

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtNamedDeclaration

/**
 * ```
 * abstract class Example {
 *   abstract val foo: String
 *   val bar = wrap(foo)
 *                  ^^^ Accessing non-final property 'foo' in constructor
 * }
 * ```
 *
 * @author 凛 (https://github.com/RinOrz)
 */
class LeakingSuppressor : InspectionSuppressor {
  override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
    return toolId == "LeakingThis" &&
      element.language == KotlinLanguage.INSTANCE &&
      element.parent.let { it is KtNamedDeclaration && it.isLazyInitProperty() }
  }

  override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> =
    SuppressQuickFix.EMPTY_ARRAY
}