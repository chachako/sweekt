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
package com.meowool.sweekt.lazyinit

import com.google.auto.service.AutoService
import com.meowool.sweekt.AbstractErrors
import org.jetbrains.kotlin.diagnostics.rendering.DefaultErrorMessages
import org.jetbrains.kotlin.diagnostics.rendering.Renderers

/**
 * @author 凛 (RinOrz)
 */
object LazyInitErrors : AbstractErrors() {
  @JvmField val NotAllowedJvmField = renderError(
    "Property ''{0}'' marked with @LazyInit cannot be marked with @JvmField at the same time.",
    Renderers.NAME
  )

  @JvmField val NotAllowedGetter = renderError(
    "Property ''{0}'' marked with @LazyInit cannot declare getter (`{1}`) at the same time.",
    Renderers.NAME,
    Renderers.ELEMENT_TEXT
  )

  @JvmField val RequiredInitializer = renderError(
    "Property ''{0}'' marked with @LazyInit must have initializer (`val {0} = ...`) for the value of lazy initialization.",
    Renderers.NAME,
  )

  @JvmField val ResetReceiverRequiredMarkedProperty = renderError(
    "The extension receiver of the 'resetLazyValue' function must be a property marked with @LazyInit."
  )

  @JvmField val ResetRequiredAtLeastOneArgument = renderError(
    "The called 'resetLazyValues' function requires at least one argument, and the argument must be a property marked with @LazyInit."
  )

  @JvmField val ResetArgumentRequiredMarkedProperty = renderError(
    "Requires a property marked with @LazyInit as a argument of the 'resetLazyValues' function."
  )

  @AutoService(DefaultErrorMessages.Extension::class)
  class Messages : AbstractErrors.Messages(renderers)

  init {
    initFactory(Messages())
  }
}
