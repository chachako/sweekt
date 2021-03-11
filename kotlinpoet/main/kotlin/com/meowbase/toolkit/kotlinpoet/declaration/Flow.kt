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

/**
 * 用于声明流式代码
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/12 - 19:09
 */
inline class FlowDeclaration(
  @PublishedApi
  internal val declaration: CodeDeclaration
) {

  /**
   * 创建下一个流式 Api
   *
   * @param controlFlow 控制流开始的语句
   * @param args 格式化 [controlFlow]
   * @param coding 这个控制流内的代码
   */
  inline fun next(controlFlow: String, vararg args: Any?, coding: DeclareCode): FlowDeclaration {
    declaration.add(".", line = true)
    declaration.flow(controlFlow, args, coding = coding)
    return this
  }
}