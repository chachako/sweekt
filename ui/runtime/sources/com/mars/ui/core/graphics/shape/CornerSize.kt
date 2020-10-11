@file:Suppress("FunctionName")

package com.mars.ui.core.graphics.shape

import android.graphics.RectF
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.mars.toolkit.float
import com.mars.ui.core.graphics.geometry.minSize
import com.mars.ui.core.unit.Px
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toPx
import com.mars.ui.core.unit.useOrElse

/*
 * author: 凛
 * date: 2020/8/9 11:50 AM
 * github: https://github.com/oh-Rin
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