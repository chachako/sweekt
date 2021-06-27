@file:Suppress("SpellCheckingInspection", "NOTHING_TO_INLINE")

package com.meowool.sweekt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.meowool.sweekt.iteration.onEmpty
import java.io.Serializable

/**
 * Returns the new intent.
 *
 * @param arguments the arguments to be put into the result intent.
 * @param configuration configure the result intent.
 */
inline fun Intent(
  vararg arguments: Pair<String, *>,
  configuration: Intent.() -> Unit = {}
) = Intent()
  .apply { put(*arguments) }
  .apply(configuration)

/**
 * Returns the new intent.
 *
 * @param action the action of intent.
 * @param arguments the arguments to be put into the result intent.
 * @param configuration configure the result intent.
 */
inline fun Intent(
  action: String,
  vararg arguments: Pair<String, *>,
  configuration: Intent.() -> Unit = {}
) = Intent(action)
  .apply { put(*arguments) }
  .apply(configuration)

/**
 * Returns the new [A] activity intent.
 *
 * @param arguments the arguments to be put into the result intent.
 * @param configuration configure the result intent.
 */
inline fun <reified A : Activity> Context.activityIntent(
  vararg arguments: Pair<String, *>,
  configuration: Intent.() -> Unit = {}
) = Intent(this, A::class.java)
  .apply { put(*arguments) }
  .apply(configuration)

/**
 * Returns the new [A] activity intent.
 *
 * @param arguments the arguments to be put into the result intent.
 * @param configuration configure the result intent.
 */
inline fun <reified A : Activity> Fragment.activityIntent(
  vararg arguments: Pair<String, *>,
  configuration: Intent.() -> Unit = {}
) = requireContext().activityIntent<A>(*arguments, configuration = configuration)

/**
 * Put the [arguments] name and value to this [Intent].
 */
fun Intent.put(vararg arguments: Pair<String, *>): Intent = apply {
  arguments.onEmpty { return@apply }.forEach {
    when (val value = it.second) {
      is Array<*> -> when {
        value.isArrayOf<CharSequence>() -> putExtra(it.first, value)
        value.isArrayOf<String>() -> putExtra(it.first, value)
        value.isArrayOf<Parcelable>() -> putExtra(it.first, value)
        else -> error("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
      }
      is Int -> putExtra(it.first, value)
      is Long -> putExtra(it.first, value)
      is String -> putExtra(it.first, value)
      is Float -> putExtra(it.first, value)
      is Double -> putExtra(it.first, value)
      is Char -> putExtra(it.first, value)
      is Short -> putExtra(it.first, value)
      is Boolean -> putExtra(it.first, value)
      is Bundle -> putExtra(it.first, value)
      is IntArray -> putExtra(it.first, value)
      is LongArray -> putExtra(it.first, value)
      is FloatArray -> putExtra(it.first, value)
      is DoubleArray -> putExtra(it.first, value)
      is CharArray -> putExtra(it.first, value)
      is ShortArray -> putExtra(it.first, value)
      is BooleanArray -> putExtra(it.first, value)
      is CharSequence -> putExtra(it.first, value)
      is Serializable -> putExtra(it.first, value)
      is Parcelable -> putExtra(it.first, value)
      null -> putExtra(it.first, null as Serializable?)
      else -> error("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
    }
  }
}

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_DOCUMENT] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newDocument(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT) }

/**
 * Add the [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.excludeFromRecents(): Intent =
  apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

/**
 * Add the [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_ANIMATION] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_HISTORY] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

/**
 * Add the [Intent.FLAG_ACTIVITY_SINGLE_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }