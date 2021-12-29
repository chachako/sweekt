@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


/**
 * If this boolean value is `true`, returns the result of [block] execution, otherwise returns `null`.
 *
 * ```
 * If (isSucceed) {
 *   result()
 * } else {
 *   retry()
 * }
 *
 * ----
 *
 * isSucceed.ifTrue { result() } ?: retry()
 * ```
 *
 * @author 凛 (RinOrz)
 */
inline fun <R> Boolean?.ifTrue(block: () -> R): R? {
  contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
  return if (this == true) block() else null
}

/**
 * If this boolean value is `false`, returns the result of [block] execution, otherwise returns `null`.
 *
 * ```
 * If (isFailed) {
 *   retry()
 * } else {
 *   result()
 * }
 *
 * ----
 *
 * isFailed.ifTrue { retry() } ?: result()
 * ```
 *
 * @author 凛 (RinOrz)
 */
inline fun <R> Boolean?.ifFalse(block: () -> R): R? {
  contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
  return if (this == false) block() else null
}

/**
 * If this boolean value is `true`, returns the result of [block] execution, otherwise returns `null`.
 *
 * ```
 * If (isSucceed) {
 *   result()
 * } else {
 *   retry()
 * }
 *
 * ----
 *
 * isSucceed.ifTrue { result() } ?: retry()
 * ```
 *
 * @author 凛 (RinOrz)
 */
inline fun <R> Boolean.ifTrue(block: () -> R): R? = if (this) block() else null

/**
 * If this boolean value is `false`, returns the result of [block] execution, otherwise returns `null`.
 *
 * ```
 * If (isFailed) {
 *   retry()
 * } else {
 *   result()
 * }
 *
 * ----
 *
 * isFailed.ifTrue { retry() } ?: result()
 * ```
 *
 * @author 凛 (RinOrz)
 */
inline fun <R> Boolean.ifFalse(block: () -> R): R? = if (!this) block() else null

/**
 * Select the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [yes] value, otherwise returns the [no] value.
 * @author 凛 (RinOrz)
 */
inline fun <R> Boolean?.select(yes: R, no: R): R = if (this == true) yes else no

/**
 * Selects the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [yes] value, otherwise returns the [no] value.
 * @author 凛 (RinOrz)
 */
inline fun <R> Boolean.select(yes: R, no: R): R = if (this) yes else no

/**
 * Select the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [yes] value, otherwise returns the [no] value.
 * @author 凛 (RinOrz)
 */
inline fun <R> Boolean?.select(yes: () -> R, no: () -> R): R = if (this == true) yes() else no()

/**
 * Selects the given value based on this boolean value.
 *
 * @return if this boolean is `true`, returns the [yes] value, otherwise returns the [no] value.
 * @author 凛 (RinOrz)
 */
inline fun <R> Boolean.select(yes: () -> R, no: () -> R): R = if (this) yes() else no()