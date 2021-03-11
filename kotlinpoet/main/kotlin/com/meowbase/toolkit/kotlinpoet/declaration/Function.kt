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

@file:Suppress("OVERRIDE_BY_INLINE")

package com.meowbase.toolkit.kotlinpoet.declaration

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass

typealias DeclareFun = FunDeclaration.() -> Unit

/**
 * [FunSpec.Builder] 的 DSL 包装器
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 23:36
 */
@DslApi
class FunDeclaration(
  /** 这个方法的名称 */
  val name: String,

  @PublishedApi
  internal val builder: FunSpec.Builder
) : SpecDeclaration<FunSpec>, Coding {

  @PublishedApi
  internal val body = CodeDeclaration()

  /**
   * 这个方法的接收器
   * 代表这个方法是对这个类型的扩展方法
   */
  var receiver: TypeName? = null
    set(value) {
      value?.also(builder::receiver)
      field = value
    }

  /** 这个方法的返回类型 */
  var returns: TypeName? = null
    set(value) {
      value?.also(builder::returns)
      field = value
    }

  /** 添加方法的修饰符 */
  fun modifiers(vararg modifiers: KModifier) {
    builder.addModifiers(*modifiers)
  }

  /** 添加方法的修饰符 */
  fun modifiers(modifiers: Iterable<KModifier>) = apply {
    builder.addModifiers(modifiers)
  }

  /**
   * 设置方法的接收器类型，并写入 Kdoc
   *
   * @param type 这个方法的接收器类型
   * @param kdoc 如果不为 null, 将这个类型作为 kdoc 中的接收值
   * `@receiver T`
   */
  fun receiver(type: TypeName, kdoc: CodeBlock? = null) {
    when(kdoc) {
      null -> builder.receiver(type)
      else -> builder.receiver(type, kdoc)
    }
  }

  /**
   * 设置方法的接收器类型
   * 并将 [kdocArgs] 格式化到 [kdocFormat] 后写入 Kdoc
   *
   * @param type 这个方法的接收器类型
   */
  fun receiver(type: TypeName, kdocFormat: String, vararg kdocArgs: Any) =
    receiver(type, CodeBlock.of(kdocFormat, *kdocArgs))

  /**
   * 设置方法的接收器类型，并写入 Kdoc
   *
   * @param type 这个方法的接收器类型
   * @param kdoc 如果不为 null, 将这个类型作为 kdoc 中的接收值
   * `@receiver T`
   */
  fun receiver(type: KClass<*>, kdoc: CodeBlock? = null) = receiver(type.asTypeName(), kdoc)

  /**
   * 设置方法的接收器类型
   * 并将 [kdocArgs] 格式化到 [kdocFormat] 后写入 Kdoc
   *
   * @param type 这个方法的接收器类型
   */
  fun receiver(type: KClass<*>, kdocFormat: String, vararg kdocArgs: Any) =
    receiver(type.asTypeName(), kdocFormat, *kdocArgs)

  /**
   * 设置方法的接收器类型
   *
   * @param T 这个方法的接收器类型
   * @param kdoc 如果不为 null, 将这个类型作为 kdoc 中的接收值
   * `@receiver T`
   */
  inline fun <reified T> receiver(kdoc: CodeBlock? = null) = receiver(T::class.asTypeName(), kdoc)

  /**
   * 设置方法的接收器类型
   * 并将 [kdocArgs] 格式化到 [kdocFormat] 后写入 Kdoc
   *
   * @param T 这个方法的接收器类型
   */
  inline fun <reified T> receiver(kdocFormat: String, vararg kdocArgs: Any) =
    receiver<T>(CodeBlock.of(kdocFormat, *kdocArgs))

  /**
   * 设置方法的返回类型，并写入 Kdoc
   *
   * @param type 这个方法的返回类型
   * @param kdoc 如果不为 null, 将这个类型作为 kdoc 中的返回值
   * `@return T`
   */
  fun returns(type: TypeName, kdoc: CodeBlock? = null) {
    when(kdoc) {
      null -> builder.returns(type)
      else -> builder.returns(type, kdoc)
    }
  }

  /**
   * 设置方法的返回类型
   * 并将 [kdocArgs] 格式化到 [kdocFormat] 后写入 Kdoc
   *
   * @param type 这个方法的返回类型
   */
  fun returns(type: TypeName, kdocFormat: String, vararg kdocArgs: Any) =
    returns(type, CodeBlock.of(kdocFormat, *kdocArgs))

  /**
   * 设置方法的返回类型，并写入 Kdoc
   *
   * @param type 这个方法的返回类型
   * @param kdoc 如果不为 null, 将这个类型作为 kdoc 中的返回值
   * `@return T`
   */
  fun returns(type: KClass<*>, kdoc: CodeBlock? = null) = returns(type.asTypeName(), kdoc)

  /**
   * 设置方法的返回类型
   * 并将 [kdocArgs] 格式化到 [kdocFormat] 后写入 Kdoc
   *
   * @param type 这个方法的返回类型
   */
  fun returns(type: KClass<*>, kdocFormat: String, vararg kdocArgs: Any) =
    returns(type.asTypeName(), kdocFormat, *kdocArgs)

  /**
   * 设置方法的返回类型
   *
   * @param T 这个方法的返回类型
   * @param kdoc 如果不为 null, 将这个类型作为 kdoc 中的返回值
   * `@return T`
   */
  inline fun <reified T> returns(kdoc: CodeBlock? = null) = returns(T::class.asTypeName(), kdoc)

  /**
   * 设置方法的返回类型
   * 并将 [kdocArgs] 格式化到 [kdocFormat] 后写入 Kdoc
   *
   * @param T 这个方法的返回类型
   */
  inline fun <reified T> returns(kdocFormat: String, vararg kdocArgs: Any) =
    returns<T>(CodeBlock.of(kdocFormat, *kdocArgs))

  /**
   * 设置方法的 Kotlin 文档
   *
   * @param block 代码块
   */
  fun kdoc(block: CodeBlock) {
    builder.addKdoc(block)
  }

  /**
   * 设置方法的 Kotlin 文档
   *
   * @param declare 声明 kdoc
   */
  inline fun kdoc(declare: DeclareCode) {
    builder.addKdoc(
      CodeDeclaration().apply(declare).get()
    )
  }

  /**
   * 设置方法的 Kotlin 文档
   *
   * @param format 需要格式化的语句
   * @param args 格式化参数
   */
  fun kdoc(format: String, vararg args: Any) {
    builder.addKdoc(format, *args)
  }

  /**
   * 设置方法内的代码块
   *
   * @param declare 声明方法内的代码
   */
  inline fun code(declare: DeclareCode) {
    builder.addCode(
      CodeDeclaration().apply(declare).get()
    )
  }


  /**
   * 添加代码语句
   *
   * @param format 需要格式化的语句
   * @param args 格式化参数
   * @param line 如果为 true, 则代表这是一行语句，在尾端将换行
   */
  override fun add(format: String, vararg args: Any?, line: Boolean) =
    body.add(format, *args, line = line)

  /** 添加代码块 [codeBlock] */
  override fun add(codeBlock: CodeBlock) = body.add(codeBlock)

  /**
   * 添加一个缩进后的代码块
   *
   * @param coding 缩进的代码块
   */
  override fun indent(coding: DeclareCode) = body.indent(coding)

  /**
   * 创建一个流式 Api
   *
   * @param controlFlow 控制流开始的语句
   * @param args 格式化 [controlFlow]
   * @param coding 这个控制流内的代码
   */
  override inline fun flow(controlFlow: String, vararg args: Any?, coding: DeclareCode) =
    body.flow(controlFlow, *args, coding = coding)

  /**
   * 创建调用语句
   *
   * @param target 调用的目标方法名称
   * @param declareParameters 声明调用的目标方法的参数
   */
  override inline fun call(target: String, crossinline declareParameters: DeclareCall) =
    body.call(target, declareParameters)


  /** 添加一个 [ParameterSpec] 到方法参数列表中 */
  operator fun ParameterSpec.unaryPlus() = also(builder::addParameter)

  /** 添加 [ParameterList] 到方法中 */
  operator fun ParameterList.unaryPlus() = also(builder::addParameters)

  /** 添加 [AnnotationSpec] 到方法中 */
  operator fun AnnotationSpec.unaryPlus() = also(builder::addAnnotation)

  override fun get(): FunSpec = builder.addCode(body.get()).build()
}


