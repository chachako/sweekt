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

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import android.util.StateSet

inline fun buildRippleDrawable(block: RippleDrawableBuilder.() -> Unit) =
  RippleDrawableBuilder().apply(block).build()

class RippleDrawableBuilder : DrawableWrapperBuilder<RippleDrawableBuilder>() {

  private var color: Int = Constants.DEFAULT_COLOR
  private var colorStateList: ColorStateList? = null
  private var radius: Int = -1

  fun color(color: Int) = apply { this.color = color }
  fun colorStateList(colorStateList: ColorStateList?) =
    apply { this.colorStateList = colorStateList }

  fun radius(radius: Int) = apply { this.radius = radius }

  override fun build(): Drawable {
    var drawable = this.drawable!!
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val colorStateList = this.colorStateList ?: ColorStateList(
        arrayOf(StateSet.WILD_CARD),
        intArrayOf(color)
      )

      var mask = if (drawable is DrawableContainer) drawable.getCurrent() else drawable
      if (mask is ShapeDrawable) {
        val state = mask.getConstantState()
        if (state != null) {
          val temp = state.newDrawable().mutate() as ShapeDrawable
          temp.paint.color = Color.BLACK
          mask = temp
        }
      } else if (mask is GradientDrawable) {
        val state = mask.getConstantState()
        if (state != null) {
          val temp = state.newDrawable().mutate() as GradientDrawable
          temp.setColor(Color.BLACK)
          mask = temp
        }
      } else {
        mask = ColorDrawable(Color.BLACK)
      }

      val rippleDrawable = RippleDrawable(colorStateList, drawable, mask)
      setRadius(rippleDrawable, radius)
      rippleDrawable.invalidateSelf()
      drawable = rippleDrawable
    }
    return drawable
  }
}