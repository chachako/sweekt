package com.meowool.sweekt

import kotlin.reflect.KClass

/**
 * Returns the [KClass] of this object instance.
 *
 * @see Any.javaClass
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline val <T : Any> T.kotlinClass: KClass<T> get() = javaClass.kotlin

/**
 * Returns the class name of this object instance.
 */
inline val Any.className: String get() = javaClass.name