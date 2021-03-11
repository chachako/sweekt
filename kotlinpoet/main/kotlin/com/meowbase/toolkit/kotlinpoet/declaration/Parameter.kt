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

import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass


typealias DeclareParameter = ParameterDeclaration.() -> Unit

/**
 * [ParameterSpec.Builder] 的 DSL 包装器
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 23:36
 */
@DslApi
class ParameterDeclaration(
  @PublishedApi
  internal val builder: ParameterSpec.Builder
) : SpecDeclaration<ParameterSpec> {

  fun modifiers(vararg modifiers: KModifier) {
    builder.addModifiers(*modifiers)
  }

  infix fun modifiers(modifiers: Iterable<KModifier>) = apply {
    builder.addModifiers(modifiers)
  }

  infix fun default(value: String) = apply {
    builder.defaultValue(value)
  }

  inline fun default(declare: DeclareCode) {
    builder.defaultValue(
      CodeDeclaration().apply(declare).get()
    )
  }

  fun default(format: String, vararg args: Any?) {
    builder.defaultValue(format, *args)
  }

  infix fun kdoc(value: String) = apply {
    builder.addKdoc(value)
  }

  infix fun kdoc(block: CodeBlock) = apply {
    builder.addKdoc(block)
  }

  inline fun kdoc(declare: DeclareCode) {
    builder.addKdoc(
      CodeDeclaration().apply(declare).get()
    )
  }

  fun kdoc(format: String, vararg args: Any) = apply {
    builder.addKdoc(format, *args)
  }

  operator fun AnnotationSpec.unaryPlus() = also(builder::addAnnotation)

  override fun get(): ParameterSpec = builder.build()
}


fun ParameterSpec.toDeclaration(): ParameterDeclaration = ParameterDeclaration(this.toBuilder())


/**
 * 创建一个参数列表
 *
 * @param name 参数名称
 * @param type 参数类型
 * @param declare 对这个参数列表的其他声明
 */
inline fun buildParameter(
  name: String,
  type: TypeName,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = ParameterDeclaration(
  ParameterSpec.builder(name, type, *modifiers)
).also(declare).get()

/**
 * 创建一个参数列表
 *
 * @param name 参数名称
 * @param type 参数类型
 * @param declare 对这个参数列表的其他声明
 */
inline fun buildParameter(
  name: String,
  type: KClass<*>,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = buildParameter(name, type.asTypeName(), *modifiers, declare = declare)

/**
 * 创建一个参数列表
 *
 * @param T 参数类型
 * @param name 参数名称
 * @param declare 对这个参数列表的其他声明
 */
inline fun <reified T> buildParameter(
  name: String,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = buildParameter(name, T::class, *modifiers, declare = declare)


/**
 * 创建一个参数列表并添加到 [FunDeclaration] 中
 *
 * @param name 参数名称
 * @param type 参数类型
 * @param declare 对这个参数列表的其他声明
 */
inline fun FunDeclaration.parameter(
  name: String,
  type: TypeName,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = +ParameterDeclaration(
  ParameterSpec.builder(name, type, *modifiers)
).also(declare).get()

/**
 * 创建一个参数列表并添加到 [FunDeclaration] 中
 *
 * @param name 参数名称
 * @param type 参数类型
 * @param declare 对这个参数列表的其他声明
 */
inline fun FunDeclaration.parameter(
  name: String,
  type: KClass<*>,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = parameter(name, type.asTypeName(), *modifiers, declare = declare)

/**
 * 创建一个参数列表并添加到 [FunDeclaration] 中
 *
 * @param T 参数类型
 * @param name 参数名称
 * @param declare 对这个参数列表的其他声明
 */
inline fun <reified T> FunDeclaration.parameter(
  name: String,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = parameter(name, T::class, *modifiers, declare = declare)


/**
 * 创建一个参数列表并添加到 [LambdaTypeDeclaration] 中
 *
 * @param name 参数名称
 * @param type 参数类型
 * @param declare 对这个参数列表的其他声明
 */
inline fun LambdaTypeDeclaration.parameter(
  name: String,
  type: TypeName,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = +ParameterDeclaration(
  ParameterSpec.builder(name, type, *modifiers)
).also(declare).get()

/**
 * 创建一个参数列表并添加到 [LambdaTypeDeclaration] 中
 *
 * @param name 参数名称
 * @param type 参数类型
 * @param declare 对这个参数列表的其他声明
 */
inline fun LambdaTypeDeclaration.parameter(
  name: String,
  type: KClass<*>,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = parameter(name, type.asTypeName(), *modifiers, declare = declare)

/**
 * 创建一个参数列表并添加到 [LambdaTypeDeclaration] 中
 *
 * @param T 参数类型
 * @param name 参数名称
 * @param declare 对这个参数列表的其他声明
 */
inline fun <reified T> LambdaTypeDeclaration.parameter(
  name: String,
  vararg modifiers: KModifier,
  declare: DeclareParameter = {}
): ParameterSpec = parameter(name, T::class, *modifiers, declare = declare)