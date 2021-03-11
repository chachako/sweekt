/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
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
