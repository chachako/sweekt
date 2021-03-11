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

import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape

class PathShapeDrawableBuilder {

  private var path: Path? = null
  private var pathStandardWidth: Float = 100f
  private var pathStandardHeight: Float = 100f
  private var width: Int = -1
  private var height: Int = -1

  fun path(path: Path, pathStandardWidth: Float, pathStandardHeight: Float) = apply {
    this.path = path
    this.pathStandardWidth = pathStandardWidth
    this.pathStandardHeight = pathStandardHeight
  }

  fun width(width: Int) = apply { this.width = width }
  fun height(height: Int) = apply { this.height = height }
  fun size(size: Int) = apply { width(size).height(size) }

  fun build(custom: ((shapeDrawable: ShapeDrawable) -> Unit)? = null): ShapeDrawable {
    val shapeDrawable = ShapeDrawable()
    if (path == null || width <= 0 || height <= 0) {
      return shapeDrawable
    }
    val pathShape = PathShape(path!!, pathStandardWidth, pathStandardHeight)

    shapeDrawable.shape = pathShape
    shapeDrawable.intrinsicWidth = width
    shapeDrawable.intrinsicHeight = height
    if (custom != null) {
      custom(shapeDrawable)
    }
    return shapeDrawable
  }
}
