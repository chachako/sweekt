@file:Suppress("FunctionName")

package com.meowbase.ui.core.graphics.shape

import android.graphics.RectF
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.meowbase.toolkit.float
import com.meowbase.ui.core.graphics.Outline
import com.meowbase.ui.core.graphics.createOutlinePath
import com.meowbase.ui.core.unit.SizeUnit

/*
 * author: 凛
 * date: 2020/8/9 12:06 PM
 * github: https://github.com/RinOrz
 * description: 四边为切角的形状
 */
class CutCornerShape(
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
  ): Outline {
    val width = bounds.width().float
    val height = bounds.height().float
    return when (0f) {
      topLeft + topRight + bottomLeft + bottomRight -> {
        RectangleShape.getOutline(bounds, topLeft, topRight, bottomRight, bottomLeft)
      }
      else -> createOutlinePath {
        var cornerSize = topLeft
        moveTo(0f, cornerSize)
        lineTo(cornerSize, 0f)
        cornerSize = topRight
        lineTo(width - cornerSize, 0f)
        lineTo(width, cornerSize)
        cornerSize = bottomRight
        lineTo(width, height - cornerSize)
        lineTo(width - cornerSize, height)
        cornerSize = bottomLeft
        lineTo(cornerSize, height)
        lineTo(0f, height - cornerSize)
        close()
      }
    }
  }

  override fun copy(
    topLeft: CornerSize,
    topRight: CornerSize,
    bottomRight: CornerSize,
    bottomLeft: CornerSize
  ): CutCornerShape = CutCornerShape(
    topLeft = topLeft,
    topRight = topRight,
    bottomRight = bottomRight,
    bottomLeft = bottomLeft
  )
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
  CutCornerShape(CornerSize(percent = percent))

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的切角矩形形状
 * @param percent 角大小占控件大小的百分之几
 */
fun CutCornerShape(@IntRange(from = 0, to = 100) percent: Int) =
  CutCornerShape(CornerSize(percent = percent))

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
  CornerSize(percent = topLeftPercent),
  CornerSize(percent = topRightPercent),
  CornerSize(percent = bottomRightPercent),
  CornerSize(percent = bottomLeftPercent),
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
  CornerSize(percent = topLeftPercent),
  CornerSize(percent = topRightPercent),
  CornerSize(percent = bottomRightPercent),
  CornerSize(percent = bottomLeftPercent),
)

/**
 * 菱形形状
 * 四个角每个角各占控件大小的百分之五十
 */
val DiamondShape by lazy { CutCornerShape(percent = 50) }
