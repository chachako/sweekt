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

@file:Suppress("FunctionName")
@file:OptIn(ExperimentalTypeInference::class)

package com.meowbase.toolkit.iterations

import kotlin.experimental.ExperimentalTypeInference


inline fun <K, V, R> Map<out K, V>.flatMapNotNull(transform: (Map.Entry<K, V>) -> Iterable<R>?): List<R> {
  return flatMapNotNullTo(ArrayList(), transform)
}

@kotlin.jvm.JvmName("flatMapNotNullSequence")
@OverloadResolutionByLambdaReturnType
inline fun <K, V, R> Map<out K, V>.flatMap(transform: (Map.Entry<K, V>) -> Sequence<R>?): List<R> {
  return flatMapNotNullTo(ArrayList(), transform)
}

inline fun <K, V, R, C : MutableCollection<in R>> Map<out K, V>.flatMapNotNullTo(
  destination: C,
  transform: (Map.Entry<K, V>) -> Iterable<R>?
): C {
  for (element in this) {
    val list = transform(element)
    if (list != null) destination.addAll(list)
  }
  return destination
}

@kotlin.jvm.JvmName("flatMapNotNullSequenceTo")
@OverloadResolutionByLambdaReturnType
inline fun <K, V, R, C : MutableCollection<in R>> Map<out K, V>.flatMapNotNullTo(
  destination: C,
  transform: (Map.Entry<K, V>) -> Sequence<R>?
): C {
  for (element in this) {
    val list = transform(element)
    if (list != null) destination.addAll(list)
  }
  return destination
}

/**
 * 转换 Map<K, V> 为人类可读的字符串
 * ```
 * mapOf("one" to true, "two" to 2).joinToString(StringBuilder(infix = "="))
 * // { one=true, two=2 }
 * ```
 */
fun <K, V, A : Appendable> Map<K, V>.joinTo(
  buffer: A,
  infix: CharSequence = ": ",
  separator: CharSequence = ", ",
  prefix: CharSequence = "{ ",
  postfix: CharSequence = " }",
  limit: Int = -1,
  truncated: CharSequence = "...",
  transform: ((K, V) -> CharSequence)? = null
): A {
  buffer.append(prefix)
  var count = 0
  for (element in this) {
    if (++count > 1) buffer.append(separator)
    if (limit < 0 || count <= limit) {
      buffer._appendKV(infix, element.key, element.value, transform)
    } else break
  }
  if (limit in 0 until count) buffer.append(truncated)
  buffer.append(postfix)
  return buffer
}

/**
 * 转换 Map<K, V> 为人类可读的字符串
 * ```
 * mapOf("one" to true, "two" to 2).joinToString()
 * // { one: true, two: 2 }
 * ```
 */
fun <K, V> Map<K, V>.joinToString(
  separator: CharSequence = ", ",
  infix: CharSequence = ": ",
  prefix: CharSequence = "{ ",
  postfix: CharSequence = " }",
  limit: Int = -1,
  truncated: CharSequence = "...",
  transform: ((K, V) -> CharSequence)? = null
): String = joinTo(
  buffer = StringBuilder(),
  infix = infix,
  separator = separator,
  prefix = prefix,
  postfix = postfix,
  limit = limit,
  truncated = truncated,
  transform = transform
).toString()

private fun <K, V> Appendable._appendKV(
  infix: CharSequence,
  key: K,
  value: V,
  transform: ((K, V) -> CharSequence)?
) {
  fun Any?.get(): CharSequence? = when (this) {
    is CharSequence? -> this
    else -> this.toString()
  }
  when {
    transform != null -> append(transform(key, value))
    else -> append("${key.get()}$infix${value.get()}")
  }
}