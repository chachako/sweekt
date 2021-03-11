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

import com.meowbase.toolkit.iterations.toMutableList
import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import kotlin.reflect.KClass

typealias ParameterList = List<ParameterSpec>
typealias DeclareParameters = ParametersDeclaration.() -> Unit

/**
 * 声明一个参数列表
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 23:36
 */
@DslApi
class ParametersDeclaration(
  private val parameters: MutableList<ParameterDeclaration>
) : SpecDeclaration<ParameterList> {

  /**
   * 添加一个参数
   *
   * @receiver 参数的名称
   * @param type 参数的类型
   */
  infix fun String.to(type: TypeName): ParameterDeclaration =
    ParameterDeclaration(ParameterSpec.builder(this, type)).also(parameters::add)

  /**
   * 添加一个参数
   *
   * @receiver 参数的名称
   * @param type 参数的类型
   */
  infix fun String.to(type: KClass<*>): ParameterDeclaration = this to type.asTypeName()

  /** 根据 [declare] 声明这个参数 */
  infix fun ParameterDeclaration.apply(declare: DeclareParameter) {
    also(declare)
  }

  override fun get(): ParameterList = parameters.map { it.get() }
}


fun ParameterList.toDeclaration(): ParametersDeclaration = ParametersDeclaration(
  this.map { ParameterDeclaration(it.toBuilder()) }.toMutableList()
)


/**
 * 创建一个参数列表
 *
 * @param declare 对这个参数列表的其他声明
 */
inline fun buildParameters(
  declare: DeclareParameters = {}
): ParameterList = ParametersDeclaration(mutableListOf()).also(declare).get()

/**
 * 创建一个参数列表并添加到 [FunDeclaration]
 *
 * @param declare 对这个参数列表的其他声明
 */
inline fun FunDeclaration.parameters(
  declare: DeclareParameters = {}
): ParameterList = +ParametersDeclaration(mutableListOf()).also(declare).get()