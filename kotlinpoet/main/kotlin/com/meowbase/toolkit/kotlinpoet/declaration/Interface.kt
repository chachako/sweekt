package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec


/**
 * 返回一个根据 [name] 创建的接口
 *
 * @param declare 对这个接口的其他声明
 */
inline fun buildInterfaceType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name, TypeSpec.interfaceBuilder(name)).also(declare).get()

/**
 * 返回一个根据 [name] 创建的接口
 *
 * @param declare 对这个接口的其他声明
 */
inline fun buildInterfaceType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name.simpleName, TypeSpec.interfaceBuilder(name)).also(declare).get()


/**
 * 根据 [name] 创建一个接口并添加到 [FileDeclaration]
 *
 * @param declare 对这个接口的其他声明
 */
inline fun FileDeclaration.interfaceType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name, TypeSpec.interfaceBuilder(name)).also(declare).get()

/**
 * 根据 [name] 创建一个接口并添加到 [FileDeclaration]
 *
 * @param declare 对这个接口的其他声明
 */
inline fun FileDeclaration.interfaceType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name.simpleName, TypeSpec.interfaceBuilder(name)).also(declare).get()

