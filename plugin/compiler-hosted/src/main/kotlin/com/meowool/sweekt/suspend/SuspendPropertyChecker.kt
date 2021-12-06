package com.meowool.sweekt.suspend

import com.meowool.sweekt.SweektNames.suspendGetter
import com.meowool.sweekt.SweektNames.suspendSetter
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.name.Name
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
 * @author 凛 (https://github.com/RinOrz)
 */
object SuspendPropertyChecker : DeclarationChecker {
  override fun check(
    declaration: KtDeclaration,
    descriptor: DeclarationDescriptor,
    context: DeclarationCheckerContext
  ) {
    if (descriptor !is PropertyDescriptor) return
    if (declaration !is KtProperty) return

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