package com.meowool.sweekt

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Return the value safely, if the process calling [trying] throws an exception, call [catching] and return its result.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
@Deprecated(
  "Deprecated function name, use `run` instead.",
  replaceWith = ReplaceWith("run(catching = {}) {}", "com.meowool.sweekt.run"),
  level = DeprecationLevel.ERROR
)
inline fun <R> safetyValue(trying: () -> R, catching: () -> R): R = run({ catching() }, trying)

/**
 * Return the value safely, if the process calling [trying] throws an exception, return the `null`.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
@Deprecated(
  "Deprecated function name, use `runOrNull` instead.",
  replaceWith = ReplaceWith("runOrNull {}", "com.meowool.sweekt.runOrNull"),
  level = DeprecationLevel.ERROR
)
inline fun <R> safetyValue(trying: () -> R): R? = runOrNull(trying)

/**
 * If the given [predicate] is `true`, throw [throwable].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun throwIf(predicate: Boolean, throwable: () -> Throwable) {
  contract {
    callsInPlace(throwable, InvocationKind.AT_MOST_ONCE)
    returns() implies !predicate
  }
  if (predicate) throw throwable()
}

/**
 * If the given [value] is `null`, throw [throwable].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun throwIfNull(value: Any?, throwable: () -> Throwable) {
  contract {
    callsInPlace(throwable, InvocationKind.AT_MOST_ONCE)
    returns() implies (value != null)
  }
  if (value == null) throw throwable()
}

/**
 * If the given [value] is not `null`, throw [throwable].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun throwIfNotNull(value: Any?, throwable: () -> Throwable) {
  contract {
    callsInPlace(throwable, InvocationKind.AT_MOST_ONCE)
    returns() implies (value != null)
  }
  if (value == null) throw throwable()
}
