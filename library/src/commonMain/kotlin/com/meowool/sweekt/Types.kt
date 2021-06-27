@file:OptIn(ExperimentalContracts::class)
@file:Suppress("NO_ACTUAL_FOR_EXPECT", "NOTHING_TO_INLINE")

package com.meowool.sweekt

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

/**
 * Returns `true` if the instance class of this object is equal to the given [className].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
expect inline infix fun Any.equalsClass(className: String): Boolean

/**
 * Returns `true` if the instance class of this object is equal to the given [KClass].
 */
expect inline infix fun Any.equalsClass(KClass: KClass<*>): Boolean

/**
 * Returns `true` if the instance class of this object is equal to the specified class type of [T].
 */
expect inline fun <reified T> Any.equalsClass(): Boolean

/**
 * Cast this object type.
 *
 * @param T result type after conversion
 *
 * @throws ClassCastException [T] type does not match with this object.
 */
@Throws(ClassCastException::class)
inline fun <reified T> Any?.cast(): T {
  contract {
    returns() implies (this@cast is T)
  }
  return this as T
}

/**
 * Safe convert this object to [T] type.
 *
 * @param T result type after conversion, returns `null` if the type does not matches.
 */
inline fun <reified T> Any?.safeCast(): T? {
  contract {
    returnsNotNull() implies (this@safeCast is T)
    returns(null) implies (this@safeCast !is T)
  }
  return this as? T
}

/**
 * When the this object type is [T], call the [action].
 *
 * @return returns `null` if the object type is not [T], otherwise returns itself.
 */
inline fun <reified T> Any?.withType(action: T.() -> Unit): T? {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  return this.safeCast<T>()?.apply(action)
}

/**
 * When the this object type meets [expected], call the [action].
 *
 * ```kotlin
 * val foo: Any = "abc"
 * val result = foo.whenType(String::class) {
 *   this.replace("c", "d")
 * }
 *
 * result -> "abd"
 * ```
 *
 * @return returns `null` if the object type is not [T], otherwise returns the object by
 * given [action] result.
 */
@Suppress("UNUSED_PARAMETER")
inline fun <reified T: Any, R> Any?.withType(expected: KClass<T>, action: T.() -> R): R? {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  return this.safeCast<T>()?.let(action)
}

/**
 * Returns `true` if the instance of this object is null.
 */
inline fun Any?.isNull(): Boolean {
  contract {
    returns(true) implies(this@isNull == null)
  }
  return this == null
}

/**
 * Returns `true` if the instance of this object is not null.
 */
inline fun Any?.isNotNull(): Boolean {
  contract {
    returns(true) implies(this@isNotNull != null)
  }
  return this != null
}