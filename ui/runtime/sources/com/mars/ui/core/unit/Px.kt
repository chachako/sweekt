@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING")

package com.mars.ui.core.unit

/*
 * author: 凛
 * date: 2020/8/11 2:36 PM
 * github: https://github.com/oh-Rin
 * description: 像素单位
 */
inline class Px(val value: Float) : Comparable<Px>, SizeUnit {
  /**
   * Add two [Px]s together.
   */
  inline operator fun plus(other: Px) =
    Px(value = this.value + other.value)

  /**
   * Subtract a Px from another one.
   */
  inline operator fun minus(other: Px) =
    Px(value = this.value - other.value)

  /**
   * This is the same as multiplying the Px by -1.0.
   */
  inline operator fun unaryMinus() = Px(-value)

  /**
   * Divide a Px by a scalar.
   */
  inline operator fun div(other: Float): Px =
    Px(value = value / other)

  inline operator fun div(other: Int): Px =
    Px(value = value / other)

  /**
   * Divide by another Px to get a scalar.
   */
  inline operator fun div(other: Px): Float = value / other.value

  /**
   * Multiply a Px by a scalar.
   */
  inline operator fun times(other: Float): Px =
    Px(value = value * other)

  inline operator fun times(other: Int): Px =
    Px(value = value * other)

  /**
   * Support comparing Dimensions with comparison operators.
   */
  override /* TODO: inline */ operator fun compareTo(other: Px) = value.compareTo(other.value)
  override fun toString() = "$value.px"

  companion object {
    /**
     * A dimension used to represent a hairline drawing element. Zero elements take up no
     * space, but will draw a single pixel, independent of the device's resolution and density.
     */
    val Zero = Px(value = 0f)

    /**
     * Infinite px dimension.
     */
    val Infinity = Px(value = Float.POSITIVE_INFINITY)
  }
}

/**
 * Create a [Px] using an [Int]:
 *     val left = 10
 *     val x = left.px
 *     // -- or --
 *     val y = 10.px
 */
inline val Int.px: Px
  get() = Px(value = this.toFloat())

/**
 * Create a [Px] using a [Double]:
 *     val left = 10.0
 *     val x = left.px
 *     // -- or --
 *     val y = 10.0.px
 */
inline val Double.px: Px
  get() = Px(value = this.toFloat())

/**
 * Create a [Px] using a [Float]:
 *     val left = 10f
 *     val x = left.px
 *     // -- or --
 *     val y = 10f.px
 */
inline val Float.px: Px
  get() = Px(value = this)

inline operator fun Float.div(other: Px) =
  Px(this / other.value)

inline operator fun Double.div(other: Px) =
  Px(this.toFloat() / other.value)

inline operator fun Int.div(other: Px) =
  Px(this / other.value)

inline operator fun Float.times(other: Px) =
  Px(this * other.value)

inline operator fun Double.times(other: Px) =
  Px(this.toFloat() * other.value)

inline operator fun Int.times(other: Px) =
  Px(this * other.value)

inline fun min(a: Px, b: Px): Px = Px(value = kotlin.math.min(a.value, b.value))
inline fun max(a: Px, b: Px): Px = Px(value = kotlin.math.max(a.value, b.value))