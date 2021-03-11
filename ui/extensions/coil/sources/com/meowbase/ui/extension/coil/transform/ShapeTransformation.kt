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

@file:Suppress("unused")

package com.meowbase.ui.extension.coil.transform

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.CircleCropTransformation
import coil.transform.Transformation
import com.meowbase.ui.core.graphics.shape.Shape

/**
 * 一个用于裁剪图片为给定的形状的转换类
 * @param shape 需要裁剪的形状
 */
data class ShapeTransformation(private val shape: Shape) : Transformation {
  override fun key(): String = CircleCropTransformation::class.java.name

  override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    return pool.get(
      width = input.width,
      height = input.height,
      config = input.config ?: Bitmap.Config.ARGB_8888
    ).applyCanvas {
      val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
      val outline = shape.getOutline(rect)

      if (outline.usePath) {
        drawPath(outline.forcePath, paint)
      } else when {
        outline.isOval -> drawOval(rect, paint)
        outline.rectRadius == 0F -> drawRect(rect, paint)
        else -> drawRoundRect(rect, outline.rectRadius, outline.rectRadius, paint)
      }
    }
  }
}
