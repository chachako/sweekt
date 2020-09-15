@file:Suppress("FunctionName")

package com.mars.ui.core.graphics.shape

import androidx.annotation.FloatRange
import com.google.android.material.shape.AbsoluteCornerSize
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.RelativeCornerSize
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toPxOrNull

/*
 * author: 凛
 * date: 2020/8/9 11:50 AM
 * github: https://github.com/oh-Rin
 * description: 角落
 */
data class Corner(
  /** 角大小 */
  val size: CornerSize,
  /** [CornerFamily.ROUNDED] [CornerFamily.CUT] */
  @CornerFamily val family: Int = CornerFamily.ROUNDED,
)

/** 创建一个以像素为单位定义的角的大小 */
fun CornerSize(size: SizeUnit): CornerSize = AbsoluteCornerSize(size.toPxOrNull() ?: 0f)


/** 创建一个以控件大小为单位的百分比大小的角*/
fun CornerPercent(@FloatRange(from = 0.0, to = 1.0) percent: Float): CornerSize =
  RelativeCornerSize(percent)

val ZeroCornerSize = AbsoluteCornerSize(0f)