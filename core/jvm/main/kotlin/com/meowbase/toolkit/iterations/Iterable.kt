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

@file:Suppress("NAME_SHADOWING")
@file:OptIn(ExperimentalTypeInference::class)

package com.meowbase.toolkit.iterations

import kotlin.collections.slice
import kotlin.experimental.ExperimentalTypeInference


inline fun <T, R> Iterable<T>.mapToSequence(crossinline transform: (T) -> R): Sequence<R> {
  val origin = this.iterator()
  return generateSequence {
    when {
      origin.hasNext() -> transform(origin.next())
      else -> null
    }
  }
}

inline fun <T, R> Iterable<T>.flatMapToSequence(
  crossinline transform: (T) -> Iterable<R>
): Sequence<R> = mapToSequence(transform).flatten()

inline fun <T, reified R> Iterable<T>.mapToArray(crossinline transform: (T) -> R): Array<R> {
  val origin = this.iterator()
  return Array(this.count()) {
    when {
      origin.hasNext() -> transform(origin.next())
      else -> throw ArrayIndexOutOfBoundsException(it)
    }
  }
}

inline fun <T, reified R> Iterable<T>.flatMapToArray(crossinline transform: (T) -> Iterable<R>): Array<R> =
  mapToArray(transform).flatten().toTypedArray()

inline fun <T, R> Iterable<T>.flatMapNotNull(transform: (T) -> Iterable<R>?): List<R> {
  return flatMapNotNullTo(ArrayList(), transform)
}

@kotlin.jvm.JvmName("flatMapNotNullSequence")
@OverloadResolutionByLambdaReturnType
inline fun <T, R> Iterable<T>.flatMapNotNull(transform: (T) -> Sequence<R>?): List<R> {
  return flatMapNotNullTo(ArrayList(), transform)
}

inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.flatMapNotNullTo(
  destination: C,
  transform: (T) -> Iterable<R>?
): C {
  for (element in this) {
    val list = transform(element)
    if (list != null) destination.addAll(list)
  }
  return destination
}

@kotlin.jvm.JvmName("flatMapNotNullSequenceTo")
@OverloadResolutionByLambdaReturnType
inline fun <T, R, C : MutableCollection<in R>> Iterable<T>.flatMapNotNullTo(
  destination: C,
  transform: (T) -> Sequence<R>?
): C {
  for (element in this) {
    val list = transform(element)
    if (list != null) destination.addAll(list)
  }
  return destination
}

/** 判断 [Iterable] 的开头是否包含另一个 [Iterable] */
fun <T> Iterable<T>.startsWith(prefix: Iterable<T>): Boolean =
  toList().run {
    val prefix = prefix.toList()
    size >= prefix.size && slice(0, prefix.size) == prefix
  }

/** 判断 [Iterable] 的结尾是否包含另一个 [Iterable] */
fun <T> Iterable<T>.endsWith(suffix: Iterable<T>): Boolean =
  toList().run {
    val suffix = suffix.toList()
    size >= suffix.size && slice(size - suffix.size, size) == suffix
  }

/** 替换所有数据为 [elements] */
fun <T> Iterable<T>.replaceAll(elements: Iterable<T>) = toMutableList().apply {
  clear()
  addAll(elements)
}

/**
 * 截取源列表的 [startIndex] 到 [endIndex], 并生成新的 [Collection]
 * @see List.subList
 */
fun <T> Iterable<T>.slice(startIndex: Int, endIndex: Int): List<T> =
  toList().slice(startIndex..endIndex)

/** [kotlin.collections.slice] */
fun <T> Iterable<T>.slice(indices: IntRange): List<T> =
  toList().slice(indices)

/** [kotlin.collections.slice] */
fun <T> Iterable<T>.slice(indices: Iterable<Int>): List<T> =
  toList().slice(indices)

/** Receiver [Iterable] 存在则返回扩展函数，否则直接返回 [other] */
infix fun <T> Iterable<T>?.or(other: Iterable<T>) =
  if (this != null && toList().isNotEmpty()) this else other

/**
 * 按数量 [count] 分组
 * 每个组都会存放到一个列表中
 * ```
 * listOf(1, 2, 3, 4, 5, 6).groupByQuantity(2)
 * // listOf([1, 2], [3, 4], [5, 6])
 * ```
 */
fun <T> Iterable<T>.groupByQuantity(count: Int): List<List<T>> = toList().run {
  val resultList = mutableListOf<List<T>>()
  val groupCount = size / count
  for (groupIndex in 0 until groupCount) {
    val start = groupIndex * count // 当前组数 * n（每一组有 n 个数据）
    val end = start + count // 开始到结束总共 n 个数据
    resultList.add(
      slice(
        start,
        // 如果结尾大于总数则应该不超过总数
        if (size < end) size else end
      )
    )
  }
  return resultList
}