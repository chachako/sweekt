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

@file:Suppress("SpellCheckingInspection")

/**
 * 一些类型名称规范转换
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/24 - 21:25
 */
package com.meowbase.toolkit

private const val TypeSeparator = '.'
private const val DescriptorSeparator = '/'
private const val DescriptorPrefix = 'L'
private const val ArrayDescriptorPrefix = '['
private const val DescriptorSuffix = ';'
private const val InnerClassSeparator = '$'
private const val JavaArraySymbol = "[]"


/**
 * 判断给定的 [CharSequence] 是否符合 JVM 的基本类型描述符或名称定义
 *
 * @see isJvmPrimitiveDescriptor
 * @see isJavaPrimitiveTypeName
 */
fun CharSequence.isJvmPrimitiveType() = isJvmPrimitiveDescriptor()
  || isJavaPrimitiveTypeName()

/**
 * 判断给定的 [CharSequence] 是否符合 JVM 的基本类型描述符定义
 *
 * ```
 * I // true
 * int // false
 * ```
 */
fun CharSequence.isJvmPrimitiveDescriptor() = when (this) {
  "I", "Z", "C", "D", "F", "J", "S", "B", "V" -> true
  else -> false
}

/**
 * 判断给定的 [CharSequence] 是否符合 Java 语言的基本类型名称定义
 *
 * ```
 * int // true
 * I // false
 * ```
 */
fun CharSequence.isJavaPrimitiveTypeName() = when (this) {
  "int", "boolean", "char", "double", "float", "long", "short", "byte", "void" -> true
  else -> false
}

/**
 * 判断给定的 [CharSequence] 是否符合 JVM 描述符定义
 * [参考](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3)
 *
 * ```
 * Lcom/a/b; // true
 * Lcom.a.b; // false
 * com/a/b // false
 * com.a.b // false
 * ```
 *
 * @see isJvmPrimitiveDescriptor
 */
fun CharSequence.isJvmDescriptor() = this.isJvmPrimitiveDescriptor() || run {
  this.last() == DescriptorSuffix
    && this.first().run { equals(DescriptorPrefix) || equals(ArrayDescriptorPrefix) }
    && this.contains(DescriptorSeparator)
}

/**
 * 将给定的 [CharSequence] 转换为 JVM 规范的描述符定义
 *
 * ```
 * boolean -> Z
 * java.lang.Object -> Ljava/lang/Object;
 * [I -> [I;
 * [La.b.C; -> [La/b/C;
 * ```
 *
 * [Reference](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3)
 */
fun CharSequence.toJvmDescriptor(): String {
  // 储存可能存在的 `[[[...` 然后在 return 时加到描述符前面
  var arrayDescriptor = ""
  fun String.tryWrapArray() = takeIf { arrayDescriptor.isEmpty() }?.run { this }
    ?: takeIf { it.last() == DescriptorSuffix }?.run { "$arrayDescriptor$this" }
    ?: "$arrayDescriptor$this$DescriptorSuffix"

  var descriptor = this.toString().replace(TypeSeparator, DescriptorSeparator)
  if (descriptor.isJvmDescriptor()) return descriptor

  // 以 `[]` 结尾的话，我们需要把它改为 `[`
  while (descriptor.endsWith(JavaArraySymbol)) {
    descriptor = descriptor.removeSuffix(JavaArraySymbol)
    arrayDescriptor += ArrayDescriptorPrefix
  }

  if (descriptor.isJavaPrimitiveTypeName()) {
    return when (descriptor) {
      "int" -> "I"
      "boolean" -> "Z"
      "char" -> "C"
      "double" -> "D"
      "float" -> "F"
      "long" -> "J"
      "short" -> "S"
      "byte" -> "B"
      "void" -> "V"
      else -> error("$descriptor 是一个 Java 基本类型名称定义，但发生意外错误。")
    }.tryWrapArray()
  }

  descriptor = descriptor.tryWrapArray()
  if (descriptor.last() != DescriptorSuffix) {
    // 修复开头存在 `[` 但是结尾少了 `;`
    return takeIf { descriptor.first() == ArrayDescriptorPrefix }
      ?.run { "$descriptor$DescriptorSuffix" }
    // 修复缺少开头 `L` 和结尾 `;` 的情况
      ?: "$DescriptorPrefix$descriptor$DescriptorSuffix"
  }

  return descriptor
}

/**
 * 将给定的 [CharSequence] 转换为 JVM 符合预期的限定名
 *
 * ```
 * I -> int
 * Ljava/lang/Object; -> java.lang.Object
 * [Ljava/lang/Object; -> [Ljava.lang.Object;
 *
 * // 当 canonical 开启时
 * [La.b.Foo$Bar -> a.b.Foo.Bar[]
 * ```
 *
 * @param canonical 规范化名称
 *   例如 `[La.b.Foo$Bar` 将会规范为 `a.b.Foo.Bar[]`
 * @param separator 转换后限定名称的分割符，默认为 `.`
 */
fun CharSequence.toJvmQualifiedName(
  canonical: Boolean = false,
  separator: Char = TypeSeparator,
): String {
  // 储存可能存在的 `[][]...` 然后在 canonical 情况下 return 时加到限定名后面
  var arraySymbols = ""

  var qualified = this.toString()
    .replace(DescriptorSeparator, separator)

  if (qualified.first() == DescriptorPrefix && qualified.last() == DescriptorSuffix) {
    // 只有开头为 `L` 时我们才去掉 `;` 否则它可能是一个 `[Lxxx;`
    qualified = qualified.substring(1, qualified.lastIndex)
  }

  if (canonical) {
    qualified = qualified.replace(InnerClassSeparator, separator)

    // 以 `[` 开头的话，我们需要把它改为 `[]`
    while (qualified.startsWith(ArrayDescriptorPrefix)) {
      qualified = qualified.substring(
        qualified.startsWith("$ArrayDescriptorPrefix$DescriptorPrefix") that 2 ?: 1
      )
      arraySymbols += JavaArraySymbol
    }
  }

  return run {
    !qualified.isJvmPrimitiveDescriptor() that qualified ?: when (qualified) {
      "I" -> "int"
      "Z" -> "boolean"
      "C" -> "int"
      "D" -> "double"
      "F" -> "float"
      "J" -> "long"
      "S" -> "short"
      "B" -> "byte"
      "V" -> "void"
      else -> error("$qualified 是一个 JVM 基本类型描述符定义，但发生意外错误。")
    }
  } + arraySymbols
}

/**
 * 将给定的 [CharSequence] 转换为 JVM 规范的简单名称定义
 *
 * ```
 * Z -> boolean
 * [I -> int[]
 * java.lang.Object -> Object
 * [La.b.Foo$Bar -> Bar
 * ```
 */
fun CharSequence.toJvmSimpleName(): String = this.toJvmQualifiedName(canonical = true)
  .substringAfterLast(TypeSeparator)

/**
 * 将给定的 [CharSequence] 转换为 JVM 规范的包名定义
 * 注意：如果给定的 [CharSequence] 是基本类型定义将会返回 `java.lang`
 *
 * ```
 * a.b.Foo$Bar -> a.b
 * I -> java.lang
 * ```
 */
fun CharSequence.toJvmPackageName(): String = this.toString()
  .substringBeforeLast(InnerClassSeparator)
  .toJvmQualifiedName(canonical = true)
  .replace(JavaArraySymbol, "").run {
    isJvmPrimitiveType() that "java.lang"
      ?: toJvmQualifiedName().substringBeforeLast(TypeSeparator)
  }