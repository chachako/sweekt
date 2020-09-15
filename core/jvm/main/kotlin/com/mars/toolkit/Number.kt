@file:Suppress("NOTHING_TO_INLINE")

package com.mars.toolkit

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.math.roundToInt
import kotlin.math.roundToLong

inline val Number.int
  get() = when (this) {
    is Int -> this
    is Double -> roundToInt()
    is Float -> roundToInt()
    else -> toInt()
  }

inline val Number.float
  get() = when (this) {
    is Float -> this
    else -> toFloat()
  }

inline val Number.long
  get() = when (this) {
    is Long -> this
    is Double -> roundToLong()
    is Float -> roundToLong()
    else -> toLong()
  }

/**
 * Flag 位运算 (包含)
 * @return receiver 中是否存在 ? flag
 */
inline fun Int.hasFlags(mask: Int): Boolean = mask and this == mask
inline fun Long.hasFlags(mask: Long): Boolean = mask and this == mask
inline fun Short.hasFlags(mask: Short): Boolean = mask and this == mask
inline fun Byte.hasFlags(mask: Byte): Boolean = mask and this == mask

/**
 * Flag 位运算 (增加一个或多个)
 * @param mask 将此 flag 添加到 receiver 中
 */
inline fun Int.addFlags(mask: Int): Int = this or mask
inline fun Long.addFlags(mask: Long): Long = this or mask
inline fun Short.addFlags(mask: Short): Short = this or mask
inline fun Byte.addFlags(mask: Byte): Byte = this or mask

/**
 * Flag 位运算 (删除一个或多个)
 * @param mask 将此 flag 从 receiver 中删除
 */
inline fun Int.removeFlags(mask: Int): Int = this and mask.inv()
inline fun Long.removeFlags(mask: Long): Long = this and mask.inv()
inline fun Short.removeFlags(mask: Short): Short = this and mask.inv()
inline fun Byte.removeFlags(flag: Byte): Byte = this and flag.inv()