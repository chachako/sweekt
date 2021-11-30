package com.meowool.sweekt

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiTreeUtil.getParentOfType
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtNamedDeclaration
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
 * @author 凛 (https://github.com/RinOrz)
 */
class LeakingSuppressor : InspectionSuppressor {
  override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
    if (element.language != KotlinLanguage.INSTANCE && toolId != "LeakingThis") return false
    return getParentOfType(element, KtProperty::class.java)?.isLazyInitProperty() == true
  }

  override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> =
    SuppressQuickFix.EMPTY_ARRAY
}