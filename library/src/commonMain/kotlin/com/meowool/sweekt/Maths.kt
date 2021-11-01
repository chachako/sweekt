@file:Suppress("SpellCheckingInspection")

package com.meowool.sweekt

import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Linearly interpolate between [start] and [stop] with [fraction] fraction between them.
 */
fun lerp(start: Float, stop: Float, fraction: Float): Float {
  return (1 - fraction) * start + fraction * stop
}

/**
 * Linearly interpolate between [start] and [stop] with [fraction] fraction between them.
 */
fun lerp(start: Int, stop: Int, fraction: Float): Int {
  return start + ((stop - start) * fraction.toDouble()).roundToInt()
}

/**
 * Linearly interpolate between [start] and [stop] with [fraction] fraction between them.
 */
fun lerp(start: Long, stop: Long, fraction: Float): Long {
  return start + ((stop - start) * fraction.toDouble()).roundToLong()
}

/**
 * Convert this [Byte] to the hexadecimal string.
 */
fun Byte.toHexString() = toString(16).padStart(2, '0').uppercase()

/**
 * Convert this [Int] to the hexadecimal string.
 */
fun Int.toHexString() = toString(16).padStart(8, '0').uppercase()

/**
 * Convert this [Long] to the hexadecimal string.
 */
fun Long.toHexString() = toString(16).padStart(16, '0').uppercase()

/**
 * Converts this [ByteArray] to hexadecimal string.
 */
fun ByteArray.toHexString() = joinToString("") { it.toInt().and(0xFF).toString(16).padStart(2, '0') }

fun Float.toRadians(): Float = this / 180f * PI.toFloat()
fun Double.toRadians(): Double = this / 180 * PI