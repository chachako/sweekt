@file:Suppress("FunctionName", "SpellCheckingInspection")

package com.mars.ui.core.graphics.shape

import android.graphics.RectF
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath
import com.mars.toolkit.float
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.theme.Shapes
import com.mars.ui.theme.Shapes.Companion.resolveShape

/*
 * author: 凛
 * date: 2020/8/9 12:06 PM
 * github: https://github.com/oh-Rin
 * description: 四边为平滑圆角的形状 https://www.zhihu.com/question/21191767
 */
data class SmoothCornerShape(
  val topLeft: CornerSize = ZeroCornerSize,
  val topRight: CornerSize = ZeroCornerSize,
  val bottomRight: CornerSize = ZeroCornerSize,
  val bottomLeft: CornerSize = ZeroCornerSize,
) : Shape {
  /** [Shapes.resolveShape] */
  override var id: Int = -1
  override fun toModelBuilder(): ShapeAppearanceModel.Builder = ShapeAppearanceModel.builder()
    .setAllCorners(SmoothCornerTreatment())
    .setTopLeftCornerSize(topLeft)
    .setTopRightCornerSize(topRight)
    .setBottomRightCornerSize(bottomRight)
    .setBottomLeftCornerSize(bottomLeft)

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }
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
  SmoothCornerShape(CornerPercent(percent))

/**
 * 创建一个以控件大小为单位
 * 四个角相同百分比的平滑圆角矩形形状
 * @param percent 角大小占控件大小的百分之几
 */
fun SmoothCornerShape(@IntRange(from = 0, to = 100) percent: Int) =
  SmoothCornerShape(percent = percent * 0.01f)

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
  topLeftPercent = topLeftPercent.float,
  topRightPercent = topRightPercent.float,
  bottomRightPercent = bottomRightPercent.float,
  bottomLeftPercent = bottomLeftPercent.float,
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
  CornerPercent(topLeftPercent),
  CornerPercent(topRightPercent),
  CornerPercent(bottomRightPercent),
  CornerPercent(bottomLeftPercent),
)

/*
 * 画每一个平滑圆角的实现
 * from: https://github.com/MartinRGB/sketch-smooth-corner-android/blob/master/app/src/main/java/com/example/martinrgb/roundconrner/smoothcorners/SketchSmoothCorners.java
 */
class SmoothCornerTreatment : CornerTreatment() {
  override fun getCornerPath(
    shapePath: ShapePath,
    angle: Float,
    interpolation: Float,
    bounds: RectF,
    size: CornerSize
  ) {
    val radius = size.getCornerSize(bounds)
    val width = bounds.width()
    val height = bounds.height()
    val centerX = bounds.centerX()
    val centerY = bounds.centerY()

    // 半径大于或者等于高或者宽的一半时则使用原来的圆角路径
    if (radius >= width.coerceAtMost(height) / 2) {
      shapePath.reset(0f, radius * interpolation, 180F, 180 - angle)
      shapePath.addArc(0f, 0f, 2 * radius * interpolation, 2 * radius * interpolation, 180f, angle)
      return
    }

    val posX = centerX - width / 2
    val posY = centerY - height / 2

    val vertexRatio = if (radius / (width / 2).coerceAtMost(height / 2) > 0.5) {
      val percentage = (radius / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
      val clampedPer = 1f.coerceAtMost(percentage)
      1f - (1 - 1.104f / 1.2819f) * clampedPer
    } else {
      1f
    }

    val controlRatio = if (radius / (width / 2).coerceAtMost(height / 2) > 0.6) {
      val percentage = (radius / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
      val clampedPer = 1f.coerceAtMost(percentage)
      1 + (0.8717f / 0.8362f - 1) * clampedPer
    } else {
      1f
    }

    shapePath.reset(0f, 0f)

    shapePath.lineTo(posX, posY + (height / 2).coerceAtMost(radius / 100f * 128.19f * vertexRatio))
    shapePath.cubicToPoint(
      posX,
      posY + radius / 100f * 83.62f * controlRatio,
      posX + radius / 100f * 4.64f,
      posY + radius / 100f * 67.45f,
      posX + radius / 100f * 13.36f,
      posY + radius / 100f * 51.16f
    )
    shapePath.cubicToPoint(
      posX + radius / 100f * 22.07f,
      posY + radius / 100f * 34.86f,
      posX + radius / 100f * 34.86f,
      posY + radius / 100f * 22.07f,
      posX + radius / 100f * 51.16f,
      posY + radius / 100f * 13.36f
    )
    shapePath.cubicToPoint(
      posX + radius / 100f * 67.45f,
      posY + radius / 100f * 4.64f,
      posX + radius / 100f * 83.62f * controlRatio,
      posY,
      posX + (width / 2).coerceAtMost(radius / 100f * 128.19f * vertexRatio),
      posY
    )
  }
}
