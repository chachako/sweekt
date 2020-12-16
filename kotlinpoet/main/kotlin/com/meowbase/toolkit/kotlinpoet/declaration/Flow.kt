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