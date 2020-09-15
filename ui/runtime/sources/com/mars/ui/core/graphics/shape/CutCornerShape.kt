@file:Suppress("FunctionName")

package com.mars.ui.core.graphics.shape

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.mars.toolkit.float
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.theme.Shapes
import com.mars.ui.theme.Shapes.Companion.resolveShape

/*
 * author: 凛
 * date: 2020/8/9 12:06 PM
 * github: https://github.com/oh-Rin
 * description: 四边为切角的形状
 */
data class CutCornerShape(
  override val topLeft: CornerSize = ZeroCornerSize,
  override val topRight: CornerSize = ZeroCornerSize,
  override val bottomRight: CornerSize = ZeroCornerSize,
  override val bottomLeft: CornerSize = ZeroCornerSize,
) : CornerBasedShape {
  /** [Shapes.resolveShape] */
  override var id: Int = -1
  override val family: Int = CornerFamily.CUT

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }
}

/** 创建一个四个角大小相同的切角矩形形状 */
fun CutCornerShape(size: SizeUnit) = CutCornerShape(size, size, size, size)

/** 创建一个四个角 [CornerSize] 相同的切角矩形形状 */
fun CutCornerShape(size: CornerSize) = CutCornerShape(size, size, size, size)

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的切角矩形形状
 * @param percent 角大小占控件大小的 1/x
 */
fun CutCornerShape(@FloatRange(from = .0, to = 1.0) percent: Float) =
  CutCornerShape(CornerPercent(percent))

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的切角矩形形状
 * @param percent 角大小占控件大小的百分之几
 */
fun CutCornerShape(@IntRange(from = 0, to = 100) percent: Int) =
  CutCornerShape(percent.float)

/** 创建一个可单独定义每个角大小的切角矩形形状 */
fun CutCornerShape(
  topLeft: SizeUnit = SizeUnit.Unspecified,
  topRight: SizeUnit = SizeUnit.Unspecified,
  bottomRight: SizeUnit = SizeUnit.Unspecified,
  bottomLeft: SizeUnit = SizeUnit.Unspecified,
) = CutCornerShape(
  topLeft = CornerSize(topLeft),
  topRight = CornerSize(topRight),
  bottomLeft = CornerSize(bottomLeft),
  bottomRight = CornerSize(bottomRight),
)

/**
 * 创建一个可单独定义每个角大小的切角矩形形状
 * 根据控件大小的百分比来决定切角大小
 */
fun CutCornerShape(
  @IntRange(from = 0, to = 100) topLeftPercent: Int = 0,
  @IntRange(from = 0, to = 100) topRightPercent: Int = 0,
  @IntRange(from = 0, to = 100) bottomRightPercent: Int = 0,
  @IntRange(from = 0, to = 100) bottomLeftPercent: Int = 0
) = CutCornerShape(
  topLeftPercent = topLeftPercent.float,
  topRightPercent = topRightPercent.float,
  bottomRightPercent = bottomRightPercent.float,
  bottomLeftPercent = bottomLeftPercent.float,
)

/**
 * 创建一个可单独定义每个角大小的切角矩形形状
 * 根据控件大小的 1/x 几来决定切角大小
 */
fun CutCornerShape(
  @FloatRange(from = .0, to = 1.0) topLeftPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) topRightPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) bottomRightPercent: Float = 0f,
  @FloatRange(from = .0, to = 1.0) bottomLeftPercent: Float = 0f
) = CutCornerShape(
  CornerPercent(topLeftPercent),
  CornerPercent(topRightPercent),
  CornerPercent(bottomRightPercent),
  CornerPercent(bottomLeftPercent),
)

/**
 * 菱形形状
 * 四个角每个角各占控件大小的百分之五十
 */
val DiamondShape by lazy { RoundedCornerShape(percent = 50) }
