package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec


/**
 * 返回一个根据 [name] 创建的类
 *
 * @param declare 对这个类的其他声明
 */
inline fun buildExpectClassType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name, TypeSpec.expectClassBuilder(name)).also(declare).get()

/**
 * 返回一个根据 [name] 创建的类
 *
 * @param declare 对这个类的其他声明
 */
inline fun buildExpectClassType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name.simpleName, TypeSpec.expectClassBuilder(name)).also(declare).get()


/**
 * 根据 [name] 创建一个类并添加到 [FileDeclaration]
 *
 * @param declare 对这个类的其他声明
 */
inline fun FileDeclaration.expectClassType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name, TypeSpec.expectClassBuilder(name)).also(declare).get()

/**
 * 根据 [name] 创建一个类并添加到 [FileDeclaration]
 *
 * @param declare 对这个类的其他声明
 */
inline fun FileDeclaration.expectClassType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name.simpleName, TypeSpec.expectClassBuilder(name)).also(declare).get()

