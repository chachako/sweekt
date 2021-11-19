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
package com.meowool.sweekt.lazyinit

import com.meowool.sweekt.SweektNames.LazyInit
import com.meowool.sweekt.SweektNames.resetLazyValue
import com.meowool.sweekt.SweektNames.resetLazyValues
import com.meowool.sweekt.castOrNull
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.Diagnostic
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.checkers.CallChecker
import org.jetbrains.kotlin.resolve.calls.checkers.CallCheckerContext
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.VarargValueArgument
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.scopes.receivers.ExpressionReceiver

/**
 * @author 凛 (https://github.com/RinOrz)
 */
object ResetValueChecker : CallChecker {
  override fun check(resolvedCall: ResolvedCall<*>, reportOn: PsiElement, context: CallCheckerContext) {
    when (resolvedCall.resultingDescriptor.fqNameSafe) {
      resetLazyValue -> {
        val receiver = resolvedCall.extensionReceiver.castOrNull<ExpressionReceiver>()?.expression
        receiver.requireLazyProperty(
          context.trace,
          LazyInitErrors.ResetReceiverRequiredMarkedProperty.on(receiver ?: reportOn)
        )
      }
      resetLazyValues -> {
        val vararg = resolvedCall.valueArgumentsByIndex?.get(0)?.castOrNull<VarargValueArgument>()
        val arguments = vararg?.arguments
        if (arguments == null || arguments.isEmpty()) {
          context.trace.report(LazyInitErrors.ResetRequiredAtLeastOneArgument.on(reportOn))
          return
        }
        vararg.arguments.forEach {
          val expr = it.getArgumentExpression()
          expr.requireLazyProperty(
            context.trace,
            LazyInitErrors.ResetArgumentRequiredMarkedProperty.on(expr ?: reportOn)
          )
        }
      }
    }
  }

  private fun KtExpression?.requireLazyProperty(trace: BindingTrace, diagnostic: Diagnostic) {
    val property = getResolvedCall(trace.bindingContext)?.resultingDescriptor
    if (property == null || property.annotations.hasAnnotation(LazyInit).not()) {
      trace.report(diagnostic)
    }
  }
}
