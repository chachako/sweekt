package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec


/**
 * 返回一个根据 [name] 创建的枚举
 *
 * @param declare 对这个枚举的其他声明
 */
inline fun buildEnumType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name, TypeSpec.enumBuilder(name)).also(declare).get()

/**
 * 返回一个根据 [name] 创建的枚举
 *
 * @param declare 对这个枚举的其他声明
 */
inline fun buildEnumType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name.simpleName, TypeSpec.enumBuilder(name)).also(declare).get()


/**
 * 根据 [name] 创建一个枚举并添加到 [FileDeclaration]
 *
 * @param declare 对这个枚举的其他声明
 */
inline fun FileDeclaration.enumType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name, TypeSpec.enumBuilder(name)).also(declare).get()

/**
 * 根据 [name] 创建一个枚举并添加到 [FileDeclaration]
 *
 * @param declare 对这个枚举的其他声明
 */
inline fun FileDeclaration.enumType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name.simpleName, TypeSpec.enumBuilder(name)).also(declare).get()

