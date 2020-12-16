/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("NOTHING_TO_INLINE")

package com.meowbase.ui.core.unit

import com.meowbase.toolkit.lerp
import com.meowbase.ui.core.unit.Dp.Companion.Zero
import com.meowbase.toolkit.packFloats
import com.meowbase.toolkit.unpackFloat1
import com.meowbase.toolkit.unpackFloat2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Dimension value representing device-independent pixels (dp). Component APIs specify their
 * dimensions such as line thickness in DP with Dp objects. Zero (1 pixel) thickness
 * may be specified with [Zero], a dimension that take up no space. Dp are normally
 * defined using [dp], which can be applied to [Int], [Double], and [Float].
 *     val leftMargin = 10.dp
 *     val rightMargin = 10f.dp
 *     val topMargin = 20.0.dp
 *     val bottomMargin = 10.dp
 * Drawing is done in pixels. To retrieve the pixel size of a Dp, use [toPx]:
 *     val lineThicknessPx = lineThickness.toPx(context)
 * [toPx] is normally needed only for painting operations.
 */
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class Dp(val value: Float) : Comparable<Dp>, SizeUnit {
  /**
   * Add two [Dp]s together.
   */
  inline operator fun plus(other: Dp) =
    Dp(value = this.value + other.value)

  inline operator fun plus(other: Float) =
    Dp(value = this.value + other)

  inline operator fun plus(other: Int) =
    Dp(value = this.value + other)

  /**
   * Subtract a Dp from another one.
   */
  inline operator fun minus(other: Dp) =
    Dp(value = this.value - other.value)

  inline operator fun minus(other: Float) =
    Dp(value = this.value - other)

  inline operator fun minus(other: Int) =
    Dp(value = this.value - other)

  /**
   * This is the same as multiplying the Dp by -1.0.
   */
  inline operator fun unaryMinus() = Dp(-value)

  /**
   * Divide a Dp by a scalar.
   */
  inline operator fun div(other: Float): Dp =
    Dp(value = value / other)

  inline operator fun div(other: Int): Dp =
    Dp(value = value / other)

  /**
   * Divide by another Dp to get a scalar.
   */
  inline operator fun div(other: Dp): Float = value / other.value

  /**
   * Divide by [DpSquared] to get a [DpInverse].
   */
  inline operator fun div(other: DpSquared): DpInverse =
    DpInverse(value = value / other.value)

  /**
   * Multiply a Dp by a scalar.
   */
  inline operator fun times(other: Float): Dp =
    Dp(value = value * other)

  inline operator fun times(other: Int): Dp =
    Dp(value = value * other)

  /**
   * Multiply by a Dp to get a [DpSquared] result.
   */
  inline operator fun times(other: Dp): DpSquared =
    DpSquared(value = value * other.value)

  /**
   * Multiply by a Dp to get a [DpSquared] result.
   */
  inline operator fun times(other: DpSquared): DpCubed =
    DpCubed(value = value * other.value)

  /**
   * Support comparing Dimensions with comparison operators.
   */
  override /* TODO: inline */ operator fun compareTo(other: Dp) = value.compareTo(other.value)
  override fun toString() = "$value.dp"

  companion object {
    /**
     * A dimension used to represent a hairline drawing element. Zero elements take up no
     * space, but will draw a single pixel, independent of the device's resolution and density.
     */
    val Zero = Dp(value = 0f)

    /**
     * Infinite dp dimension.
     */
    val Infinity = Dp(value = Float.POSITIVE_INFINITY)
  }
}

/**
 * Create a [Dp] using an [Int]:
 *     val left = 10
 *     val x = left.dp
 *     // -- or --
 *     val y = 10.dp
 */
inline val Int.dp: Dp
  get() = Dp(value = this.toFloat())

/**
 * Create a [Dp] using a [Double]:
 *     val left = 10.0
 *     val x = left.dp
 *     // -- or --
 *     val y = 10.0.dp
 */
inline val Double.dp: Dp
  get() = Dp(value = this.toFloat())

/**
 * Create a [Dp] using a [Float]:
 *     val left = 10f
 *     val x = left.dp
 *     // -- or --
 *     val y = 10f.dp
 */
inline val Float.dp: Dp
  get() = Dp(value = this)

inline operator fun Float.div(other: Dp) =
  DpInverse(this / other.value)

