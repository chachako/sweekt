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

@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.meowbase.ui.core.graphics.shape

import android.graphics.RectF
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.meowbase.ui.core.graphics.Outline
import com.meowbase.ui.core.graphics.createOutlinePath
import com.meowbase.ui.core.graphics.geometry.minSize
import com.meowbase.ui.core.unit.SizeUnit

/*
 * author: 凛
 * date: 2020/8/9 12:06 PM
 * github: https://github.com/RinOrz
 * description: 四边为平滑圆角的形状 https://www.zhihu.com/question/21191767
 * reference: https://github.com/MartinRGB/sketch-smooth-corner-android/blob/master/app/src/main/java/com/example/martinrgb/roundconrner/smoothcorners/SketchSmoothCorners.java
 */
class SmoothCornerShape(
  topLeft: CornerSize = ZeroCornerSize,
  topRight: CornerSize = ZeroCornerSize,
  bottomRight: CornerSize = ZeroCornerSize,
  bottomLeft: CornerSize = ZeroCornerSize,
) : CornerBasedShape(topLeft, topRight, bottomRight, bottomLeft) {
  override fun getOutline(
    bounds: RectF,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float
  ): Outline = when {
    bounds.minSize / 2 == 100f -> {
      // 百分百圆度的角在平滑圆角模式情况下会出问题，所以这里我们切换回正常情况的全圆角（效果完全相同）
      CircleRoundedShape.getOutline(bounds, topLeft, topRight, bottomRight, bottomLeft)
    }
    topLeft + topRight + bottomLeft + bottomRight == 0f -> {
      RectangleShape.getOutline(bounds, topLeft, topRight, bottomRight, bottomLeft)
    }
    else -> createOutlinePath {
      val width = bounds.width()
      val height = bounds.height()
      val centerX = width * 1.0f / 2
      val centerY = height * 1.0f / 2
      val posX: Float = centerX - width / 2
      val posY: Float = centerY - height / 2

      val trVertexRatio: Float = if (topRight / (width / 2).coerceAtMost(height / 2) > 0.5) {
        val percentage = (topRight / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
        val clampedPer = 1f.coerceAtMost(percentage)
        1f - (1 - 1.104f / 1.2819f) * clampedPer
      } else {
        1f
      }
      val brVertexRatio: Float = if (bottomRight / (width / 2).coerceAtMost(height / 2) > 0.5) {
        val percentage = (bottomRight / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
        val clampedPer = 1f.coerceAtMost(percentage)
        1f - (1 - 1.104f / 1.2819f) * clampedPer
      } else {
        1f
      }
      val tlVertexRatio: Float = if (topLeft / (width / 2).coerceAtMost(height / 2) > 0.5) {
        val percentage = (topLeft / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
        val clampedPer = 1f.coerceAtMost(percentage)
        1f - (1 - 1.104f / 1.2819f) * clampedPer
      } else {
        1f
      }
      val blVertexRatio: Float = if (bottomLeft / (width / 2).coerceAtMost(height / 2) > 0.5) {
        val percentage = (bottomLeft / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
        val clampedPer = 1f.coerceAtMost(percentage)
        1f - (1 - 1.104f / 1.2819f) * clampedPer
      } else {
        1f
      }


      val tlControlRatio: Float = if (topLeft / (width / 2).coerceAtMost(height / 2) > 0.6) {
        val percentage = (topLeft / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
        val clampedPer = 1f.coerceAtMost(percentage)
        1 + (0.8717f / 0.8362f - 1) * clampedPer
      } else {
        1f
      }
      val trControlRatio: Float = if (topRight / (width / 2).coerceAtMost(height / 2) > 0.6) {
        val percentage = (topRight / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
        val clampedPer = 1f.coerceAtMost(percentage)
        1 + (0.8717f / 0.8362f - 1) * clampedPer
      } else {
        1f
      }
      val blControlRatio: Float = if (bottomLeft / (width / 2).coerceAtMost(height / 2) > 0.6) {
        val percentage = (bottomLeft / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
        val clampedPer = 1f.coerceAtMost(percentage)
        1 + (0.8717f / 0.8362f - 1) * clampedPer
      } else {
        1f
      }
      val brControlRatio: Float = if (bottomRight / (width / 2).coerceAtMost(height / 2) > 0.6) {
        val percentage = (bottomRight / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
        val clampedPer = 1f.coerceAtMost(percentage)
        1 + (0.8717f / 0.8362f - 1) * clampedPer
      } else {
        1f
      }

      moveTo(posX + width / 2, posY)

      lineTo(
        posX + (width / 2).coerceAtLeast(width - topRight / 100.0f * 128.19f * trVertexRatio),
        posY
      )
      cubicTo(
        posX + width - topRight / 100f * 83.62f * trControlRatio,
        posY,
        posX + width - topRight / 100f * 67.45f,
        posY + topRight / 100f * 4.64f,
        posX + width - topRight / 100f * 51.16f,
        posY + topRight / 100f * 13.36f
      )
      cubicTo(
        posX + width - topRight / 100f * 34.86f,
        posY + topRight / 100f * 22.07f,
        posX + width - topRight / 100f * 22.07f,
        posY + topRight / 100f * 34.86f,
        posX + width - topRight / 100f * 13.36f,
        posY + topRight / 100f * 51.16f
      )
      cubicTo(
        posX + width - topRight / 100f * 4.64f,
        posY + topRight / 100f * 67.45f,
        posX + width,
        posY + topRight / 100f * 83.62f * trControlRatio,
        posX + width,
        posY + (height / 2).coerceAtMost(topRight / 100f * 128.19f * trVertexRatio)
      )

      lineTo(
        posX + width,
        posY + (height / 2).coerceAtLeast(height - bottomRight / 100f * 128.19f * brVertexRatio)
      )
      cubicTo(
        posX + width,
        posY + height - bottomRight / 100f * 83.62f * brControlRatio,
        posX + width - bottomRight / 100f * 4.64f,
        posY + height - bottomRight / 100f * 67.45f,
        posX + width - bottomRight / 100f * 13.36f,
        posY + height - bottomRight / 100f * 51.16f
      )
      cubicTo(
        posX + width - bottomRight / 100f * 22.07f,
        posY + height - bottomRight / 100f * 34.86f,
        posX + width - bottomRight / 100f * 34.86f,
        posY + height - bottomRight / 100f * 22.07f,
        posX + width - bottomRight / 100f * 51.16f,
        posY + height - bottomRight / 100f * 13.36f
      )
      cubicTo(
        posX + width - bottomRight / 100f * 67.45f,
        posY + height - bottomRight / 100f * 4.64f,
        posX + width - bottomRight / 100f * 83.62f * brControlRatio,
        posY + height,
        posX + (width / 2).coerceAtLeast(width - bottomRight / 100f * 128.19f * brVertexRatio),
        posY + height
      )

      lineTo(
        posX + (width / 2).coerceAtMost(bottomLeft / 100f * 128.19f * blControlRatio),
        posY + height
      )
      cubicTo(
        posX + bottomLeft / 100f * 83.62f * blControlRatio,
        posY + height,
        posX + bottomLeft / 100f * 67.45f,
        posY + height - bottomLeft / 100f * 4.64f,
        posX + bottomLeft / 100f * 51.16f,
        posY + height - bottomLeft / 100f * 13.36f
      )
      cubicTo(
        posX + bottomLeft / 100f * 34.86f,
        posY + height - bottomLeft / 100f * 22.07f,
        posX + bottomLeft / 100f * 22.07f,
        posY + height - bottomLeft / 100f * 34.86f,
        posX + bottomLeft / 100f * 13.36f,
        posY + height - bottomLeft / 100f * 51.16f
      )
      cubicTo(
        posX + bottomLeft / 100f * 4.64f,
        posY + height - bottomLeft / 100f * 67.45f,
        posX,
        posY + height - bottomLeft / 100f * 83.62f * blControlRatio,
        posX,
        posY + (height / 2).coerceAtLeast(height - bottomLeft / 100f * 128.19f * blVertexRatio)
      )

      lineTo(posX, posY + (height / 2).coerceAtMost(topLeft / 100f * 128.19f * tlVertexRatio))
      cubicTo(
        posX,
        posY + topLeft / 100f * 83.62f * tlControlRatio,
        posX + topLeft / 100f * 4.64f,
        posY + topLeft / 100f * 67.45f,
        posX + topLeft / 100f * 13.36f,
        posY + topLeft / 100f * 51.16f
      )
      cubicTo(
        posX + topLeft / 100f * 22.07f,
        posY + topLeft / 100f * 34.86f,
        posX + topLeft / 100f * 34.86f,
        posY + topLeft / 100f * 22.07f,
        posX + topLeft / 100f * 51.16f,
        posY + topLeft / 100f * 13.36f
      )
      cubicTo(
        posX + topLeft / 100f * 67.45f,
        posY + topLeft / 100f * 4.64f,
        posX + topLeft / 100f * 83.62f * tlControlRatio,
        posY,
        posX + (width / 2).coerceAtMost(topLeft / 100f * 128.19f * tlVertexRatio),
        posY
      )
      close()
    }
  }

  override fun copy(
    topLeft: CornerSize,
    topRight: CornerSize,
    bottomRight: CornerSize,
    bottomLeft: CornerSize
  ): SmoothCornerShape = SmoothCornerShape(topLeft, topRight, bottomRight, bottomLeft)
}


/** 创建一个四个角大小相同的平滑圆角矩形形状 */
fun SmoothCornerShape(size: SizeUnit) = SmoothCornerShape(size, size, size, size)

/** 创建一个四个角 [CornerSize] 相同的平滑圆角矩形形状 */
fun SmoothCornerShape(size: CornerSize) = SmoothCornerShape(size, size, size, size)

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的平滑圆角矩形形状
 * @param percent 角大小占控件大小的 1/x
 */
fun SmoothCornerShape(@FloatRange(from = .0, to = 1.0) percent: Float) =
  SmoothCornerShape(CornerSize(percent = percent))

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的平滑圆角矩形形状
 * @param percent 角大小占控件大小的百分之几
 */
fun SmoothCornerShape(@IntRange(from = 0, to = 100) percent: Int) =
  SmoothCornerShape(CornerSize(percent))

/** 创建一个可单独定义每个角大小的平滑圆角矩形形状 */
fun SmoothCornerShape(
  topLeft: SizeUnit = SizeUnit.Unspecified,
  topRight: SizeUnit = SizeUnit.Unspecified,
  bottomRight: SizeUnit = SizeUnit.Unspecified,
  bottomLeft: SizeUnit = SizeUnit.Unspecified,
) = SmoothCornerShape(
  topLeft = CornerSize(topLeft),
  topRight = CornerSize(topRight),
  bottomLeft = CornerSize(bottomLeft),
  bottomRight = CornerSize(bottomRight),
)

/**
 * 创建一个可单独定义每个角大小的平滑圆角矩形形状
 * 根据控件大小的百分比来决定平滑圆角大小
 */
fun SmoothCornerShape(
  @IntRange(from = 0, to = 100) topLeftPercent: Int = 0,
  @IntRange(from = 0, to = 100) topRightPercent: Int = 0,
  @IntRange(from = 0, to = 100) bottomRightPercent: Int = 0,
  @IntRange(from = 0, to = 100) bottomLeftPercent: Int = 0
) = SmoothCornerShape(
  CornerSize(percent = topLeftPercent),
  CornerSize(percent = topRightPercent),
  CornerSize(percent = bottomRightPercent),
  CornerSize(percent = bottomLeftPercent),
)

/**
 * 创建一个可单独定义每个角大小的平滑圆角矩形形状
 * 根据控件大小的 1/x 几来决定平滑圆角大小
 */
fun SmoothCornerShape(
  @FloatRange(from = .0, to = 1.0) topLeftPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) topRightPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) bottomRightPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) bottomLeftPercent: Float = 0f
) = SmoothCornerShape(
  CornerSize(percent = topLeftPercent),
  CornerSize(percent = topRightPercent),
  CornerSize(percent = bottomRightPercent),
  CornerSize(percent = bottomLeftPercent),
)