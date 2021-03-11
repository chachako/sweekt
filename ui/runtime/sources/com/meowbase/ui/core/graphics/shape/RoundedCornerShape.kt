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

@file:Suppress("FunctionName")

package com.meowbase.ui.core.graphics.shape

import android.graphics.Path
import android.graphics.RectF
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.meowbase.ui.core.graphics.createOutline
import com.meowbase.ui.core.graphics.geometry.*
import com.meowbase.ui.core.unit.SizeUnit

/*
 * author: 凛
 * date: 2020/8/9 12:06 PM
 * github: https://github.com/RinOrz
 * description: 四边为圆角的形状
 */
open class RoundedCornerShape(
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
  ) = when {
    topLeft + topRight + bottomLeft + bottomRight == 0f -> {
      RectangleShape.getOutline(bounds, topLeft, topRight, bottomRight, bottomLeft)
    }
    else -> createOutline {
      val roundRect = RoundRect(
        rectF = bounds,
        topLeft = topLeft.toCornerRadius(),
        topRight = topRight.toCornerRadius(),
        bottomRight = bottomRight.toCornerRadius(),
        bottomLeft = bottomLeft.toCornerRadius()
      )
      if (roundRect.isSimple) {
        setRoundRect(
          roundRect.left,
          roundRect.top,
          roundRect.right,
          roundRect.bottom,
          topLeft
        )
      } else {
        path = Path().apply { addRoundRect(roundRect) }
      }
    }
  }

  override fun copy(
    topLeft: CornerSize,
    topRight: CornerSize,
    bottomRight: CornerSize,
    bottomLeft: CornerSize
  ): RoundedCornerShape = RoundedCornerShape(topLeft, topRight, bottomRight, bottomLeft)
}

/** 创建一个四个角大小相同的圆角矩形形状 */
fun RoundedCornerShape(size: SizeUnit) = RoundedCornerShape(size, size, size, size)

/** 创建一个四个角 [CornerSize] 相同的圆角矩形形状 */
fun RoundedCornerShape(size: CornerSize) = RoundedCornerShape(size, size, size, size)

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的圆角矩形形状
 * @param percent 角大小占控件大小的 1/x
 */
fun RoundedCornerShape(@FloatRange(from = .0, to = 1.0) percent: Float) =
  RoundedCornerShape(CornerSize(percent = percent))

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的圆角矩形形状
 * @param percent 角大小占控件大小的百分之几
 */
fun RoundedCornerShape(@IntRange(from = 0, to = 100) percent: Int) =
  RoundedCornerShape(CornerSize(percent))

/** 创建一个可单独定义每个角大小的圆角矩形形状 */
fun RoundedCornerShape(
  topLeft: SizeUnit = SizeUnit.Unspecified,
  topRight: SizeUnit = SizeUnit.Unspecified,
  bottomRight: SizeUnit = SizeUnit.Unspecified,
  bottomLeft: SizeUnit = SizeUnit.Unspecified,
) = RoundedCornerShape(
  topLeft = CornerSize(topLeft),
  topRight = CornerSize(topRight),
  bottomLeft = CornerSize(bottomLeft),
  bottomRight = CornerSize(bottomRight),
)

/**
 * 创建一个可单独定义每个角大小的圆角矩形形状
 * 根据控件大小的百分比来决定圆角大小
 */
fun RoundedCornerShape(
  @IntRange(from = 0, to = 100) topLeftPercent: Int = 0,
  @IntRange(from = 0, to = 100) topRightPercent: Int = 0,
  @IntRange(from = 0, to = 100) bottomRightPercent: Int = 0,
  @IntRange(from = 0, to = 100) bottomLeftPercent: Int = 0
) = RoundedCornerShape(
  CornerSize(percent = topLeftPercent),
  CornerSize(percent = topRightPercent),
  CornerSize(percent = bottomRightPercent),
  CornerSize(percent = bottomLeftPercent),
)

/**
 * 创建一个可单独定义每个角大小的圆角矩形形状
 * 根据控件大小的 1/x 几来决定圆角大小
 */
fun RoundedCornerShape(
  @FloatRange(from = .0, to = 1.0) topLeftPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) topRightPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) bottomRightPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) bottomLeftPercent: Float = 0f
) = RoundedCornerShape(
  CornerSize(percent = topLeftPercent),
  CornerSize(percent = topRightPercent),
  CornerSize(percent = bottomRightPercent),
  CornerSize(percent = bottomLeftPercent),
)

/** 定义一个所有角都是圆形的形状 */
val CircleRoundedShape by lazy { RoundedCornerShape(percent = 50) }