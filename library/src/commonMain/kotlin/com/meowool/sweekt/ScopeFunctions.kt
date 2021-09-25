package com.meowool.sweekt

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Calls the specified function [block] and returns its result. If an exception occurs during calling, calls the
 * specified function [catching] to catch exception and returns the result.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.run
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <R> run(catching: (Throwable) -> R, block: () -> R): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    block()
  } catch (e: Throwable) {
    catching(e)
  }
}

/**
 * Calls the specified function [block] and returns its result. If an exception occurs during calling, calls the
 * specified function [catching] to catch exception and returns the result.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.run
 */
inline fun <T, R> T.run(catching: T.(Throwable) -> R, block: T.() -> R): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    block()
  } catch (e: Throwable) {
    catching(e)
  }
}

/**
 * Calls the specified function [block] and returns its result. If an exception occurs during calling, `null` will
 * be returned and the exception will not be thrown.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.run
 */
inline fun <R> runOrNull(block: () -> R): R? {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    block()
  } catch (e: Throwable) {
    null
  }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its result. If an exception
 * occurs during calling, `null` will be returned and the exception will not be thrown.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.run
 */
inline fun <T, R> T.runOrNull(block: T.() -> R): R? {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    block()
  } catch (e: Throwable) {
    null
  }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns `this` value. If an exception
 * occurs during calling, calls the specified function [catching] to catch exception.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.apply
 */
inline fun <T, R> T.apply(catching: T.(Throwable) -> Unit, block: T.() -> Unit): T {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  try {
    block()
  } catch (e: Throwable) {
    catching(e)
  }
  return this
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns `this` value. If an exception
 * occurs during calling, `null` will be returned and the exception will not be thrown.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.apply
 */
inline fun <T> T.applyOrNull(block: T.() -> Unit): T? {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    block()
    this
  } catch (e: Throwable) {
    null
  }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns `this` value. If an exception
 * occurs during calling, calls the specified function [catching] to catch exception.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.also
 */
inline fun <T, R> T.also(catching: (receiver: T, t: Throwable) -> Unit, block: (receiver: T) -> Unit): T {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  try {
    block(this)
  } catch (e: Throwable) {
    catching(this, e)
  }
  return this
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns `this` value. If an exception
 * occurs during calling, `null` will be returned and the exception will not be thrown.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.apply
 */
inline fun <T> T.alsoOrNull(block: (receiver: T) -> Unit): T? {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    block(this)
    this
  } catch (e: Throwable) {
    null
  }
}

/**
 * Calls the specified function [block] with `this` value as its argument and returns its result. If an exception
 * occurs during calling, calls the specified function [catching] to catch exception and returns the result.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.let
 */
inline fun <T, R> T.let(catching: (receiver: T, t: Throwable) -> R, block: (receiver: T) -> R): R {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    block(this)
  } catch (e: Throwable) {
    catching(this, e)
  }
}

/**
 * Calls the specified function [block] with `this` value as its argument and returns its result. If an exception
 * occurs during calling, `null` will be returned and the exception will not be thrown.
 *
 * This is a lighter implementation than [runCatching].
 *
 * @see kotlin.let
 */
inline fun <T, R> T.letOrNull(block: (receiver: T) -> R): R? {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    block(this)
  } catch (e: Throwable) {
    null
  }
}


/**
 * If the result of the given [predicate] call is `false` or an exception is thrown during the [predicate] calls, then
 * returns `null`, otherwise returns `this`.
 *
 * ```
 * 10.takeTryIf { it < 100 }               ->    10
 * 10.takeTryIf { it > 10 }                ->    null
 * 10.takeTryIf { throw Exception(..) }    ->    null
 * ```
 *
 * @see kotlin.takeIf
 */
inline fun <T> T.takeTryIf(predicate: (T) -> Boolean): T? {
  contract {
    callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
  }
  return if (predicate(this)) this else null
}

/**
 * If the result of the given [predicate] call is `true` or an exception is thrown during the [predicate] calls, then
 * returns `null`, otherwise returns `this`.
 *
 * ```
 * 10.takeTryUnless { it < 100 }               ->    null
 * 10.takeTryUnless { it > 10 }                ->    10
 * 10.takeTryUnless { throw Exception(..) }    ->    null
 * ```
 *
 * @see kotlin.takeUnless
 */
inline fun <T> T.takeTryUnless(predicate: (T) -> Boolean): T? {
  contract {
    callsInPlace(predicate, InvocationKind.EXACTLY_ONCE)
  }
  return try {
    if (!predicate(this)) this else null
  } catch (e: Throwable) {
    null
  }
}

/**
 * If the result of the given [predicate] call is `true` or an exception is thrown during the [predicate] calls, then
 * returns `null`, otherwise returns `this`.
 *
 * @see takeTryUnless
 */
inline fun <T> T.takeTryIfNot(predicate: (T) -> Boolean): T? = takeTryUnless(predicate)

/**
 * Returns `this` value if it _does not_ satisfy the given [predicate] or `null`, if it does.
 *
 * @see kotlin.takeUnless
 */
inline fun <T> T.takeIfNot(predicate: (T) -> Boolean): T? = takeUnless(predicate)