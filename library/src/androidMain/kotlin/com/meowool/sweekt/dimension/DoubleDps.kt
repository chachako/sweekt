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
val Double.dpInt: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * Converts this density-independent pixels to the int value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
fun Double.dpInt(context: Context): Int = (this * context.resources.displayMetrics.density).roundToInt()

/**
 * Converts this density-independent pixels to the long value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
val Double.dpLong: Long get() = (this * Resources.getSystem().displayMetrics.density).roundToLong()

/**
 * Converts this density-independent pixels to the long value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
fun Double.dpLong(context: Context): Long = (this * context.resources.displayMetrics.density).roundToLong()

/**
 * Converts this density-independent pixels to the float value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
val Double.dpFloat: Float get() = (this * Resources.getSystem().displayMetrics.density).toFloat()

/**
 * Converts this density-independent pixels to the float value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
fun Double.dpFloat(context: Context): Float = (this * context.resources.displayMetrics.density).toFloat()

/**
 * Converts this density-independent pixels to the double value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
val Double.dpDouble: Double get() = this * Resources.getSystem().displayMetrics.density

/**
 * Converts this density-independent pixels to the double value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
fun Double.dpDouble(context: Context): Double = this * context.resources.displayMetrics.density