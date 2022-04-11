@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ExperimentalContracts::class)

package com.meowool.sweekt.iteration

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName

/**
 * Returns the number of elements in the iterable.
 *
 * @author 凛 (RinOrz)
 */
inline val <T> Iterable<T>.size: Int get() = this.count()

/**
 * Returns `true` if the iterable instance is empty.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, I : Iterable<T>> I.isEmpty(): Boolean = this.none()

/**
 * Returns `true` if the iterable instance is null or empty.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, I : Iterable<T>> I?.isNullOrEmpty(): Boolean = this == null || this.none()

/**
 * Returns `true` if the iterable instance is not empty.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, I : Iterable<T>> I.isNotEmpty(): Boolean = this.any()

/**
 * Returns `true` if the iterable instance is not `null` and not empty.
 *
 * @author 凛 (RinOrz)
 */
@JvmName("isNotNullNotEmpty")
inline fun <T, I : Iterable<T>> I?.isNotEmpty(): Boolean {
  contract {
    returns(true) implies (this@isNotEmpty != null)
  }
  return this != null && this.any()
}

/**
 * Call the given [action] when this iterable instance is not null and not empty.
 *
 * @author 凛 (RinOrz)
 */
@JvmName("onNotNullNotEmpty")
inline fun <T, I : Iterable<T>> I?.onNotEmpty(action: (I) -> Unit): I? {
  contract {
    returnsNotNull() implies (this@onNotEmpty != null)
    callsInPlace(action, InvocationKind.AT_MOST_ONCE)
  }
  if (this.isNotEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this iterable instance is not empty.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, I : Iterable<T>> I.onNotEmpty(action: (I) -> Unit): I {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isNotEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this iterable instance is null or empty.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, I : Iterable<T>> I?.onNullOrEmpty(action: (I?) -> Unit): I? {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isNullOrEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this iterable instance is empty.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, I : Iterable<T>> I.onEmpty(action: (I) -> Unit): I {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isEmpty()) {
    action(this)
  }
  return this
}

/**
 * Returns itself if this [Iterable] is not empty, otherwise null.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, I : Iterable<T>> I?.takeIfNotEmpty(): I? {
  contract {
    returnsNotNull() implies (this@takeIfNotEmpty != null)
  }
  return if (isNullOrEmpty()) null else this
}

/**
 * Returns itself if this [Iterable] is empty, otherwise null.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, I : Iterable<T>> I.takeIfEmpty(): I? = if (isEmpty()) this else null

/**
 * Returns `true` if this iterable starts with given [slice].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableStartsWith")
fun <T> Iterable<T>?.startsWith(slice: T): Boolean = this?.first() == slice

/**
 * Returns `true` if this iterable starts with given [slice].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Iterable<T>.startsWith(slice: T): Boolean = this.first() == slice

/**
 * Returns `true` if this iterable ends with given [slice].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableEndsWith")
fun <T> Iterable<T>?.endsWith(slice: T) = this?.last() == slice

/**
 * Returns `true` if this iterable ends with given [slice].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Iterable<T>.endsWith(slice: T) = this.last() == slice

/**
 * Returns `true` if this iterable starts with given [slice].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableStartsWith")
fun <T> Iterable<T>?.startsWith(slice: Iterable<T>) = this?.take(slice.size) == slice.asList()

/**
 * Returns `true` if this iterable starts with given [slice].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Iterable<T>.startsWith(slice: Iterable<T>) = this.take(slice.size) == slice.asList()

/**
 * Returns `true` if this iterable ends with given [slice].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableEndsWith")
fun <T> Iterable<T>?.endsWith(slice: Iterable<T>) = this?.asList()?.takeLast(slice.size) == slice.asList()

/**
 * Returns `true` if this iterable ends with given [slice].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Iterable<T>.endsWith(slice: Iterable<T>) = this.asList().takeLast(slice.size) == slice.asList()

/**
 * Returns `true` if this iterable starts with given [slice].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableStartsWith")
fun <T> Iterable<T>?.startsWith(slice: Sequence<T>) = this?.take(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable starts with given [slice].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Iterable<T>.startsWith(slice: Sequence<T>) = this.take(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable ends with given [slice].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableEndsWith")
fun <T> Iterable<T>?.endsWith(slice: Sequence<T>) = this?.asList()?.takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable ends with given [slice].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Iterable<T>.endsWith(slice: Sequence<T>) = this.asList().takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable starts with given [slice].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableStartsWith")
fun <T> Iterable<T>?.startsWith(vararg slice: T) = this?.take(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable starts with given [slice].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Iterable<T>.startsWith(vararg slice: T) = this.take(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable ends with given [slice].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableEndsWith")
fun <T> Iterable<T>?.endsWith(vararg slice: T) = this?.asList()?.takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable ends with given [slice].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Iterable<T>.endsWith(vararg slice: T) = this.asList().takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableContains")
inline fun <T> Iterable<T>?.contains(predicate: (T) -> Boolean): Boolean = this != null && any(predicate)

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean = any(predicate)

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("nullableIterableHas")
inline fun <T> Iterable<T>?.has(predicate: (T) -> Boolean): Boolean = this != null && any(predicate)

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.has(predicate: (T) -> Boolean): Boolean = any(predicate)

/**
 * Converts [Iterable] to [Array].
 *
 * @author 凛 (RinOrz)
 */
inline fun <reified T> Iterable<T>.toArray(): Array<T> = this.asCollection().toTypedArray()

/**
 * Drops the first element of this [Iterable] and returns a new list.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.dropFirst(): List<T> = drop(1)

/**
 * Returns a list containing all elements except first [n] elements.
 *
 * @throws IllegalArgumentException if [n] is negative.
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.dropFirst(n: Int): List<T> = drop(n)

/**
 * Returns a new list which filters out all duplicate items from this iterate object.
 *
 * ## Examples
 *
 * ```
 * val list = listOf("orange", "apple", "apple", "banana", "water", "bread", "banana")
 *
 * assert(list.filterDuplicates() == listOf("apple", "banana"))
 * ```
 *
 * @author RinOrz
 */
fun <T> Iterable<T>.filterDuplicates(): List<T> = filterDuplicatesBy { it }

/**
 * Returns a new list which filters out all duplicate items from this iterate object.
 *
 * ## Examples
 *
 * ```
 * val list = listOf("orange;", "orange", "apple", "banana", "banana")
 *
 * assert(list.filterDuplicatesBy { it.removeSuffix(';') } == listOf("orange", "banana"))
 * ```
 *
 * @author RinOrz
 */
inline fun <T, K> Iterable<T>.filterDuplicatesBy(selector: (T) -> K): List<T> {
  val set = HashSet<K>()
  val list = ArrayList<T>()
  for (item in this) {
    val key = selector(item)
    if (!set.add(key)) list.add(item)
  }
  return list
}
