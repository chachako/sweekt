@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import kotlin.contracts.ExperimentalContracts
import kotlin.reflect.KClass


/**
 * Returns `true` if the instance class of this object is equal to the given [className].
 *
 * @author 凛 (RinOrz)
 */
actual inline infix fun Any.equalsClass(className: String): Boolean = this.javaClass.name == className

/**
 * Returns `true` if the instance class of this object is equal to the given [javaClass].
 *
 * @author 凛 (RinOrz)
 */
inline infix fun Any.equalsClass(javaClass: Class<*>): Boolean = this.javaClass == javaClass

/**
 * Returns `true` if the instance class of this object is equal to the given [KClass].
 *
 * @author 凛 (RinOrz)
 */
actual inline infix fun Any.equalsClass(KClass: KClass<*>): Boolean = this.kotlinClass == KClass

/**
 * Returns `true` if the instance class of this object is equal to the specified class type of [T].
 *
 * @author 凛 (RinOrz)
 */
actual inline fun <reified T> Any.equalsClass(): Boolean = this.kotlinClass == T::class