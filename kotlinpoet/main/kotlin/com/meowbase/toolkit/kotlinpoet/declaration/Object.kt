package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec


/**
 * 返回一个根据 [name] 创建的 Object
 *
 * @param declare 对这个 Object 的其他声明
 */
inline fun buildObjectType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name, TypeSpec.objectBuilder(name)).also(declare).get()

/**
 * 返回一个根据 [name] 创建的 Object
 *
 * @param declare 对这个 Object 的其他声明
 */
inline fun buildObjectType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name.simpleName, TypeSpec.objectBuilder(name)).also(declare).get()


/**
 * 根据 [name] 创建一个 Object 并添加到 [FileDeclaration]
 *
 * @param declare 对这个 Object 的其他声明
 */
inline fun FileDeclaration.objectType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name, TypeSpec.objectBuilder(name)).also(declare).get()

/**
 * 根据 [name] 创建一个 Object 并添加到 [FileDeclaration]
 *
 * @param declare 对这个 Object 的其他声明
 */
inline fun FileDeclaration.objectType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name.simpleName, TypeSpec.objectBuilder(name)).also(declare).get()

