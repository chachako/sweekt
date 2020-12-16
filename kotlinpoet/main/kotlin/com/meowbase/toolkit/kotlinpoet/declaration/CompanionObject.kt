package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.TypeSpec


/**
 * 返回一个根据 [name] 创建的 CompanionObject
 *
 * @param declare 对这个 CompanionObject 的其他声明
 */
inline fun buildCompanionObjectType(
  name: String? = null,
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(name, TypeSpec.companionObjectBuilder(name)).also(declare).get()


/**
 * 根据 [name] 创建一个 CompanionObject 并添加到 [FileDeclaration]
 *
 * @param declare 对这个 CompanionObject 的其他声明
 */
inline fun FileDeclaration.companionObjectType(
  name: String? = null,
  declare: DeclareType = {}
): TypeSpec = +TypeDeclaration(name, TypeSpec.companionObjectBuilder(name)).also(declare).get()

