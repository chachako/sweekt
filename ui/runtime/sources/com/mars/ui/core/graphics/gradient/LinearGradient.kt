@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.mars.ui.core.graphics.gradient

import android.graphics.LinearGradient
import android.view.View
import androidx.annotation.FloatRange
import com.mars.toolkit.float
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.TileMode
import com.mars.ui.core.graphics.toNativeTileMode


/**
 * 线性渐变
 * @property angle 渐变角度 [resolveGradientAngle]
 * @property colors 多个颜色组成渐变，如 arrayOf(Color.Blue, Color.Red, Color.Green)
 * @property positions 每个渐变颜色的对应位置（需要和渐变色的数量一致），如 intArrayOf(0f, 0.8f, 1f)
 * @property tile 颜色平铺模式
 */
data class LinearGradient(
  val colors: Array<Color>,
  val angle: GradientAngle = GradientAngle.LeftRight,
  @FloatRange(from = 0.0, to = 1.0) val positions: FloatArray? = floatArrayOf(0f, 1f),
  val tile: TileMode = TileMode.Clamp,
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as com.mars.ui.core.graphics.gradient.LinearGradient
    if (!colors.contentEquals(other.colors)) return false
    if (angle != other.angle) return false
    if (positions != null) {
      if (other.positions == null) return false
      if (!positions.contentEquals(other.positions)) return false
    } else if (other.positions != null) return false
    if (tile != other.tile) return false

    return true
  }

  override fun hashCode(): Int {
    var result = colors.contentHashCode()
    result = 31 * result + angle.hashCode()
    result = 31 * result + (positions?.contentHashCode() ?: 0)
    result = 31 * result + tile.hashCode()
    return result
  }
}


/**
 * 线性渐变
 * @param startX 渐变开始的 x 轴坐标
 * @param startY 渐变开始的 y 轴坐标
 * @param endX 渐变结束的 x 轴坐标
 * @param endY 渐变结束的 y 轴坐标
 * @param colors 多个颜色组成渐变，如 arrayOf(Color.Blue, Color.Red, Color.Green)
 * @param positions 每个渐变颜色的对应位置（需要和渐变色的数量一致），如 intArrayOf(0f, 0.8f, 1f)
 * @param tile 颜色平铺模式
 */
fun LinearGradient(
  startX: Number,
  startY: Number,
  endX: Number,
  endY: Number,
  colors: Array<Color>,
  @FloatRange(from = 0.0, to = 1.0) positions: FloatArray? = floatArrayOf(0f, 1f),
  tile: TileMode = TileMode.Clamp,
) = LinearGradient(
  startX.float, startY.float, endX.float, endY.float,
  colors.map { it.argb }.toTypedArray().toIntArray(), positions, tile.toNativeTileMode()
)

/**
 * 线性渐变
 * @param start 渐变开始的 x 坐标
 * @param end 渐变结束的 x 坐标
 * @param colors 多个颜色组成渐变，如 arrayOf(Color.Blue, Color.Red, Color.Green)
 * @param positions 每个渐变颜色的对应位置（需要和渐变色的数量一致），如 intArrayOf(0f, 0.8f, 1f)
 * @param tile 颜色平铺模式
 */
fun LinearGradient(
  start: Number,
  end: Number,
  colors: Array<Color>,
  @FloatRange(from = 0.0, to = 1.0) positions: FloatArray? = floatArrayOf(0f, 1f),
  tile: TileMode = TileMode.Clamp,
) = LinearGradient(start.float, 0f, end.float, 0f, colors, positions, tile)

/**
 * 线性渐变
 * @param view 视图大小基准
 * @param angle 渐变角度
 * @param colors 多个颜色组成渐变，如 arrayOf(Color.Blue, Color.Red, Color.Green)
 * @param positions 每个渐变颜色的对应位置（需要和渐变色的数量一致），如 intArrayOf(0f, 0.8f, 1f)
 * @param tile 颜色平铺模式
 */
