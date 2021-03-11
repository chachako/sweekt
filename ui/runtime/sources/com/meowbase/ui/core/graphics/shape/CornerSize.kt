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

import android.graphics.RectF
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.meowbase.toolkit.float
import com.meowbase.ui.core.graphics.geometry.minSize
import com.meowbase.ui.core.unit.Px
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toPx
import com.meowbase.ui.core.unit.useOrElse

/*
 * author: 凛
 * date: 2020/8/9 11:50 AM
 * github: https://github.com/RinOrz
 * description: 定义一个角大小
 */
interface CornerSize {
  /**
   * 根据形状大小解析 [CornerSize] 为像素值
   *
   * @param shapeBounds 形状的矩形边缘
   */
  fun toPx(shapeBounds: RectF): Float
}


/** 创建一个以像素为单位定义的角的 [SizeUnit] 大小 */
fun CornerSize(size: SizeUnit): CornerSize = object : CornerSize {
  override fun toPx(shapeBounds: RectF) =
    size.useOrElse { Px.Zero }.toPx()
}


/**
 * 创建百分比大小的角
 * @param percent 将会以形状较小的一侧尺寸来决定此百分比所占的真实像素
 */
fun CornerSize(@IntRange(from = 0, to = 100) percent: Int): CornerSize =
  PercentCornerSize(percent.float)

/**
 * 创建百分比大小的角
 * @param percent 将会以形状较小的一侧尺寸来决定此百分比所占的真实像素
 */
fun CornerSize(@FloatRange(from = .0, to = 1.0) percent: Float): CornerSize =
  PercentCornerSize(percent * 100f)

/**
 * 创建百分比大小的角
 * @param percent 将会以形状较小的一侧尺寸来决定此百分比所占的真实像素
 */
private data class PercentCornerSize(private val percent: Float) : CornerSize {
  init {
    if (percent < 0 || percent > 100) {
      throw IllegalArgumentException("The percent should be in the range of [0, 100]")
    }
  }

  override fun toPx(shapeBounds: RectF) = shapeBounds.minSize * (percent / 100f)
}


/**
 * 创建一个总是为 0 的 [CornerSize]
 */
val ZeroCornerSize: CornerSize = object : CornerSize {
  override fun toPx(shapeBounds: RectF) = 0.0f
}