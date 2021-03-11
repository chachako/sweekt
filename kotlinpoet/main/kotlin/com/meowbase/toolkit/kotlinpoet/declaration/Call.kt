/*
 * Copyright (c) 2021. Rin Orz (凛)
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
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package com.meowbase.toolkit.kotlinpoet.declaration

typealias DeclareCall = CallDeclaration.() -> Unit

/**
 * 用于声明方法调用代码
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/12 - 19:09
 */
inline class CallDeclaration(
  @PublishedApi
  internal val declaration: CodeDeclaration
) {
  /** 添加调用参数语句 */
  @SinceKotlin("1.4")
  fun add(format: String, vararg args: Any?) {
    declaration.add("$format,", *args)
  }
}