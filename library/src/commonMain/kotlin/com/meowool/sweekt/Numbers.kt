@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Returns this [Number] as [Int].
 *
 * @author 凛 (RinOrz)
 */
inline fun Number.asInt(): Int = when (this) {
  is Int -> this
  is Double -> roundToInt()
  is Float -> roundToInt()
  else -> toInt()
}

/**
 * Returns this [Number] as [Float].
 *
 * @author 凛 (RinOrz)
 */
inline fun Number.asFloat(): Float = when (this) {
  is Float -> this
  else -> toFloat()
}

/**
 * Returns this [Number] as [Long].
 *
 * @author 凛 (RinOrz)
 */
inline fun Number.asLong(): Long = when (this) {
  is Long -> this
  is Double -> roundToLong()
  is Float -> roundToLong()
  else -> toLong()
}

/**
 * Set the given [mask] to this [Int] bit flags.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Int.addFlag(mask: Int): Int = this or mask

/**
 * Returns `true` if this [Int] bit flags contains the given [mask].
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Int.hasFlag(mask: Int): Boolean = mask and this == mask

/**
 * Remove the given [mask] from [Int] this bit flag.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Int.removeFlag(mask: Int): Int = this and mask.inv()

/**
 * Set the given [mask] to this [Long] bit flags.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Long.addFlag(mask: Long): Long = this or mask

/**
 * Returns `true` if this [Long] bit flags contains the given [mask].
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Long.hasFlag(mask: Long): Boolean = mask and this == mask

/**
 * Remove the given [mask] from [Long] this bit flag.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Long.removeFlag(mask: Long): Long = this and mask.inv()

/**
 * Set the given [mask] to this [Short] bit flags.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Short.addFlag(mask: Short): Short = this or mask

/**
 * Returns `true` if this [Short] bit flags contains the given [mask].
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Short.hasFlag(mask: Short): Boolean = mask and this == mask

/**
 * Remove the given [mask] from [Short] this bit flag.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Short.removeFlag(mask: Short): Short = this and mask.inv()

/**
 * Set the given [mask] to this [Byte] bit flags.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Byte.addFlag(mask: Byte): Byte = this or mask

/**
 * Returns `true` if this [Byte] bit flags contains the given [mask].
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Byte.hasFlag(mask: Byte): Boolean = mask and this == mask

/**
 * Remove the given [mask] from [Byte] this bit flag.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun Byte.removeFlag(mask: Byte): Byte = this and mask.inv()

/**
 * Set the given [mask] to this [UInt] bit flags.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UInt.addFlag(mask: UInt): UInt = this or mask

/**
 * Returns `true` if this [UInt] bit flags contains the given [mask].
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UInt.hasFlag(mask: UInt): Boolean = mask and this == mask

/**
 * Remove the given [mask] from [UInt] this bit flag.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UInt.removeFlag(mask: UInt): UInt = this and mask.inv()

/**
 * Set the given [mask] to this [ULong] bit flags.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun ULong.addFlag(mask: ULong): ULong = this or mask

/**
 * Returns `true` if this [ULong] bit flags contains the given [mask].
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun ULong.hasFlag(mask: ULong): Boolean = mask and this == mask

/**
 * Remove the given [mask] from [ULong] this bit flag.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun ULong.removeFlag(mask: ULong): ULong = this and mask.inv()

/**
 * Set the given [mask] to this [UShort] bit flags.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UShort.addFlag(mask: UShort): UShort = this or mask

/**
 * Returns `true` if this [UShort] bit flags contains the given [mask].
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UShort.hasFlag(mask: UShort): Boolean = mask and this == mask

/**
 * Remove the given [mask] from [UShort] this bit flag.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UShort.removeFlag(mask: UShort): UShort = this and mask.inv()

/**
 * Set the given [mask] to this [UByte] bit flags.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UByte.addFlag(mask: UByte): UByte = this or mask

/**
 * Returns `true` if this [UByte] bit flags contains the given [mask].
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UByte.hasFlag(mask: UByte): Boolean = mask and this == mask

/**
 * Remove the given [mask] from [UByte] this bit flag.
 *
 * [For more details](https://en.wikipedia.org/wiki/Mask_(computing))
 *
 * @author 凛 (RinOrz)
 */
inline fun UByte.removeFlag(mask: UByte): UByte = this and mask.inv()

/**
 * Packs two Float values into one Long value for use in inline classes.
 */
fun packFloats(val1: Float, val2: Float): Long {
  val v1 = val1.toBits().toLong()
  val v2 = val2.toBits().toLong()
  return v1.shl(32) or (v2 and 0xFFFFFFFF)
}

/**
 * Unpacks the first Float value in [packFloats] from its returned Long.
 */
fun unpackFloat1(value: Long): Float {
  return Float.fromBits(value.shr(32).toInt())
}

/**
 * Unpacks the second Float value in [packFloats] from its returned Long.
 */
fun unpackFloat2(value: Long): Float {
  return Float.fromBits(value.and(0xFFFFFFFF).toInt())
}

/**
 * Packs two Int values into one Long value for use in inline classes.
 */
fun packInts(val1: Int, val2: Int): Long {
  return val1.toLong().shl(32) or (val2.toLong() and 0xFFFFFFFF)
}

/**
 * Unpacks the first Int value in [packInts] from its returned ULong.
 */
fun unpackInt1(value: Long): Int {
  return value.shr(32).toInt()
}

/**
 * Unpacks the second Int value in [packInts] from its returned ULong.
 */
fun unpackInt2(value: Long): Int {
  return value.and(0xFFFFFFFF).toInt()
}