@file:Suppress("SpellCheckingInspection")

package com.mars.toolkit

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

@OptIn(ExperimentalUnsignedTypes::class)
fun Int.toHexString() = "0x${toUInt().toString(16).padStart(8, '0')}"

fun Float.toRadians(): Float = this / 180f * PI.toFloat()
fun Double.toRadians(): Double = this / 180 * PI