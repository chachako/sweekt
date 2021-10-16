@file:OptIn(ExperimentalContracts::class)
@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.iteration

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName


/**
 * Returns the number of elements in the sequence.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline val <T> Sequence<T>.size: Int get() = this.count()

/**
 * Returns `true` if the sequence is empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>.isEmpty(): Boolean = this.none()

/**
 * Returns `true` if the sequence is not empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>.isNotEmpty(): Boolean = this.any()

/**
 * Returns `true` if this nullable sequence is either null or empty.
 *
 * The operation is _terminal_.
 *
 * @see isNotEmpty
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>?.isNullOrEmpty(): Boolean {
  contract {
    returns(false) implies (this@isNullOrEmpty != null)
  }

  return this == null || this.isEmpty()
}

/**
 * Returns `true` if this nullable sequence is either not `null` or not empty.
 *
 * The operation is _terminal_.
 *
 * @see isNullOrEmpty
 * @author 凛 (https://github.com/RinOrz)
 */
@JvmName("isNotNullNotEmpty")
inline fun <T> Sequence<T>?.isNotEmpty(): Boolean {
  contract {
    returns(true) implies (this@isNotEmpty != null)
  }

  return this?.isNotEmpty() == true
}

/**
 * Call the given [action] when this sequence is not `null` and not empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
@JvmName("onNotNullNotEmpty")
inline fun <T> Sequence<T>?.onNotEmpty(action: (Sequence<T>) -> Unit): Sequence<T>? {
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
 * Call the given [action] when this sequence is not empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>.onNotEmpty(action: (Sequence<T>) -> Unit): Sequence<T> {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isNotEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this sequence is null or empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>?.onNullOrEmpty(action: (Sequence<T>?) -> Unit): Sequence<T>? {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isNullOrEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this sequence is empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>.onEmpty(action: (Sequence<T>) -> Unit): Sequence<T> {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isEmpty()) {
    action(this)
  }
  return this
}

/**
 * Returns itself if this [Sequence] is not empty, otherwise null.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>?.takeIfNotEmpty(): Sequence<T>? {
  contract {
    returnsNotNull() implies (this@takeIfNotEmpty != null)
  }
  return if (isNullOrEmpty()) null else this
}

/**
 * Returns itself if this [Sequence] is empty, otherwise null.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>.takeIfEmpty(): Sequence<T>? = if (isEmpty()) this else null

/**
 * Returns `true` if this sequence starts with given [slice].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> Sequence<T>.startsWith(slice: Iterable<T>) = this.take(slice.size) == slice.asSequence()

/**
 * Returns `true` if this sequence ends with given [slice].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> Sequence<T>.endsWith(slice: Iterable<T>) = this.toList().takeLast(slice.size) == slice.asList()

/**
 * Returns `true` if this sequence starts with given [slice].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> Sequence<T>.startsWith(slice: Sequence<T>) = this.take(slice.size) == slice.asSequence()

/**
 * Returns `true` if this sequence ends with given [slice].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> Sequence<T>.endsWith(slice: Sequence<T>) = this.toList().takeLast(slice.size) == slice.toList()

/**
 * Returns `true` if this sequence starts with given [slice].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> Sequence<T>.startsWith(vararg slice: T) = this.take(slice.size) == slice.asSequence()

/**
 * Returns `true` if this sequence ends with given [slice].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <T> Sequence<T>.endsWith(vararg slice: T) = this.toList().takeLast(slice.size) == slice.asList()

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>.contains(predicate: (T) -> Boolean): Boolean = any(predicate)

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <T> Sequence<T>.has(predicate: (T) -> Boolean): Boolean = any(predicate)

/**
 * Converts [Sequence] to [Array].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <reified T> Sequence<T>.toArray(): Array<T> = this.toList().toTypedArray()