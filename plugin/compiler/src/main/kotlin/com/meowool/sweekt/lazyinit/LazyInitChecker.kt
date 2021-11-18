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

import com.meowool.sweekt.SweektNames.JvmField
import com.meowool.sweekt.SweektNames.LazyInit
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.checkers.DeclarationChecker
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext
import org.jetbrains.kotlin.resolve.source.getPsi

/**
 * @author 凛 (https://github.com/RinOrz)
 */
object LazyInitChecker : DeclarationChecker {
  override fun check(
    declaration: KtDeclaration,
    descriptor: DeclarationDescriptor,
    context: DeclarationCheckerContext
  ) {
    if (descriptor !is PropertyDescriptor) return
    if (declaration !is KtProperty) return
    if (!descriptor.annotations.hasAnnotation(LazyInit)) return

    descriptor.checkJvmField(context.trace, declaration) ?: return
    descriptor.checkGetter(context.trace, declaration) ?: return
    descriptor.checkInitializer(context.trace, declaration) ?: return
  }

  private fun PropertyDescriptor.checkJvmField(trace: BindingTrace, property: KtProperty): Unit? {
    val jvmField = backingField?.annotations?.findAnnotation(JvmField) ?: return Unit
    trace.report(LazyInitErrors.NotAllowedJvmField.on(jvmField.source.getPsi() ?: property, this))
    return null
  }

  private fun PropertyDescriptor.checkGetter(trace: BindingTrace, property: KtProperty): Unit? {
    val getter = property.getter
    if (getter != null && getter.hasBody()) {
      trace.report(LazyInitErrors.NotAllowedGetter.on(getter, this, getter))
      return null
    }
    return Unit
  }

  private fun PropertyDescriptor.checkInitializer(trace: BindingTrace, property: KtProperty): Unit? {
    if (property.initializer == null) {
      trace.report(LazyInitErrors.RequiredInitializer.on(property.nameIdentifier ?: property, this))
      return null
    }
    return Unit
  }
}
