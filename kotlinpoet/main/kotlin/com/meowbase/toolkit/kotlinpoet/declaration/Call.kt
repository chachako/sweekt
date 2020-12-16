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