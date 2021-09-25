@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ExperimentalContracts::class)

package com.meowool.sweekt.iteration

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Returns the number of elements in the iterable.
 */
inline val <T> Iterable<T>.size: Int get() = this.count()

/**
 * Returns `true` if the iterable instance is empty.
 */
inline fun <T, I : Iterable<T>> I.isEmpty(): Boolean = this.none()

/**
 * Returns `true` if the iterable instance is null or empty.
 */
inline fun <T, I : Iterable<T>> I?.isNullOrEmpty(): Boolean = this == null || this.none()

/**
 * Returns `true` if the iterable instance is not empty.
 */
inline fun <T, I : Iterable<T>> I.isNotEmpty(): Boolean = this.any()

/**
 * Returns `true` if the iterable instance is not null and not empty.
 */
inline fun <T, I : Iterable<T>> I?.isNotNullEmpty(): Boolean {
  contract {
    returns(true) implies (this@isNotNullEmpty != null)
  }
  return this != null && this.isNotEmpty()
}

/**
 * Call the given [action] when this iterable instance is not null and not empty.
 */
inline fun <T, I : Iterable<T>> I?.onNotNullEmpty(action: (I) -> Unit): I? {
  contract {
    returnsNotNull() implies (this@onNotNullEmpty != null)
    callsInPlace(action, InvocationKind.AT_MOST_ONCE)
  }
  if (this.isNotNullEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this iterable instance is not empty.
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
 */
inline fun <T, I : Iterable<T>> I?.takeIfNotEmpty(): I? {
  contract {
    returnsNotNull() implies (this@takeIfNotEmpty != null)
  }
  return if (isNullOrEmpty()) null else this
}

/**
 * Returns itself if this [Iterable] is empty, otherwise null.
 */
inline fun <T, I : Iterable<T>> I.takeIfEmpty(): I? = if (isEmpty()) this else null

/**
 * Returns `true` if this iterable starts with given [slice].
 */
fun <T> Iterable<T>.startsWith(slice: T) = this.first() == slice

/**
 * Returns `true` if this iterable ends with given [slice].
 */
fun <T> Iterable<T>.endsWith(slice: T) = this.last() == slice

/**
 * Returns `true` if this iterable starts with given [slice].
 */
fun <T> Iterable<T>.startsWith(slice: Iterable<T>) = this.take(slice.size) == slice.asList()

/**
 * Returns `true` if this iterable ends with given [slice].
 */
fun <T> Iterable<T>.endsWith(slice: Iterable<T>) = this.toList().takeLast(slice.size) == slice.asList()

/**
 * Returns `true` if this iterable starts with given [slice].
 */
fun <T> Iterable<T>.startsWith(slice: Sequence<T>) = this.take(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable ends with given [slice].
 */
fun <T> Iterable<T>.endsWith(slice: Sequence<T>) = this.toList().takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable starts with given [slice].
 */
fun <T> Iterable<T>.startsWith(vararg slice: T) = this.take(slice.size) == slice.toList()

/**
 * Returns `true` if this iterable ends with given [slice].
 */
fun <T> Iterable<T>.endsWith(vararg slice: T) = this.toList().takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if at least one element matches the given [predicate].
 */
inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean = any(predicate)

/**
 * Returns `true` if at least one element matches the given [predicate].
 */
inline fun <T> Iterable<T>.has(predicate: (T) -> Boolean): Boolean = any(predicate)

/**
 * Converts [Iterable] to [Array].
 */
inline fun <reified T> Iterable<T>.toArray(): Array<T> = this.asCollection().toTypedArray()

/**
 * Drops the first element of this [Iterable] and returns a new list.
 */
inline fun <T> Iterable<T>.dropFirst(): List<T> = drop(1)

/**
 * Returns a list containing all elements except first [n] elements.
 *
 * @throws IllegalArgumentException if [n] is negative.
 */
inline fun <T> Iterable<T>.dropFirst(n: Int): List<T> = drop(n)