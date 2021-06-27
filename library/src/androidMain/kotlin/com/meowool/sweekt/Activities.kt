@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment

/**
 * The abstract interface of activity or its holder, in order to get activity sometimes.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
interface ActivityHolder {
  /**
   * Provides current activity.
   */
  fun obtainActivity(): Activity
}

/**
 * Get the window root layout from this context, if not found, throws exception.
 *
 * @see Context.window
 * @see Window.rootLayout
 */
inline val Activity.windowRootLayout: ViewGroup get() = window.rootLayout

/**
 * Find the window root layout from this context, if not found, returns `null`.
 *
 * @see Context.findWindow
 * @see Window.rootLayout
 */
inline fun Activity.findWindowRootLayout(): ViewGroup? = window.findRootLayout()

/**
 * Starts the Activity [A].
 *
 * @param arguments additional arguments to be received by the activity.
 * @param configIntent configure the activity intent.
 *
 * @see activityIntent
 */
inline fun <reified A : Activity> Context.start(
  vararg arguments: Pair<String, *>,
  configIntent: Intent.() -> Unit = {}
) = startActivity(activityIntent<A>(*arguments, configuration = configIntent))

/**
 * Starts the Activity [A].
 *
 * @param arguments additional arguments to be received by the activity.
 * @param configIntent configure the activity intent.
 *
 * @see activityIntent
 */
inline fun <reified A : Activity> Fragment.start(
  vararg arguments: Pair<String, *>,
  configIntent: Intent.() -> Unit = {}
) = startActivity(activityIntent<A>(*arguments, configuration = configIntent))

