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
 * description: 四边为圆角的形状
 */
data class RoundedCornerShape(
  override val topLeft: CornerSize = ZeroCornerSize,
  override val topRight: CornerSize = ZeroCornerSize,
  override val bottomRight: CornerSize = ZeroCornerSize,
  override val bottomLeft: CornerSize = ZeroCornerSize,
) : CornerBasedShape {
  /** [Shapes.resolveShape] */
  override var id: Int = -1
  override val family: Int = CornerFamily.ROUNDED

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }
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
  RoundedCornerShape(CornerPercent(percent))

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的圆角矩形形状
 * @param percent 角大小占控件大小的百分之几
 */
fun RoundedCornerShape(@IntRange(from = 0, to = 100) percent: Int) =
  RoundedCornerShape(percent.float)

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
  topLeftPercent = topLeftPercent.float,
  topRightPercent = topRightPercent.float,
  bottomRightPercent = bottomRightPercent.float,
  bottomLeftPercent = bottomLeftPercent.float,
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
  CornerPercent(topLeftPercent),
  CornerPercent(topRightPercent),
  CornerPercent(bottomRightPercent),
  CornerPercent(bottomLeftPercent),
)

/**
 * 横纵向完全圆的形状
 * 当四个角每个角各占控件大小的百分之五十时既是横纵向圆形的圆角
 */
val CircleShape by lazy { RoundedCornerShape(percent = 50) }