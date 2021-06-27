@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ExperimentalContracts::class)

package com.meowool.sweekt.iteration

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Returns `true` if the array is not empty.
 */
inline fun <T> Array<T>?.isNotNullEmpty(): Boolean {
  contract {
    returns(true) implies (this@isNotNullEmpty != null)
  }
  return this != null && this.isNotEmpty()
}

/**
 * Call the given [action] when this array is not null and not empty.
 */
inline fun <T> Array<T>?.onNotNullEmpty(action: (Array<T>) -> Unit): Array<T>? {
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
 * Call the given [action] when this array is not empty.
 */
inline fun <T> Array<T>.onNotEmpty(action: (Array<T>) -> Unit): Array<T> {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isNotEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this array is null or empty.
 */
inline fun <T> Array<T>?.onNullOrEmpty(action: (Array<T>?) -> Unit): Array<T>? {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isNullOrEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this array is empty.
 */
inline fun <T> Array<T>.onEmpty(action: (Array<T>) -> Unit): Array<T> {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isEmpty()) {
    action(this)
  }
  return this
}

/**
 * Returns `true` if this array starts with given [slice].
 */
fun <T> Array<T>.startsWith(slice: Iterable<T>) = this.take(slice.size) == slice.asList()

/**
 * Returns `true` if this array ends with given [slice].
 */
fun <T> Array<T>.endsWith(slice: Iterable<T>) = this.takeLast(slice.size) == slice.asList()

/**
 * Returns `true` if this array starts with given [slice].
 */
fun <T> Array<T>.startsWith(slice: Sequence<T>) = this.take(slice.size) == slice.toList()

/**
 * Returns `true` if this array ends with given [slice].
 */
fun <T> Array<T>.endsWith(slice: Sequence<T>) = this.takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if this array starts with given [slice].
 */
fun <T> Array<T>.startsWith(vararg slice: T) = this.take(slice.size) == slice.toList()

/**
 * Returns `true` if this array ends with given [slice].
 */
fun <T> Array<T>.endsWith(vararg slice: T) = this.takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if at least one element matches the given [predicate].
 */
inline fun <T> Array<T>.contains(predicate: (T) -> Boolean): Boolean = any(predicate)

/**
 * Returns `true` if at least one element matches the given [predicate].
 */
inline fun <T> Array<T>.has(predicate: (T) -> Boolean): Boolean = any(predicate)