inline operator fun Double.div(other: Dp) =
  DpInverse(this.toFloat() / other.value)

inline operator fun Int.div(other: Dp) =
  DpInverse(this / other.value)

inline operator fun Float.times(other: Dp) =
  Dp(this * other.value)

inline operator fun Double.times(other: Dp) =
  Dp(this.toFloat() * other.value)

inline operator fun Int.times(other: Dp) =
  Dp(this * other.value)

inline fun min(a: Dp, b: Dp): Dp = Dp(value = min(a.value, b.value))
inline fun max(a: Dp, b: Dp): Dp = Dp(value = max(a.value, b.value))

/**
 * Ensures that this value lies in the specified range [minimumValue]..[maximumValue].
 *
 * @return this value if it's in the range, or [minimumValue] if this value is less than
 * [minimumValue], or [maximumValue] if this value is greater than [maximumValue].
 */
inline fun Dp.coerceIn(minimumValue: Dp, maximumValue: Dp): Dp =
  Dp(value = value.coerceIn(minimumValue.value, maximumValue.value))

/**
 * Ensures that this value is not less than the specified [minimumValue].
 * @return this value if it's greater than or equal to the [minimumValue] or the
 * [minimumValue] otherwise.
 */
inline fun Dp.coerceAtLeast(minimumValue: Dp): Dp =
  Dp(value = value.coerceAtLeast(minimumValue.value))

/**
 * Ensures that this value is not greater than the specified [maximumValue].
 *
 * @return this value if it's less than or equal to the [maximumValue] or the
 * [maximumValue] otherwise.
 */
inline fun Dp.coerceAtMost(maximumValue: Dp): Dp =
  Dp(value = value.coerceAtMost(maximumValue.value))

/**
 *
 * Return `true` when it is finite or `false` when it is [Dp.Infinity]
 */
inline fun Dp.isFinite(): Boolean = value != Float.POSITIVE_INFINITY

/**
 * Linearly interpolate between two [Dp]s.
 *
 * The [fraction] argument represents position on the timeline, with 0.0 meaning
 * that the interpolation has not started, returning [start] (or something
 * equivalent to [start]), 1.0 meaning that the interpolation has finished,
 * returning [stop] (or something equivalent to [stop]), and values in between
 * meaning that the interpolation is at the relevant point on the timeline
 * between [start] and [stop]. The interpolation can be extrapolated beyond 0.0 and
 * 1.0, so negative values and values greater than 1.0 are valid.
 */
fun lerp(start: Dp, stop: Dp, fraction: Float): Dp {
  return Dp(lerp(start.value, stop.value, fraction))
}

/**
 * Holds a unit of squared dimensions, such as `1.value * 2.dp`. [DpSquared], [DpCubed],
 * and [DpInverse] are used primarily for [Dp] calculations to ensure resulting
 * units are as expected. Many times, [Dp] calculations use scalars to determine the final
 * dimension during calculation:
 *     val width = oldWidth * stretchAmount
 * Other times, it is useful to do intermediate calculations with Dimensions directly:
 *     val width = oldWidth * newTotalWidth / oldTotalWidth
 */
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class DpSquared(val value: Float) : Comparable<DpSquared>, SizeUnit {
  /**
   * Add two DimensionSquares together.
   */
  inline operator fun plus(other: DpSquared) =
    DpSquared(value = value + other.value)

  inline operator fun plus(other: Float) =
    DpSquared(value = value + other)

  inline operator fun plus(other: Int) =
    DpSquared(value = value + other)

  /**
   * Subtract a DimensionSquare from another one.
   */
  inline operator fun minus(other: DpSquared) =
    DpSquared(value = value - other.value)

  inline operator fun minus(other: Float) =
    DpSquared(value = value - other)

  inline operator fun minus(other: Int) =
    DpSquared(value = value - other)

  /**
   * Divide a DimensionSquare by a scalar.
   */
  inline operator fun div(other: Float): DpSquared =
    DpSquared(value = value / other)

  inline operator fun div(other: Int): DpSquared =
    DpSquared(value = value / other)

  /**
   * Divide by a [Dp] to get a [Dp] result.
   */
  inline operator fun div(other: Dp): Dp =
    Dp(value = value / other.value)

  /**
   * Divide by a DpSquared to get a scalar result.
   */
  inline operator fun div(other: DpSquared): Float = value / other.value

  /**
   * Divide by a [DpCubed] to get a [DpInverse] result.
   */
  inline operator fun div(other: DpCubed): DpInverse =
    DpInverse(value / other.value)

  /**
   * Multiply by a scalar to get a DpSquared result.
   */
  inline operator fun times(other: Float): DpSquared =
    DpSquared(value = value * other)

  inline operator fun times(other: Int): DpSquared =
    DpSquared(value = value * other)

  /**
   * Multiply by a scalar to get a DpSquared result.
   */
  inline operator fun times(other: Dp): DpCubed =
    DpCubed(value = value * other.value)

  /**
   * Support comparing DpSquared with comparison operators.
   */
  override /* TODO: inline */ operator fun compareTo(other: DpSquared) =
    value.compareTo(other.value)

  override fun toString(): String = "$value.dp^2"
}

