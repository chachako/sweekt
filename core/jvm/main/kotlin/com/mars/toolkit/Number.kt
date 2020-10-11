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


/**
 * Packs two Float values into one Long value for use in inline classes.
 */
inline fun packFloats(val1: Float, val2: Float): Long {
  val v1 = val1.toBits().toLong()
  val v2 = val2.toBits().toLong()
  return v1.shl(32) or (v2 and 0xFFFFFFFF)
}

/**
 * Unpacks the first Float value in [packFloats] from its returned Long.
 */
inline fun unpackFloat1(value: Long): Float {
  return Float.fromBits(value.shr(32).toInt())
}

/**
 * Unpacks the second Float value in [packFloats] from its returned Long.
 */
inline fun unpackFloat2(value: Long): Float {
  return Float.fromBits(value.and(0xFFFFFFFF).toInt())
}

/**
 * Packs two Int values into one Long value for use in inline classes.
 */
inline fun packInts(val1: Int, val2: Int): Long {
  return val1.toLong().shl(32) or (val2.toLong() and 0xFFFFFFFF)
}

/**
 * Unpacks the first Int value in [packInts] from its returned ULong.
 */
inline fun unpackInt1(value: Long): Int {
  return value.shr(32).toInt()
}

/**
 * Unpacks the second Int value in [packInts] from its returned ULong.
 */
inline fun unpackInt2(value: Long): Int {
  return value.and(0xFFFFFFFF).toInt()
}