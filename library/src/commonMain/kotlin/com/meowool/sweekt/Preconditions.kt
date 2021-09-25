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

/**
 * Imitating the ternary operator of java's, when this [Boolean] value is `true`, it returns [default],
 * otherwise it returns `null`.
 *
 * For example `data.isNull() that "no data" ?: "data exists"`, when the `data` is null, returns "no data",
 * otherwise returns "data exists"`.
 */
inline infix fun <R> Boolean?.that(default: R?): R? {
  contract {
    returnsNotNull() implies (this@that != null)
  }
  return if (this == true) default else null
}

/**
 * Imitating the ternary operator of java's, when this [Boolean] value is `true`, it returns [default],
 * otherwise it returns `null`.
 *
 * For example `data.isNull() that "no data" ?: "data exists"`, when the `data` is null, returns "no data",
 * otherwise returns "data exists"`.
 */
inline infix fun <R> Boolean.that(default: R?): R? = if (this) default else null