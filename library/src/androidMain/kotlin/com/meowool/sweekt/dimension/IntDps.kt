package com.meowool.sweekt.dimension

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Converts this density-independent pixels to the int value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
val Int.dpInt: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * Converts this density-independent pixels to the int value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
fun Int.dpInt(context: Context): Int = (this * context.resources.displayMetrics.density).roundToInt()

/**
 * Converts this density-independent pixels to the long value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
val Int.dpLong: Long get() = (this * Resources.getSystem().displayMetrics.density).roundToLong()

/**
 * Converts this density-independent pixels to the long value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
fun Int.dpLong(context: Context): Long = (this * context.resources.displayMetrics.density).roundToLong()

/**
 * Converts this density-independent pixels to the float value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
val Int.dpFloat: Float get() = this * Resources.getSystem().displayMetrics.density

/**
 * Converts this density-independent pixels to the float value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
fun Int.dpFloat(context: Context): Float = this * context.resources.displayMetrics.density

/**
 * Converts this density-independent pixels to the double value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
val Int.dpDouble: Double get() = (this * Resources.getSystem().displayMetrics.density).toDouble()

/**
 * Converts this density-independent pixels to the double value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
fun Int.dpDouble(context: Context): Double = (this * context.resources.displayMetrics.density).toDouble()