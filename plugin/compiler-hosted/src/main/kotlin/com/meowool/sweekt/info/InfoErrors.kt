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
package com.meowool.sweekt.info

import com.google.auto.service.AutoService
import com.meowool.sweekt.AbstractErrors
import org.jetbrains.kotlin.diagnostics.rendering.DefaultErrorMessages
import org.jetbrains.kotlin.diagnostics.rendering.Renderers

/**
 * @author 凛 (https://github.com/RinOrz)
 */
object InfoErrors : AbstractErrors() {
  @JvmField val NeedPrimaryConstructor = renderError(
    "Class marked with @Info declares multiple constructors, please declare the primary constructor explicitly, " +
      "or change the marker to: `@Info(generateCopy = false)`."
  )

  @JvmField val UnsupportedSpecialClass = renderError(
    "It is not allowed to mark @Info on the {0} class.",
    Renderers.STRING
  )

  @JvmField val PropertyAndParamNameConflict = renderError(
    "Property ''{0}'' name conflicts with the parameter in the constructor. " +
      "This is not allowed in the class marked with `@Info(generateCopy = true)`",
    Renderers.STRING
  )

  @JvmField val ComponentRequiredOperator = renderError(
    "In the class marked with @Info, " +
      "function with name in the format of 'component + \$number' must be with 'operator' modifier.",
  )

  @JvmField val NotAllowedOverrideSynthetic = renderError(
    "It is not allowed to manually override the method ''{0}'', this is a stub synthetic method specific to @Info class.",
    Renderers.STRING
  )

  @JvmField val SuperInfoTypes = renderError(
    "It is found super types ({0}) marked with @Info, so this declaration must be marked with @Info.",
    Renderers.STRING
  )

  @AutoService(DefaultErrorMessages.Extension::class)
  class Messages : AbstractErrors.Messages(renderers)

  init {
    initFactory(Messages())
  }
}