fun LinearGradient(
  view: View,
  colors: Array<Color>,
  @FloatRange(from = 0.0, to = 1.0) positions: FloatArray? = floatArrayOf(0f, 1f),
  angle: GradientAngle = GradientAngle.LeftRight,
  tile: TileMode = TileMode.Clamp,
) = LinearGradient(view.left, view.top, view.right, view.bottom, colors, positions, angle, tile)

/**
 * 线性渐变
 * @param left 视图的左侧基准
 * @param top 视图的顶部基准
 * @param right 视图的右侧基准
 * @param bottom 视图的底部基准
 * @param angle 渐变角度
 * @param colors 多个颜色组成渐变，如 arrayOf(Color.Blue, Color.Red, Color.Green)
 * @param positions 每个渐变颜色的对应位置（需要和渐变色的数量一致），如 intArrayOf(0f, 0.8f, 1f)
 * @param tile 颜色平铺模式
 */
fun LinearGradient(
  left: Number,
  top: Number,
  right: Number,
  bottom: Number,
  colors: Array<Color>,
  @FloatRange(from = 0.0, to = 1.0) positions: FloatArray? = floatArrayOf(0f, 1f),
  angle: GradientAngle = GradientAngle.LeftRight,
  tile: TileMode = TileMode.Clamp,
) = resolveGradientAngle(angle, left.float, top.float, right.float, bottom.float).run {
  LinearGradient(
    startX.float, startY.float, endX.float, endY.float,
    colors.map { it.argb }.toTypedArray().toIntArray(), positions, tile.toNativeTileMode()
  )
}

/** 渐变角度 */
enum class GradientAngle {
  /** 从左到右 */
  LeftRight,
  LR,

  /** 从右到左 */
  RightLeft,
  RL,

  /** 从上到下 */
  TopBottom,
  TB,

  /** 从下到上 */
  BottomTop,
  BT,

  /** 从左上角到右下角 */
  TopLeftToBottomRight,
  TLBR,

  /** 从右上角到左下角 */
  TopRightToBottomLeft,
  TRBL,

  /** 从右下角到左上角 */
  BottomRightToTopLeft,
  BRTL,

  /** 从左下角到右上角 */
  BottomLeftToTopRight,
  BLTR,
}

internal class AngleCoordinate {
  var startX: Number = 0F
  var startY: Number = 0F
  var endX: Number = 0F
  var endY: Number = 0F
}

internal fun resolveGradientAngle(
  gradientAngle: GradientAngle,
  left: Float,
  top: Float,
  right: Float,
  bottom: Float,
) = AngleCoordinate().apply {
  when (gradientAngle) {
    GradientAngle.LeftRight, GradientAngle.LR -> {
      startX = left
      endX = right
    }
    GradientAngle.RightLeft, GradientAngle.RL -> {
      startX = right
      endX = left
    }
    GradientAngle.TopBottom, GradientAngle.TB -> {
      startY = top
      endY = bottom
    }
    GradientAngle.BottomTop, GradientAngle.BT -> {
      startY = bottom
      endY = top
    }
    GradientAngle.TopLeftToBottomRight, GradientAngle.TLBR -> {
      // left to right
      startX = left
      endX = right
      // top to bottom
      startY = top
      endY = bottom
    }
    GradientAngle.TopRightToBottomLeft, GradientAngle.TRBL -> {
      // right to left
      startX = right
      endX = left
      // top to bottom
      startY = top
      endY = bottom
    }
    GradientAngle.BottomLeftToTopRight, GradientAngle.BLTR -> {
      // left to right
      startX = left
      endX = right
      // bottom to top
      startY = bottom
      endY = top
    }
    GradientAngle.BottomRightToTopLeft, GradientAngle.BRTL -> {
      // right to left
      startX = right
      endX = left
      // bottom to top
      startY = bottom
      endY = top
    }
  }
}