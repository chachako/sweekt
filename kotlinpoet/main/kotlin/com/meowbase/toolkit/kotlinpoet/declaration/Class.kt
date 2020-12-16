package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec


/**
 * 返回一个根据 [name] 创建的类
 *
 * @param declare 对这个类的其他声明
 */
inline fun buildClassType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name, TypeSpec.classBuilder(name)).also(declare).get()

/**
 * 返回一个根据 [name] 创建的类
 *
 * @param declare 对这个类的其他声明
 */
inline fun buildClassType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name.simpleName, TypeSpec.classBuilder(name)).also(declare).get()


/**
 * 根据 [name] 创建一个类并添加到 [FileDeclaration]
 *
 * @param declare 对这个类的其他声明
 */
inline fun FileDeclaration.classType(
  name: String,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name, TypeSpec.classBuilder(name)).also(declare).get()

/**
 * 根据 [name] 创建一个类并添加到 [FileDeclaration]
 *
 * @param declare 对这个类的其他声明
 */
inline fun FileDeclaration.classType(
  name: ClassName,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name.simpleName, TypeSpec.classBuilder(name)).also(declare).get()