fun FunSpec.toDeclaration(): FunDeclaration = FunDeclaration(name, this.toBuilder())


/**
 * 创建一个方法
 *
 * @param name 方法的名称
 * @param declare 对这个方法的其他声明
 */
inline fun buildFunction(
  name: String,
  declare: DeclareFun = {}
): FunSpec = FunDeclaration(name, FunSpec.builder(name)).also(declare).get()

/**
 * 创建一个方法
 *
 * @param R 方法的返回类型
 * @param name 方法的名称
 * @param declare 对这个方法的其他声明
 */
@JvmName("functionWithReturns")
inline fun <reified R> buildFunction(
  name: String,
  declare: DeclareFun = {}
): FunSpec = buildFunction(name) { returns<R>(); declare() }


/**
 * 创建一个方法并添加到 [TypeDeclaration] 中
 *
 * @param name 方法的名称
 * @param declare 对这个方法的其他声明
 */
inline fun TypeDeclaration.function(
  name: String,
  declare: DeclareFun = {}
): FunSpec = +FunDeclaration(name, FunSpec.builder(name)).also(declare).get()

/**
 * 创建一个方法并添加到 [TypeDeclaration] 中
 *
 * @param R 方法的返回类型
 * @param name 方法的名称
 * @param declare 对这个方法的其他声明
 */
@JvmName("functionWithReturns")
inline fun <reified R> TypeDeclaration.function(
  name: String,
  declare: DeclareFun = {}
): FunSpec = function(name) { returns<R>(); declare() }


/**
 * 创建一个方法并添加到 [FileDeclaration] 中
 *
 * @param name 方法的名称
 * @param declare 对这个方法的其他声明
 */
inline fun FileDeclaration.function(
  name: String,
  declare: DeclareFun = {}
): FunSpec = +FunDeclaration(name, FunSpec.builder(name)).also(declare).get()

/**
 * 创建一个方法并添加到 [FileDeclaration] 中
 *
 * @param R 方法的返回类型
 * @param name 方法的名称
 * @param declare 对这个方法的其他声明
 */
@JvmName("functionWithReturns")
inline fun <reified R> FileDeclaration.function(
  name: String,
  declare: DeclareFun = {}
): FunSpec = function(name) { returns<R>(); declare() }