/**
 * Holds a unit of cubed dimensions, such as `1.value * 2.value * 3.dp`. [DpSquared],
 * [DpCubed], and [DpInverse] are used primarily for [Dp] calculations to
 * ensure resulting units are as expected. Many times, [Dp] calculations use scalars to
 * determine the final dimension during calculation:
 *     val width = oldWidth * stretchAmount
 * Other times, it is useful to do intermediate calculations with Dimensions directly:
 *     val width = oldWidth * newTotalWidth / oldTotalWidth
 */
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class DpCubed(val value: Float) : Comparable<DpCubed>, SizeUnit {

  /**
   * Add two DpCubed together.
   */
  inline operator fun plus(dimension: DpCubed) =
    DpCubed(value = value + dimension.value)

  inline operator fun plus(dimension: Float) =
    DpCubed(value = value + dimension)

  inline operator fun plus(dimension: Int) =
    DpCubed(value = value + dimension)

  /**
   * Subtract a DpCubed from another one.
   */
  inline operator fun minus(dimension: DpCubed) =
    DpCubed(value = value - dimension.value)

  inline operator fun minus(dimension: Float) =
    DpCubed(value = value - dimension)

  inline operator fun minus(dimension: Int) =
    DpCubed(value = value - dimension)

  /**
   * Divide a DpCubed by a scalar.
   */
  inline operator fun div(other: Float): DpCubed =
    DpCubed(value = value / other)

  inline operator fun div(other: Int): DpCubed =
    DpCubed(value = value / other)

  /**
   * Divide by a [Dp] to get a [DpSquared] result.
   */
  inline operator fun div(other: Dp): DpSquared =
    DpSquared(value = value / other.value)

  /**
   * Divide by a [DpSquared] to get a [Dp] result.
   */
  inline operator fun div(other: DpSquared): Dp =
    Dp(value = value / other.value)

  /**
   * Divide by a DpCubed to get a scalar result.
   */
  inline operator fun div(other: DpCubed): Float = value / other.value

  /**
   * Multiply by a scalar to get a DpCubed result.
   */
  inline operator fun times(other: Float): DpCubed =
    DpCubed(value = value * other)

  inline operator fun times(other: Int): DpCubed =
    DpCubed(value = value * other)

  /**
   * Support comparing DpCubed with comparison operators.
   */
  override /* TODO: inline */ operator fun compareTo(other: DpCubed) =
    value.compareTo(other.value)

  override fun toString(): String = "$value.dp^3"
}

