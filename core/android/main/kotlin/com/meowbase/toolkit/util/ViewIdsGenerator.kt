/*
 * Copyright 2019 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */
package com.meowbase.toolkit.util

import android.view.View
import com.meowbase.toolkit.os.isMainThread

/* copy from: https://github.com/LouisCAD/Splitties */

/**
 * Generates a View id that doesn't collide with AAPT generated ones (`R.id.xxx`).
 *
 * Specially **optimized for usage on main thread** to be synchronization free.
 * **Backwards compatible** below API 17.
 */
fun generateViewId(): Int = when {
  isMainThread -> mainThreadLastGeneratedId.also {
    // Decrement here to avoid any collision with other generated ids which are incremented.
    mainThreadLastGeneratedId = (if (it == 1) aaptIdsStart else it) - 1
  }
  else -> View.generateViewId()
}

/** aapt-generated IDs have the high byte nonzero. Clamp to the range under that. */
private const val aaptIdsStart = 0x00FFFFFF
private var mainThreadLastGeneratedId = aaptIdsStart - 1
