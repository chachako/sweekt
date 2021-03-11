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
import android.graphics.drawable.RotateDrawable

class RotateDrawableBuilder : DrawableWrapperBuilder<RotateDrawableBuilder>() {

  private var pivotX: Float = 0.5f
  private var pivotY: Float = 0.5f
  private var fromDegrees: Float = 0f
  private var toDegrees: Float = 360f

  fun pivotX(x: Float) = apply { pivotX = x }
  fun pivotY(y: Float) = apply { pivotY = y }
  fun fromDegrees(degree: Float) = apply { fromDegrees = degree }
  fun toDegrees(degree: Float) = apply { toDegrees = degree }

  override fun build(): Drawable {
    val rotateDrawable = RotateDrawable()
    drawable?.let {
      setDrawable(rotateDrawable, it)
      apply {
        setPivotX(rotateDrawable, pivotX)
        setPivotY(rotateDrawable, pivotY)
        setFromDegrees(rotateDrawable, fromDegrees)
        setToDegrees(rotateDrawable, toDegrees)
      }
    }
    return rotateDrawable
  }
}