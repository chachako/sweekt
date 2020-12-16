package com.meowbase.toolkit.iterations

/**
 * 在 [Iterable] 开头插入数据
 * 可用于流式 Api 的扩展
 * ```
 * listOf(null, 1, 2, 3)
 *   .filter { it < 3 }
 *   .filterNotNull()
 *   .insertFirst { listOf(-1, -2) }
 *
 * // [-1, -2, 1, 2]
 * ```
 */
inline fun <T> Iterable<T>.insertFirst(insert: () -> Iterable<T>): List<T> = insert() + this

/**
 * 在 [Iterable] 结尾插入数据
 * 可用于流式 Api 的扩展
 * ```
 * listOf(null, 1, 2, 3)
 *   .filter { it > 2 }
 *   .filterNotNull()
 *   .insertLast { listOf(10, 10) }
 *
 * // [3, 10, 10]
 * ```
 */
inline fun <T> Iterable<T>.insertLast(insert: () -> Iterable<T>): List<T> = this + insert()

/**
 * 在 [Iterable] 开头插入数据
 * 可用于流式 Api 的扩展
 * ```
 * listOf(null, 1, 2, 3)
 *   .filter { it < 3 }
 *   .filterNotNull()
 *   .insertFirst(listOf(-1, -2))
 *
 * // [-1, -2, 1, 2]
 * ```
 */
fun <T> Iterable<T>.insertFirst(insert: Iterable<T>): List<T> = insert + this

/**
 * 在 [Iterable] 结尾插入数据
 * 可用于流式 Api 的扩展
 * ```
 * listOf(null, 1, 2, 3)
 *   .filter { it > 2 }
 *   .filterNotNull()
 *   .insertLast(listOf(10, 10))
 *
 * // [3, 10, 10]
 * ```
 */
fun <T> Iterable<T>.insertLast(insert: Iterable<T>): List<T> = this + insert


/**
 * 将 [insert] 数据扁平化处理后插入 [Iterable] 的开头中
 * 可用于流式 Api 的扩展
 * ```
 * listOf(null, 1, 2, 3)
 *   .filter { it < 3 }
 *   .filterNotNull()
 *   .flatInsertFirst { list(listOf(-1, -2), listOf(-3, -4)) }
 *
 * // [-1, -2, -3, -4, 1, 2]
 * ```
 */
inline fun <T> Iterable<T>.flatInsertFirst(insert: () -> Iterable<Iterable<T>>): List<T> =
  insert().flatten() + this

/**
 * 将 [insert] 数据扁平化处理后插入 [Iterable] 的结尾中
 * 可用于流式 Api 的扩展
 * ```
 * listOf(null, 1, 2, 3)
 *   .filter { it > 2 }
 *   .filterNotNull()
 *   .flatInsertLast { listOf(listOf(1, 10), listOf(2, 20)) }
 *
 * // [3, 1, 10, 2, 20]
 * ```
 */
inline fun <T> Iterable<T>.flatInsertLast(insert: () -> Iterable<Iterable<T>>): List<T> =
  this + insert().flatten()

/**
 * 将 [insert] 数据扁平化处理后插入 [Iterable] 的开头中
 * 可用于流式 Api 的扩展
 * ```
 * list(null, 1, 2, 3)
 *   .filter { it < 8 }
 *   .filterNotNull()
 *   .flatInsertLast(listOf(listOf(1, 10), listOf(2, 20)))
 *
 * // [-1, -2, -3, -4, 1, 2]
 * ```
 */
fun <T> Iterable<T>.flatInsertFirst(insert: Iterable<Iterable<T>>): List<T> =
  insert.flatten() + this

/**
 * 将 [insert] 数据扁平化处理后插入 [Iterable] 的结尾中
 * 可用于流式 Api 的扩展
 * ```
 * listOf(null, 1, 2, 3)
 *   .filter { it > 2 }
 *   .filterNotNull()
 *   .flatInsertLast(listOf(listOf(1, 10), listOf(2, 20)))
 *
 * // [3, 1, 10, 2, 20]
 * ```
 */
fun <T> Iterable<T>.flatInsertLast(insert: Iterable<Iterable<T>>): List<T> =
  this + insert.flatten()