package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec


/**
 * 返回一个根据 [name] 创建的 SAM 接口
 *
 * @param declare 对这个 SAM 接口的其他声明
 */
inline fun buildFunInterfaceType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name, TypeSpec.funInterfaceBuilder(name)).also(declare).get()

/**
 * 返回一个根据 [name] 创建的 SAM 接口
 *
 * @param declare 对这个 SAM 接口的其他声明
 */
inline fun buildFunInterfaceType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name.simpleName, TypeSpec.funInterfaceBuilder(name)).also(declare).get()


/**
 * 根据 [name] 创建一个 SAM 接口并添加到 [FileDeclaration]
 *
 * @param declare 对这个 SAM 接口的其他声明
 */
inline fun FileDeclaration.funInterfaceType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name, TypeSpec.funInterfaceBuilder(name)).also(declare).get()

/**
 * 根据 [name] 创建一个 SAM 接口并添加到 [FileDeclaration]
 *
 * @param declare 对这个 SAM 接口的其他声明
 */
inline fun FileDeclaration.funInterfaceType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name.simpleName, TypeSpec.funInterfaceBuilder(name)).also(declare).get()

