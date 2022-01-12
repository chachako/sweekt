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
package com.meowool.sweekt.suspend

import com.meowool.sweekt.SweektNames
import com.meowool.sweekt.SweektNames.suspendGetter
import com.meowool.sweekt.SweektNames.suspendSetter
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyAccessor
import org.jetbrains.kotlin.psi.KtReturnExpression
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.checkers.DeclarationChecker
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

/**
 * @author 凛 (RinOrz)
 */
object SuspendPropertyChecker : DeclarationChecker {
  override fun check(
    declaration: KtDeclaration,
    descriptor: DeclarationDescriptor,
    context: DeclarationCheckerContext
  ) {
    if (descriptor !is PropertyDescriptor) return
    if (declaration !is KtProperty) return
    if (!descriptor.annotations.hasAnnotation(SweektNames.Suspend)) return

    declaration.getter?.also { it.requiredSuspendGetter(context.trace, descriptor) }
    declaration.setter?.also { it.requiredSuspendSetter(context.trace, descriptor) }
  }

  private fun KtPropertyAccessor.requiredSuspendGetter(trace: BindingTrace, property: DeclarationDescriptor) {
    val call = initializer as? KtCallExpression ?: bodyBlockExpression?.statements
      ?.filterIsInstance<KtReturnExpression>()?.firstOrNull()
      ?.returnedExpression as? KtCallExpression

    val resolvedCall = call?.calleeExpression?.getResolvedCall(trace.bindingContext)?.resultingDescriptor
    if (resolvedCall?.fqNameSafe != suspendGetter) {
      trace.report(SuspendPropertyErrors.RequiredSuspendGetter.on(this, property))
    }
  }

  private fun KtPropertyAccessor.requiredSuspendSetter(trace: BindingTrace, property: DeclarationDescriptor) {
    val call = initializer as? KtCallExpression ?: bodyBlockExpression?.statements
      ?.filterIsInstance<KtCallExpression>()?.firstOrNull { it.calleeExpression?.text == "suspendSetter" }

    val resolvedCall = call?.calleeExpression?.getResolvedCall(trace.bindingContext)?.resultingDescriptor
    if (resolvedCall?.fqNameSafe != suspendSetter) {
      trace.report(SuspendPropertyErrors.RequiredSuspendSetter.on(this, property))
    }
  }
}
