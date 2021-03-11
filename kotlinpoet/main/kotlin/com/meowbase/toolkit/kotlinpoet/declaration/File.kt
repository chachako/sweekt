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

import com.google.devtools.ksp.processing.CodeGenerator
import com.meowbase.toolkit.kotlinpoet.annotation.DslApi
import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass

typealias DeclareFile = FileDeclaration.() -> Unit

/**
 * [FileSpec.Builder] 的 DSL 包装器
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 23:36
 */
@DslApi
class FileDeclaration(
  @PublishedApi
  internal val builder: FileSpec.Builder
) : SpecDeclaration<FileSpec> {
  /** 这个文件的所在包名 */
  val packageName: String get() = builder.packageName

  /** 这个文件的名称 */
  val name: String get() = builder.name

  /** 这个文件中所有声明的类型 */
  val types: List<TypeSpec> get() = builder.members.filterIsInstance<TypeSpec>()

  val imports: List<Import> get() = builder.imports

  fun import(`class`: KClass<*>, names: Iterable<String>) {
    builder.addImport(`class`, names)
  }

  fun import(`class`: KClass<*>, vararg names: String) {
    builder.addImport(`class`, *names)
  }

  fun import(className: ClassName, names: Iterable<String>) {
    builder.addImport(className, names)
  }

  fun import(className: ClassName, vararg names: String) {
    builder.addImport(className, *names)
  }

  fun import(packageName: String, names: Iterable<String>) {
    builder.addImport(packageName, names)
  }

  fun import(packageName: String, vararg names: String) {
    builder.addImport(packageName, *names)
  }

  fun aliasedImport(`class`: Class<*>, `as`: String) {
    builder.addAliasedImport(`class`, `as`)
  }

  fun aliasedImport(`class`: KClass<*>, `as`: String) {
    builder.addAliasedImport(`class`, `as`)
  }

  fun aliasedImport(className: ClassName, `as`: String) {
    builder.addAliasedImport(className, `as`)
  }

  fun aliasedImport(className: ClassName, memberName: String, `as`: String) {
    builder.addAliasedImport(className, memberName, `as`)
  }

  fun aliasedImport(memberName: MemberName, `as`: String) {
    builder.addAliasedImport(memberName, `as`)
  }

  fun TypeSpec.asClassName(): ClassName = this.asClassName(packageName)

  /** 添加一个 [TypeSpec] 到这个文件中 */
  operator fun TypeSpec.unaryPlus() = also(builder::addType)

  /** 添加一个 [FunSpec] 到这个文件中 */
  operator fun FunSpec.unaryPlus() = also(builder::addFunction)

  /** 添加一个 [PropertySpec] 到这个文件中 */
  operator fun PropertySpec.unaryPlus() = also(builder::addProperty)

  /** 添加一个 [AnnotationSpec] 到这个文件中 */
  operator fun AnnotationSpec.unaryPlus() = also(builder::addAnnotation)

  override fun get(): FileSpec = builder.build()
}


fun FileSpec.toDeclaration(): FileDeclaration = FileDeclaration(this.toBuilder())

/** 写出这个文件到 KSP [CodeGenerator] */
fun FileSpec.writeTo(codeGenerator: CodeGenerator) = codeGenerator
  .createNewFile(packageName, name)
  .bufferedWriter()
  .use(::writeTo)


/**
 * 根据给定的 [declare] 创建一个源文件
 *
 * @param fullName 这个文件的所在包与文件名
 * @param declare 对这个文件的其他声明
 */
inline fun declareFile(
  fullName: String,
  declare: DeclareFile = {}
): FileSpec = declareFile(
  packageName = fullName.substringBeforeLast("."),
  fileName = fullName.substringAfterLast("."),
  block = declare
)

/**
 * 根据给定的 [block] 创建一个源文件
 *
 * @param packageName 这个文件的所在包
 * @param fileName 这个文件的名称
 * @param block 对这个文件的其他声明
 */
inline fun declareFile(
  packageName: String,
  fileName: String,
  block: DeclareFile = {}
): FileSpec = FileDeclaration(FileSpec.builder(packageName, fileName)).also(block).get()
