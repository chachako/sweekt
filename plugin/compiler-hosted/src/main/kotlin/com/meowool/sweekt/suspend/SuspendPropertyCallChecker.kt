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

import com.intellij.psi.PsiElement
import com.meowool.sweekt.SweektNames.Suspend
import org.jetbrains.kotlin.builtins.StandardNames
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtCodeFragment
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtThisExpression
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.isCallableReference
import org.jetbrains.kotlin.resolve.calls.checkers.CallChecker
import org.jetbrains.kotlin.resolve.calls.checkers.CallCheckerContext
import org.jetbrains.kotlin.resolve.calls.checkers.findEnclosingSuspendFunction
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.inline.InlineUtil
import org.jetbrains.kotlin.resolve.scopes.HierarchicalScope
import org.jetbrains.kotlin.resolve.scopes.LexicalScope
import org.jetbrains.kotlin.resolve.scopes.LexicalScopeKind
import org.jetbrains.kotlin.resolve.scopes.receivers.ExpressionReceiver
import org.jetbrains.kotlin.resolve.scopes.receivers.ReceiverValue
import org.jetbrains.kotlin.resolve.scopes.utils.parentsWithSelf
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.supertypes

/**
 * [Copied](https://github.com/JetBrains/kotlin/blob/1.6.20/compiler/frontend/src/org/jetbrains/kotlin/resolve/calls/checkers/coroutineCallChecker.kt)
 *
 * @author 凛 (https://github.com/RinOrz)
 */
object SuspendPropertyCallChecker : CallChecker {
  override fun check(resolvedCall: ResolvedCall<*>, reportOn: PsiElement, context: CallCheckerContext) {
    when (val descriptor = resolvedCall.candidateDescriptor) {
      is PropertyDescriptor -> if (descriptor.annotations.hasAnnotation(Suspend).not()) return
      else -> return
    }

    val callElement = resolvedCall.call.callElement as KtExpression
    val enclosingSuspendFunction = findEnclosingSuspendFunction(context)

    when {
      enclosingSuspendFunction != null -> {
        if (!InlineUtil.checkNonLocalReturnUsage(enclosingSuspendFunction, callElement, context.resolutionContext)) {
          var shouldReport = true

          // Do not report for KtCodeFragment in a suspend function context
          val containingFile = callElement.containingFile
          if (containingFile is KtCodeFragment) {
            val c = containingFile.context?.getParentOfType<KtExpression>(false)
            if (c != null && InlineUtil.checkNonLocalReturnUsage(enclosingSuspendFunction, c, context.resolutionContext)) {
              shouldReport = false
            }
          }

          if (shouldReport) {
            context.trace.report(Errors.NON_LOCAL_SUSPENSION_POINT.on(reportOn))
          }
        } else if (context.scope.parentsWithSelf.any { it.isScopeForDefaultParameterValuesOf(enclosingSuspendFunction) }) {
          context.trace.report(Errors.UNSUPPORTED.on(reportOn, "suspend function calls in a context of default parameter value"))
        }

        context.trace.record(
          BindingContext.ENCLOSING_SUSPEND_FUNCTION_FOR_SUSPEND_FUNCTION_CALL,
          resolvedCall.call,
          enclosingSuspendFunction
        )

        checkRestrictsSuspension(enclosingSuspendFunction, resolvedCall, reportOn, context)
      }
      resolvedCall.call.isCallableReference() -> {
        // do nothing: we can get callable reference to suspend function outside suspend context
      }
      else -> context.trace.report(
        Errors.ILLEGAL_SUSPEND_PROPERTY_ACCESS.on(
          reportOn,
          resolvedCall.candidateDescriptor
        )
      )
    }
  }

  private fun HierarchicalScope.isScopeForDefaultParameterValuesOf(enclosingSuspendFunction: FunctionDescriptor) =
    this is LexicalScope && this.kind == LexicalScopeKind.DEFAULT_VALUE && this.ownerDescriptor == enclosingSuspendFunction

  private fun KotlinType.isRestrictsSuspensionReceiver() = (listOf(this) + this.supertypes()).any {
    it.constructor.declarationDescriptor?.annotations?.hasAnnotation(
      StandardNames.COROUTINES_PACKAGE_FQ_NAME.child(Name.identifier("RestrictsSuspension"))
    ) == true
  }

  private fun checkRestrictsSuspension(
    enclosingSuspendCallableDescriptor: CallableDescriptor,
    resolvedCall: ResolvedCall<*>,
    reportOn: PsiElement,
    context: CallCheckerContext
  ) {
    fun ReceiverValue.isRestrictsSuspensionReceiver() = type.isRestrictsSuspensionReceiver()

    infix fun ReceiverValue.sameInstance(other: ReceiverValue?): Boolean {
      if (other == null) return false
      // Implicit receiver should be reference equal
      if (this.original === other.original) return true

      val referenceExpression = ((other as? ExpressionReceiver)?.expression as? KtThisExpression)?.instanceReference
      val referenceTarget = referenceExpression?.let {
        context.trace.get(BindingContext.REFERENCE_TARGET, referenceExpression)
      }

      val referenceReceiverValue = when (referenceTarget) {
        is CallableDescriptor -> referenceTarget.extensionReceiverParameter?.value
        is ClassDescriptor -> referenceTarget.thisAsReceiverParameter.value
        else -> null
      }

      return this === referenceReceiverValue
    }

    fun reportError() {
      context.trace.report(Errors.ILLEGAL_RESTRICTED_SUSPENDING_FUNCTION_CALL.on(reportOn))
    }

    val enclosingSuspendExtensionReceiverValue = enclosingSuspendCallableDescriptor.extensionReceiverParameter?.value
    val enclosingSuspendDispatchReceiverValue = enclosingSuspendCallableDescriptor.dispatchReceiverParameter?.value

    val receivers = listOfNotNull(resolvedCall.dispatchReceiver, resolvedCall.extensionReceiver)
    for (receiverValue in receivers) {
      if (!receiverValue.isRestrictsSuspensionReceiver()) continue
      if (enclosingSuspendExtensionReceiverValue?.sameInstance(receiverValue) == true) continue
      if (enclosingSuspendDispatchReceiverValue?.sameInstance(receiverValue) == true) continue

      reportError()
      return
    }

    if (enclosingSuspendExtensionReceiverValue?.isRestrictsSuspensionReceiver() != true) return

    // member of suspend receiver
    if (enclosingSuspendExtensionReceiverValue sameInstance resolvedCall.dispatchReceiver) return

    if (enclosingSuspendExtensionReceiverValue sameInstance resolvedCall.extensionReceiver &&
      resolvedCall.candidateDescriptor.extensionReceiverParameter!!.value.isRestrictsSuspensionReceiver()
    ) return

    reportError()
  }
}
