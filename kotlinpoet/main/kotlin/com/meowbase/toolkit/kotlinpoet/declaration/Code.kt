@file:Suppress("OVERRIDE_BY_INLINE")

package com.meowbase.toolkit.kotlinpoet.declaration

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec


typealias DeclareCode = Coding.() -> Unit

@DslApi
interface Coding {

  /**
   * 添加代码语句
   *
   * @param format 需要格式化的语句
   * @param args 格式化参数
   * @param line 如果为 true, 则代表这是一行语句，在尾端将换行
   */
  fun add(format: String, vararg args: Any?, line: Boolean = true)

  /** 添加代码块 [codeBlock] */
  fun add(codeBlock: CodeBlock)

  /**
   * 添加一个缩进后的代码块
   *
   * @param coding 缩进的代码块
   */
  fun indent(coding: DeclareCode)

  /**
   * 创建一个流式 Api
   *
   * @param controlFlow 控制流开始的语句
   * @param args 格式化 [controlFlow]
   * @param coding 这个控制流内的代码
   * @return see [FlowDeclaration]
   */
  fun flow(controlFlow: String, vararg args: Any?, coding: DeclareCode): FlowDeclaration

  /**
   * 创建调用语句
   *
   * @param target 调用的目标方法名称
   * @param declareParameters 声明调用的目标方法的参数
   */
  fun call(target: String, declareParameters: DeclareCall)
}

/**
 * [ParameterSpec.Builder] 的 DSL 包装器
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/12 - 15:31
 */
@DslApi
class CodeDeclaration(
  @PublishedApi
  internal val builder: CodeBlock.Builder = CodeBlock.builder()
) : SpecDeclaration<CodeBlock>, Coding {

  override fun add(format: String, vararg args: Any?, line: Boolean) {
    if(line) {
      builder.addStatement(format, *args)
    } else {
      builder.add(format, *args)
    }
  }

  override fun add(codeBlock: CodeBlock) {
    builder.add(codeBlock)
  }

  override fun indent(coding: DeclareCode) {
    builder.indent()
    coding()
    builder.unindent()
  }

  override inline fun flow(controlFlow: String, vararg args: Any?, coding: DeclareCode): FlowDeclaration {
    builder.beginControlFlow(controlFlow, *args)
    coding()
    builder.endControlFlow()
    return FlowDeclaration(this)
  }

  override inline fun call(target: String, crossinline declareParameters: DeclareCall) {
    add("$target(")
    indent { CallDeclaration(this@CodeDeclaration).also(declareParameters) }
    add(")")
  }

  override fun get(): CodeBlock = builder.build()
}