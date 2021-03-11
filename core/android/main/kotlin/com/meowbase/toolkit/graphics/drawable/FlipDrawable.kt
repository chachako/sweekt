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

package com.meowbase.toolkit.graphics.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable

class FlipDrawable(
  private var drawable: Drawable,
  private var orientation: Int = ORIENTATION_HORIZONTAL
) : Drawable() {

  companion object {
    const val ORIENTATION_HORIZONTAL = 0
    const val ORIENTATION_VERTICAL = 1
  }

  override fun draw(canvas: Canvas) {
    val saveCount = canvas.save()
    if (orientation == ORIENTATION_VERTICAL) {
      canvas.scale(1f, -1f, (bounds.width() / 2).toFloat(), (bounds.height() / 2).toFloat())
    } else {
      canvas.scale(-1f, 1f, (bounds.width() / 2).toFloat(), (bounds.height() / 2).toFloat())
    }
    drawable.bounds = Rect(0, 0, bounds.width(), bounds.height())
    drawable.draw(canvas)
    canvas.restoreToCount(saveCount)
  }

  override fun onLevelChange(level: Int): Boolean {
    drawable.level = level
    invalidateSelf()
    return true
  }

  override fun getIntrinsicWidth(): Int {
    return drawable.intrinsicWidth
  }

  override fun getIntrinsicHeight(): Int {
    return drawable.intrinsicHeight
  }

  override fun setAlpha(alpha: Int) {
    drawable.alpha = alpha
  }

  override fun getOpacity(): Int {
    return drawable.opacity
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
    drawable.colorFilter = colorFilter
  }
}