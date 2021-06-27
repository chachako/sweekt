@file:OptIn(ExperimentalContracts::class)

package com.meowool.sweekt

import io.kotest.assertions.timing.EventuallyPredicate
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Return the value safely, if the process calling [trying] throws an exception, call [catching] and return its result.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <R> safetyValue(trying: () -> R, catching: () -> R): R {
  contract {
    callsInPlace(trying, InvocationKind.EXACTLY_ONCE)
    callsInPlace(catching, InvocationKind.AT_MOST_ONCE)
  }
  return try {
    trying()
  } catch (e: Throwable) {
    catching()
  }
}

/**
 * Return the value safely, if the process calling [trying] throws an exception, return the `null`.
 */
inline fun <R> safetyValue(trying: () -> R): R? {
  contract { callsInPlace(trying, InvocationKind.EXACTLY_ONCE) }
  return try {
    trying()
  } catch (e: Throwable) {
    null
  }
}

/**
 * If the given [predicate] is `true`, throw [throwable].
 */
inline fun throwIf(predicate: Boolean, throwable: () -> Throwable) {
  if (predicate) throw throwable()
}

/**
 * If the given [value] is `null`, throw [throwable].
 */
inline fun throwIfNull(value: Any?, throwable: () -> Throwable) {
  if (value == null) throw throwable()
}
