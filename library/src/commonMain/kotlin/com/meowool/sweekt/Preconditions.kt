@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ExperimentalContracts::class)

package com.meowool.sweekt

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * When the [key] is true, invoke the [action] and returns the given result [R], otherwise returns
 * the original receiver [T].
 */
inline fun <T : R, R> T.whenTrue(
  key: Boolean,
  action: (T) -> R,
): R = if (key) action(this) else this

/**
 * When the [key] is true, invoke the [action] and returns the given result [R], otherwise returns
 * the original receiver [T].
 */
inline fun <T : R, R> T.whenFalse(
  key: Boolean,
  action: (T) -> R,
): R = if (key.not()) action(this) else this

/**
 * When the [key] is true, invoke the [onTrue] and returns the given result [R], otherwise invoke
 * the [onFalse] and returns.
 */
inline fun <T, R> T.whenBool(
  key: Boolean,
  onTrue: (T) -> R,
  onFalse: (T) -> R,
): R = if (key) onTrue(this) else onFalse(this)

/**
 * If this [T] is not null then this is returned, otherwise [another] is executed and its result
 * is returned.
 */
inline fun <T> T?.ifNull(another: () -> T): T {
  contract { callsInPlace(another, InvocationKind.AT_MOST_ONCE) }
  return this ?: another()
}

/**
 * Select the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [true] value, otherwise returns the [false] value.
 */
inline fun <R> Boolean?.select(`true`: R, `false`: R): R = if (this == true) `true` else `false`

/**
 * Select the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [true] value, otherwise returns the [false] value.
 */
inline fun <R> Boolean?.select(`true`: () -> R, `false`: () -> R): R =
  if (this == true) `true`() else `false`()

/**
 * Imitating the ternary operator of java's, when this [Boolean] value is `true`, it returns [default],
 * otherwise it returns `null`.
 *
 * For example `data.isNull() that "no data" ?: "data exists"`, when the `data` is null, returns "no data",
 * otherwise returns "data exists"`.
 */
inline infix fun <R> Boolean?.then(default: R?): R? = if (this == true) default else null