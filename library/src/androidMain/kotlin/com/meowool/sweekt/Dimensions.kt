package com.meowool.sweekt

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import com.meowool.sweekt.asFloat
import kotlin.math.roundToInt

/**
 * Converts this density-independent pixels to the value stands device pixels.
 *
 * In order to distinguish it from `Float.dp` unit in JetpackCompose, it is named `asDp`.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
fun Number.asDp(): Int = (this.asFloat() * Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * Converts this density-independent pixels to the value stands device pixels by given [context].
 *
 * In order to distinguish it from `Float.dp` unit in JetpackCompose, it is named `asDp`.
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.density
 */
fun Number.asDp(context: Context): Int = (this.asFloat() * context.resources.displayMetrics.density).roundToInt()


/**
 * Converts this scalable pixels to the value stands device pixels.
 *
 * In order to distinguish it from `Float.dp` unit in JetpackCompose, it is named `asDp`.
 *
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.scaledDensity
 */
fun Number.asSp(): Int = (this.asFloat() * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

/**
 * Converts this scalable pixels to the value stands device pixels by given [context].
 *
 * @see Context.getResources
 * @see Resources.getDisplayMetrics
 * @see DisplayMetrics.scaledDensity
 */
fun Number.asSp(context: Context): Int = (this.asFloat() * context.resources.displayMetrics.scaledDensity).roundToInt()