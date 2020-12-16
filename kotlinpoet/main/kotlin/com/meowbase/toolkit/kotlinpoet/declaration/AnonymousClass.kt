package com.meowbase.toolkit.kotlinpoet.declaration

import com.squareup.kotlinpoet.TypeSpec


/**
 * 创建一个匿名类
 *
 * @param declare 对这个类的其他声明
 */
inline fun buildAnonymousClassType(
  declare: DeclareType = {}
): TypeSpec = TypeDeclaration(null, TypeSpec.anonymousClassBuilder()).also(declare).get()
