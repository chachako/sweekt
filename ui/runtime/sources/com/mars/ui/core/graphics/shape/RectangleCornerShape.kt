@file:Suppress("FunctionName")

package com.mars.ui.core.graphics.shape

import android.graphics.RectF
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath
import com.mars.ui.theme.Shapes
import com.mars.ui.theme.Shapes.Companion.resolveShape

/*
 * author: 凛
 * date: 2020/8/9 12:06 PM
 * github: https://github.com/oh-Rin
 * description: 四边为直角的形状
 */
class RectangleCornerShape : Shape {
  /** [Shapes.resolveShape] */
  override var id: Int = -1

  override fun toModelBuilder(): ShapeAppearanceModel.Builder = ShapeAppearanceModel.builder()
    .setAllCorners(RectangleCornerTreatment())

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = RectangleCornerShape().also { it.id = id }
}

class RectangleCornerTreatment : CornerTreatment() {
  override fun getCornerPath(
    shapePath: ShapePath,
    angle: Float,
    interpolation: Float,
    bounds: RectF,
    size: CornerSize
  ) {
    shapePath.reset(0f, 0f)
  }
}

/**
 * 直角形状
 * 当所有圆角大小为 0 时即是一个长/正方形
 */
val RectangleShape by lazy { RectangleCornerShape() }