/*
 * Copyright 2019 The Android Open Source Project
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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.ui.core.graphics.geometry

import com.meowbase.toolkit.float
import com.meowbase.toolkit.lerp
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toPx
import com.meowbase.toolkit.packFloats
import com.meowbase.ui.util.toStringAsFixed
import com.meowbase.toolkit.unpackFloat1
import com.meowbase.toolkit.unpackFloat2
import kotlin.math.sqrt

/**
 * Convert [SizeUnit] to [Offset]
 */
fun SizeUnit.toOffset() = Offset(this.toPx(), this.toPx())

/**
 * Convert [Number] to [Offset]
 */
fun Number.toOffset() = Offset(this.float, this.float)

/**
 * Constructs an Offset from the given relative x and y offsets
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Offset(x: SizeUnit, y: SizeUnit) = Offset(packFloats(x.toPx(), y.toPx()))

/**
 * Constructs an Offset from the given relative x and y offsets
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Offset(x: Number, y: Number) = Offset(packFloats(x.float, y.float))

/**
 * An immutable 2D floating-point offset.
 *
 * Generally speaking, Offsets can be interpreted in two ways:
 *
 * 1. As representing a point in Cartesian space a specified distance from a
 *    separately-maintained origin. For example, the top-left position of
 *    children in the [RenderBox] protocol is typically represented as an
 *    [Offset] from the top left of the parent box.
 *
 * 2. As a vector that can be applied to coordinates. For example, when
 *    painting a [RenderObject], the parent is passed an [Offset] from the
 *    screen's origin which it can add to the offsets of its children to find
 *    the [Offset] from the screen's origin to each of the children.
 *
 * Because a particular [Offset] can be interpreted as one sense at one time
 * then as the other sense at a later time, the same class is used for both
 * senses.
 *
 * See also:
 *
 *  * [Size], which represents a vector describing the size of a rectangle.
 *
 * Creates an offset. The first argument sets [x], the horizontal component,
 * and the second sets [y], the vertical component.
 */
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class Offset(val packedValue: Long) {
  val x: Float
    get() = unpackFloat1(packedValue)
  val y: Float
    get() = unpackFloat2(packedValue)

  operator fun component1(): Float = x
  operator fun component2(): Float = y

  /**
   * Returns a copy of this Offset instance optionally overriding the
   * x or y parameter
   */
  fun copy(x: Float = this.x, y: Float = this.y) = Offset(x, y)

  companion object {
    /**
     * An offset with zero magnitude.
     *
     * This can be used to represent the origin of a coordinate space.
     */
    val Zero = Offset(0.0f, 0.0f)

    /**
     * An offset with infinite x and y components.
     *
     * See also:
     *
     *  * [isInfinite], which checks whether either component is infinite.
     *  * [isFinite], which checks whether both components are finite.
     */
    // This is included for completeness, because [Size.infinite] exists.
    val Infinite = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
  }

  fun isValid(): Boolean {
    check(!x.isNaN() && !y.isNaN()) {
      "Offset argument contained a NaN value."
    }
    return true
  }

  /**
   * The magnitude of the offset.
   *
   * If you need this value to compare it to another [Offset]'s distance,
   * consider using [getDistanceSquared] instead, since it is cheaper to compute.
   */
  fun getDistance() = sqrt(x * x + y * y)

  /**
   * The square of the magnitude of the offset.
   *
   * This is cheaper than computing the [getDistance] itself.
   */
  fun getDistanceSquared() = x * x + y * y

  /**
   * Unary negation operator.
   *
   * Returns an offset with the coordinates negated.
   *
   * If the [Offset] represents an arrow on a plane, this operator returns the
   * same arrow but pointing in the reverse direction.
   */
  operator fun unaryMinus(): Offset = Offset(-x, -y)

  /**
   * Binary subtraction operator.
   *
   * Returns an offset whose [x] value is the left-hand-side operand's [x]
   * minus the right-hand-side operand's [x] and whose [y] value is the
   * left-hand-side operand's [y] minus the right-hand-side operand's [y].
   */
  operator fun minus(other: Offset): Offset = Offset(x - other.x, y - other.y)

  /**
   * Binary addition operator.
   *
   * Returns an offset whose [x] value is the sum of the [x] values of the
   * two operands, and whose [y] value is the sum of the [y] values of the
   * two operands.
   */
  operator fun plus(other: Offset): Offset = Offset(x + other.x, y + other.y)

  /**
   * Multiplication operator.
   *
   * Returns an offset whose coordinates are the coordinates of the
   * left-hand-side operand (an Offset) multiplied by the scalar
   * right-hand-side operand (a Float).
   */
  operator fun times(operand: Float): Offset = Offset(x * operand, y * operand)

  /**
   * Division operator.
   *
   * Returns an offset whose coordinates are the coordinates of the
   * left-hand-side operand (an Offset) divided by the scalar right-hand-side
   * operand (a Float).
   */
  operator fun div(operand: Float): Offset = Offset(x / operand, y / operand)

  /**
   * Modulo (remainder) operator.
   *
   * Returns an offset whose coordinates are the remainder of dividing the
   * coordinates of the left-hand-side operand (an Offset) by the scalar
   * right-hand-side operand (a Float).
   */
  operator fun rem(operand: Float) = Offset(x % operand, y % operand)

  override fun toString() = "Offset(${x.toStringAsFixed(1)}, ${y.toStringAsFixed(1)})"
}

/**
 * Linearly interpolate between two offsets.
 *
 * The [fraction] argument represents position on the timeline, with 0.0 meaning
 * that the interpolation has not started, returning [start] (or something
 * equivalent to [start]), 1.0 meaning that the interpolation has finished,
 * returning [stop] (or something equivalent to [stop]), and values in between
 * meaning that the interpolation is at the relevant point on the timeline
 * between [start] and [stop]. The interpolation can be extrapolated beyond 0.0 and
 * 1.0, so negative values and values greater than 1.0 are valid (and can
 * easily be generated by curves).
 *
 * Values for [fraction] are usually obtained from an [Animation<Float>], such as
 * an `AnimationController`.
 */
fun lerp(start: Offset, stop: Offset, fraction: Float): Offset {
  return Offset(
    lerp(start.x, stop.x, fraction),
    lerp(start.y, stop.y, fraction)
  )
}