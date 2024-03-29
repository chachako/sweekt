@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.ViewGroup
import android.view.Window

/**
 * The abstract interface of context or its holder, in order to get context sometimes.
 *
 * @author 凛 (RinOrz)
 */
interface ContextHolder {
  /**
   * Provides current context.
   */
  fun obtainContext(): Context
}

/**
 * Returns [Activity] if this context is an activity, otherwise throw the [TypeCastException].
 *
 * @see Context.asActivityOrNull
 * @author 凛 (RinOrz)
 */
fun Context.asActivity(): Activity = asActivityOrNull()
  ?: throw TypeCastException("Cannot use a context instance of type ${this.javaClass.name} as an activity.")

/**
 * Returns [Activity] if this context is an activity, otherwise returns `null`.
 *
 * @see Context.asActivity
 * @author 凛 (RinOrz)
 */
fun Context.asActivityOrNull(): Activity? = this.castOrNull()
  ?: this.castOrNull<ContextWrapper>()?.baseContext?.asActivityOrNull()
  ?: this.castOrNull<ContextHolder>()?.obtainContext()?.asActivityOrNull()
  ?: this.castOrNull<ActivityHolder>()?.obtainActivity()

/**
 * Get the window from this context.
 *
 * @see Context.findWindow
 * @author 凛 (RinOrz)
 */
inline val Context.window: Window get() = this.asActivityOrNull()?.window
  ?: error("Cannot find window from the context of a non-activity instance, accident happened on `${this.javaClass.name}`.")

/**
 * Find the window from this context and return, if not found, returns `null`.
 *
 * @see Context.window
 * @author 凛 (RinOrz)
 */
inline fun Context.findWindow(): Window? = this.asActivityOrNull()?.window

/**
 * Get the window root layout from this context, if not found, throws exception.
 *
 * @see Context.window
 * @see Window.rootLayout
 * @author 凛 (RinOrz)
 */
inline val Context.windowRootLayout: ViewGroup get() = window.rootLayout

/**
 * Find the window root layout from this context and return, if not found, returns `null`.
 *
 * @see Context.findWindow
 * @see Window.rootLayout
 * @author 凛 (RinOrz)
 */
inline fun Context.findWindowRootLayout(): ViewGroup? = findWindow()?.rootLayout