/**
 * Holds a unit of an inverse dimensions, such as `1.dp / (2.value * 3.dp)`. [DpSquared],
 * [DpCubed], and [DpInverse] are used primarily for [Dp] calculations to
 * ensure resulting units are as expected. Many times, [Dp] calculations use scalars to
 * determine the final dimension during calculation:
 *     val width = oldWidth * stretchAmount
 * Other times, it is useful to do intermediate calculations with Dimensions directly:
 *     val width = oldWidth * newTotalWidth / oldTotalWidth
 */
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class DpInverse(val value: Float) : Comparable<DpInverse>, SizeUnit {
  /**
   * Add two DpInverse together.
   */
  inline operator fun plus(dimension: DpInverse) =
    DpInverse(value = value + dimension.value)

  inline operator fun plus(dimension: Float) =
    DpInverse(value = value + dimension)

  inline operator fun plus(dimension: Int) =
    DpInverse(value = value + dimension)

  /**
   * Subtract a DpInverse from another one.
   */
  inline operator fun minus(dimension: DpInverse) =
    DpInverse(value = value - dimension.value)

  inline operator fun minus(dimension: Float) =
    DpInverse(value = value - dimension)

  inline operator fun minus(dimension: Int) =
    DpInverse(value = value - dimension)

  /**
   * Divide a DpInverse by a scalar.
   */
  inline operator fun div(other: Float): DpInverse =
    DpInverse(value = value / other)

  inline operator fun div(other: Int): DpInverse =
    DpInverse(value = value / other)

  /**
   * Multiply by a scalar to get a DpInverse result.
   */
  inline operator fun times(other: Float): DpInverse =
    DpInverse(value = value * other)

  inline operator fun times(other: Int): DpInverse =
    DpInverse(value = value * other)

  /**
   * Multiply by a [Dp] to get a scalar result.
   */
  inline operator fun times(other: Dp): Float = value * other.value

  /**
   * Multiply by a [DpSquared] to get a [Dp] result.
   */
  inline operator fun times(other: DpSquared): Dp =
    Dp(value = value * other.value)

  /**
   * Multiply by a [DpCubed] to get a [DpSquared] result.
   */
  inline operator fun times(other: DpCubed): DpSquared =
    DpSquared(value = value * other.value)

  /**
   * Support comparing DpInverse with comparison operators.
   */
  override /* TODO: inline */ operator fun compareTo(other: DpInverse) =
    value.compareTo(other.value)

  override fun toString(): String = "$value.dp^-1"
}

// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
// Structures using Dp
// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

/**
 * A two-dimensional position using [Dp] for units
 */
@OptIn(ExperimentalUnsignedTypes::class)
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class Position(@PublishedApi internal val packedValue: Long) {
  /**
   * The horizontal aspect of the position in [Dp]
   */
  /*inline*/ val x: Dp
    get() = unpackFloat1(packedValue).dp

  /**
   * The vertical aspect of the position in [Dp]
   */
  /*inline*/ val y: Dp
    get() = unpackFloat2(packedValue).dp

  /**
   * Returns a copy of this Position instance optionally overriding the
   * x or y parameter
   */
  fun copy(x: Dp = this.x, y: Dp = this.y): Position = Position(x, y)

  /**
   * Subtract a [Position] from another one.
   */
  inline operator fun minus(other: Position) =
    Position(x - other.x, y - other.y)

  /**
   * Add a [Position] to another one.
   */
  inline operator fun plus(other: Position) =
    Position(x + other.x, y + other.y)

  override fun toString(): String = "($x, $y)"

  companion object
}

/**
 * Constructs a [Position] from [x] and [y] position [Dp] values.
 */
@OptIn(ExperimentalUnsignedTypes::class)
inline fun Position(x: Dp, y: Dp): Position = Position(packFloats(x.value, y.value))

/**
 * The magnitude of the offset represented by this [Position].
 */
fun Position.getDistance(): Dp {
  return Dp(sqrt(x.value * x.value + y.value * y.value))
}

/**
 * Linearly interpolate between two [Position]s.
 *
 * The [fraction] argument represents position on the timeline, with 0.0 meaning
 * that the interpolation has not started, returning [start] (or something
 * equivalent to [start]), 1.0 meaning that the interpolation has finished,
 * returning [stop] (or something equivalent to [stop]), and values in between
 * meaning that the interpolation is at the relevant point on the timeline
 * between [start] and [stop]. The interpolation can be extrapolated beyond 0.0 and
 * 1.0, so negative values and values greater than 1.0 are valid.
 */
fun lerp(start: Position, stop: Position, fraction: Float): Position =
  Position(lerp(start.x, stop.x, fraction), lerp(start.y, stop.y, fraction))

/**
 * A four dimensional bounds using [Dp] for units
 */
data class Bounds(
  val left: Dp,
  val top: Dp,
  val right: Dp,
  val bottom: Dp
) {
  companion object
}

/**
 * A width of this Bounds in [Dp].
 */
inline val Bounds.width: Dp
  get() = right - left

/**
 * A height of this Bounds in [Dp].
 */
inline val Bounds.height: Dp
  get() = bottom - top