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

import android.graphics.drawable.Drawable
import android.graphics.drawable.ScaleDrawable
import android.view.Gravity

class ScaleDrawableBuilder : DrawableWrapperBuilder<ScaleDrawableBuilder>() {
  private var level: Int = 10000
  private var scaleGravity = Gravity.CENTER
  private var scaleWidth: Float = 0f
  private var scaleHeight: Float = 0f

  fun level(level: Int) = apply { this.level = level }
  fun scaleGravity(gravity: Int) = apply { this.scaleGravity = gravity }
  fun scaleWidth(scale: Float) = apply { this.scaleWidth = scale }
  fun scaleHeight(scale: Float) = apply { this.scaleHeight = scale }

  override fun build(): Drawable {
    val scaleDrawable = ScaleDrawable(drawable, scaleGravity, scaleWidth, scaleHeight)
    scaleDrawable.level = level
    return scaleDrawable
  }
}