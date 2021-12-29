@file:Suppress("NO_ACTUAL_FOR_EXPECT", "NOTHING_TO_INLINE")

package com.meowool.sweekt

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

/**
 * Returns `true` if the instance class of this object is equal to the given [className].
 *
 * @author 凛 (RinOrz)
 */
expect inline infix fun Any.equalsClass(className: String): Boolean

/**
 * Returns `true` if the instance class of this object is equal to the given [KClass].
 *
 * @author 凛 (RinOrz)
 */
expect inline infix fun Any.equalsClass(KClass: KClass<*>): Boolean

/**
 * Returns `true` if the instance class of this object is equal to the specified class type of [T].
 *
 * @author 凛 (RinOrz)
 */
expect inline fun <reified T> Any.equalsClass(): Boolean

/**
 * Cast this object type to [T].
 *
 * @param T result type after conversion
 * @throws ClassCastException [T] type does not match with this object.
 * @author 凛 (RinOrz)
 */
@Throws(ClassCastException::class)
inline fun <reified T> Any?.cast(): T {
  contract {
    returns() implies (this@cast is T)
  }
  return this as? T ?: throw ClassCastException("$this cannot be cast to ${T::class}")
}

/**
 * Cast this object type to [T].
 *
 * @param T result type after conversion, returns `null` if the type does not match.
 * @author 凛 (RinOrz)
 */
inline fun <reified T> Any?.castOrNull(): T? {
  contract {
    returnsNotNull() implies (this@castOrNull is T)
    returns(null) implies (this@castOrNull !is T)
  }
  return this as? T
}

/**
 * Safe convert this object to [T] type.
 *
 * @param T result type after conversion, returns `null` if the type does not match.
 * @author 凛 (RinOrz)
 */
@Deprecated(
  "Deprecated function name, use `castOrNull` instead.",
  replaceWith = ReplaceWith("castOrNull()"),
  level = DeprecationLevel.ERROR
)
inline fun <reified T> Any?.safeCast(): T? = this@safeCast.castOrNull()

/**
 * When this object type is [T], call the [action].
 *
 * ```kotlin
 * val foo: Any = "abc"
 * val result = foo.whenType<String> {
 *   this.replace("c", "d")
 * }
 *
 * result -> "abd"
 * ```
 *
 * @return returns `null` if the object type is not [T], otherwise returns itself.
 * @author 凛 (RinOrz)
 */
inline fun <reified T> Any?.withType(action: T.() -> Unit): T? {
  contract {
    returnsNotNull() implies (this@withType is T)
    callsInPlace(action, InvocationKind.AT_MOST_ONCE)
  }
  return this.castOrNull<T>()?.apply(action)
}

/**
 * If this object type is [T], call the [action] and return its result.
 *
 * ```
 * val foo: Any = "abc"
 * val result: Boolean = foo.ifType<String> {
 *   this.replace("c", "d")
 *   true
 * }
 *
 * result -> true
 * ```
 *
 * @return returns `null` if the object type is not [T], otherwise returns the object by given [action] result.
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any, R> Any?.ifType(action: T.() -> R): R? {
  contract {
    callsInPlace(action, InvocationKind.AT_MOST_ONCE)
  }
  return this.castOrNull<T>()?.let(action)
}

/**
 * Returns `true` if the instance of this object is null.
 *
 * @author 凛 (RinOrz)
 */
inline fun Any?.isNull(): Boolean {
  contract {
    returns(true) implies (this@isNull == null)
  }
  return this == null
}

/**
 * Returns `true` if the instance of this object is not null.
 *
 * @author 凛 (RinOrz)
 */
inline fun Any?.isNotNull(): Boolean {
  contract {
    returns(true) implies (this@isNotNull != null)
  }
  return this != null
}