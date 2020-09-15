@file:Suppress("NAME_SHADOWING")

package com.mars.toolkit.iterations

import kotlin.collections.slice


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
    resultList.add(slice(
      start,
      // 如果结尾大于总数则应该不超过总数
      if (size < end) size else end
    ))
  }
  return resultList
}