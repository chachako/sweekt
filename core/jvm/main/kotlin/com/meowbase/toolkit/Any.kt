@file:Suppress("NOTHING_TO_INLINE")

package com.meowbase.toolkit

import kotlin.reflect.KClass


/**
 * 获得实例的 [KClass]
 * @see javaClass
 */
inline val <T : Any> T.kotlinClass: KClass<T> get() = javaClass.kotlin

/** 获得实例的类名 */
inline val Any.className: String get() = javaClass.name

/** 判断实例的类名是否为 [comparedClassName] */
infix fun Any.typeIs(comparedClassName: String): Boolean = javaClass.name == comparedClassName

/** 判断实例的类型 [Class] 是否为 [comparedClass] */
infix fun Any.typeIs(comparedClass: Class<*>): Boolean = javaClass == comparedClass

/** 判断实例的类型 [KClass] 是否为 [comparedClass] */
infix fun Any.typeIs(comparedClass: KClass<*>): Boolean = javaClass.kotlin == comparedClass

/** 判断实例的类型是否为 [T] */
inline fun <reified T> Any.typeIs(): Boolean = javaClass.name == T::class.java.name

/** 判断实例是否为 null */
inline fun Any?.isNull(): Boolean = this == null

/** 判断实例是否不为 null */
inline fun Any?.isNotNull(): Boolean = this != null