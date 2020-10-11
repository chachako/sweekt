@file:Suppress("FunctionName")

package com.mars.ui.core.graphics.shape

import android.graphics.RectF
import com.mars.ui.core.graphics.Outline
import com.mars.ui.core.graphics.createOutline
import com.mars.ui.theme.Shapes
import com.mars.ui.theme.Shapes.Companion.resolveShape

/*
 * author: 凛
 * date: 2020/9/27 01:33 AM
 * github: https://github.com/oh-Rin
 * description: 椭圆形
 */
class EllipseShape : Shape {
  /** [Shapes.resolveShape] */
  override var id: Int = -1

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = EllipseShape().also { it.id = id }

  override fun getOutline(bounds: RectF): Outline = createOutline { setOval(bounds) }
}

/** 缓存一个椭圆形 */
val OvalShape by lazy { EllipseShape() }