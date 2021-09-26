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
val Float.dpInt: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * Converts this density-independent pixels to the int value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
fun Float.dpInt(context: Context): Int = (this * context.resources.displayMetrics.density).roundToInt()

/**
 * Converts this density-independent pixels to the long value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
val Float.dpLong: Long get() = (this * Resources.getSystem().displayMetrics.density).roundToLong()

/**
 * Converts this density-independent pixels to the long value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
fun Float.dpLong(context: Context): Long = (this * context.resources.displayMetrics.density).roundToLong()

/**
 * Converts this density-independent pixels to the float value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
val Float.dpFloat: Float get() = this * Resources.getSystem().displayMetrics.density

/**
 * Converts this density-independent pixels to the float value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
fun Float.dpFloat(context: Context): Float = this * context.resources.displayMetrics.density

/**
 * Converts this density-independent pixels to the double value stands device pixels.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
val Float.dpDouble: Double get() = (this * Resources.getSystem().displayMetrics.density).toDouble()

/**
 * Converts this density-independent pixels to the double value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 * @author 凛 (https://github.com/RinOrz)
 */
fun Float.dpDouble(context: Context): Double = (this * context.resources.displayMetrics.density).toDouble()