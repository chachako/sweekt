@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ExperimentalContracts::class)

package com.meowool.sweekt

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


/**
 * If this [T] is not null then this is returned, otherwise [another] is executed and its result is returned.
 */
inline fun <T> T?.ifNull(another: () -> T): T {
  contract { callsInPlace(another, InvocationKind.AT_MOST_ONCE) }
  return this ?: another()
}

/**
 * When this [T] is `null`, calls the given [action], otherwise it does nothing and returns to itself.
 */
inline fun <T> T?.onNull(action: () -> T): T {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  return this ?: action()
}

/**
 * Select the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [yes] value, otherwise returns the [no] value.
 */
inline fun <R> Boolean?.select(yes: R, no: R): R = if (this == true) yes else no

/**
 * Selects the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [yes] value, otherwise returns the [no] value.
 */
inline fun <R> Boolean.select(yes: R, no: R): R = if (this) yes else no

/**
 * Select the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [yes] value, otherwise returns the [no] value.
 */
inline fun <R> Boolean?.select(yes: () -> R, no: () -> R): R = if (this == true) yes() else no()

/**
 * Selects the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [yes] value, otherwise returns the [no] value.
 */
inline fun <R> Boolean.select(yes: () -> R, no: () -> R): R = if (this) yes() else no()