@file:Suppress("SpellCheckingInspection")

/**
 * 一些类型名称规范转换
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/24 - 21:25
 */
package com.meowbase.toolkit


/**
 * // depending [separator]
 * Ljava/lang/Object; -> java.lang.Object
 *
 * // array only transform [separator]
 * Ljava.lang.Object; -> Ljava.lang.Object;
 *
 * I -> int, Z -> boolean, etc...
 */
fun CharSequence.toQualifiedClassType(separator: Char = '.'): String =
  toString().replace('.', separator).replace('/', separator).let {
    when {
      it[0] == 'L' -> it.substring(1, lastIndex)
      it[0] == '[' -> it
      else -> it
    }
  }.let {
    when (it) {
      "I" -> "int"
      "Z" -> "boolean"
      "C" -> "int"
      "D" -> "double"
      "F" -> "float"
      "J" -> "long"
      "S" -> "short"
      "B" -> "byte"
      "V" -> "void"
      else -> it
    }
  }

/**
 *  // depending [separator]
 * java.lang.Object -> Ljava/lang/Object;
 *
 * [Reference](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3)
 */
fun CharSequence.toClassTypeDescriptor(separator: Char = '/'): String = this.toString().run {
  when (this) {
    "I", "Z", "C", "D", "F", "J", "S", "B", "V" -> this
    else -> this
      .replace('.', separator)
      .replace('/', separator)
      .let {
        when {
          (startsWith('L') || startsWith("[L")) && endsWith(';') -> it
          else -> "L$it;"
        }
      }.let {
        when (it) {
          "int" -> "I"
          "boolean" -> "Z"
          "char" -> "C"
          "double" -> "D"
          "float" -> "F"
          "long" -> "J"
          "short" -> "S"
          "byte" -> "B"
          "void" -> "V"
          else -> it
        }
      }
  }
}