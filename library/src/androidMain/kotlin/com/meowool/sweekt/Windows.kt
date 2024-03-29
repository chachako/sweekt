@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import android.view.ViewGroup
import android.view.Window

/**
 * Returns the root layout in this window.
 *
 * @see Window.findRootLayout
 * @author 凛 (RinOrz)
 */
val Window.rootLayout: ViewGroup
  get() = findRootLayout() ?: error("Can't find window root layout.")

/**
 * Find the root layout from this window, if not found, returns `null`.
 *
 * @see Window.rootLayout
 * @author 凛 (RinOrz)
 */
inline fun Window.findRootLayout(): ViewGroup? = decorView.castOrNull<ViewGroup>()
  ?: decorView.findViewById(android.R.id.content)