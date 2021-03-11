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

package com.meowbase.toolkit.iterations


/**
 * 如果两个序列的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个序列 (left)
 * @param right 另一个序列
 * @param compareSize 比较两个序列的大小
 */
inline fun <Left, Right> Iterable<Left>.compareTo(
  right: Iterable<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean {
  val leftList = this.toList()
  val rightList = right.toList()
  if (compareSize && leftList.size != rightList.size) return false
  leftList.forEachIndexed { index, left ->
    val matches = predicate(left, rightList[index])
    if (!matches) return false
  }
  return true
}

/**
 * 如果两个序列的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个序列 (left)
 * @param right 另一个序列
 * @param compareSize 比较两个序列的大小
 */
inline fun <Left, Right> Iterable<Left>.compareTo(
  right: Sequence<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean = this.compareTo(right.toList(), compareSize, predicate)

/**
 * 如果两个序列/数组的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个序列 (left)
 * @param right 另一个数组
 * @param compareSize 比较两个序列/数组的大小
 */
inline fun <Left, Right> Iterable<Left>.compareTo(
  right: Array<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean = this.compareTo(right.toList(), compareSize, predicate)

/**
 * 如果两个序列的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个序列 (left)
 * @param right 另一个序列
 * @param compareSize 比较两个序列的大小
 */
inline fun <Left, Right> Sequence<Left>.compareTo(
  right: Iterable<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean = this.toList().compareTo(right, compareSize, predicate)

/**
 * 如果两个序列的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个序列 (left)
 * @param right 另一个序列
 * @param compareSize 比较两个序列的大小
 */
inline fun <Left, Right> Sequence<Left>.compareTo(
  right: Sequence<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean = this.compareTo(right.toList(), compareSize, predicate)

/**
 * 如果两个序列/数组的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个序列 (left)
 * @param right 另一个数组
 * @param compareSize 比较两个序列/数组的大小
 */
inline fun <Left, Right> Sequence<Left>.compareTo(
  right: Array<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean = this.compareTo(right.toList(), compareSize, predicate)

/**
 * 如果两个数组的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个数组 (left)
 * @param right 另一个数组
 * @param compareSize 比较两个数组的大小
 */
inline fun <Left, Right> Array<Left>.compareTo(
  right: Array<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean {
  if (compareSize && this.size != right.size) return false
  this.forEachIndexed { index, left ->
    val matches = predicate(left, right[index])
    if (!matches) return false
  }
  return true
}

/**
 * 如果两个数组/序列的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个数组 (left)
 * @param right 另一个序列
 * @param compareSize 比较两个数组/序列的大小
 */
inline fun <Left, Right> Array<Left>.compareTo(
  right: Iterable<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean {
  val rightList = right.toList()
  if (compareSize && this.size != rightList.size) return false
  this.forEachIndexed { index, left ->
    val matches = predicate(left, rightList[index])
    if (!matches) return false
  }
  return true
}

/**
 * 如果两个数组的所有元素都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个数组 (left)
 * @param right 另一个序列
 * @param compareSize 比较两个数组/序列的大小
 */
inline fun <Left, Right> Array<Left>.compareTo(
  right: Sequence<Right>,
  compareSize: Boolean = true,
  predicate: (left: Left, right: Right) -> Boolean
): Boolean = this.compareTo(right.toList(), compareSize, predicate)

/**
 * 如果两个 [Map] 的所有条目都与给定的 [predicate] 匹配，则返回 true
 *
 * @receiver 这个 [Map] (left)
 * @param right 另一个 [Map]
 * @param compareSize 比较两个 [Map] 的大小
 */
inline fun <LK, LV, RK, RV> Map<out LK, LV>.compareTo(
  right: Map<out RK, RV>,
  compareSize: Boolean = true,
  predicate: (left: Map.Entry<LK, LV>, right: Map.Entry<RK, RV>) -> Boolean
): Boolean {
  if (compareSize && this.size != right.size) return false
  var index = 0
  val rightList = right.entries.toList()
  forEach { left ->
    val matches = predicate(left, rightList[index])
    if (!matches) return false
    index++
  }
  return true
}