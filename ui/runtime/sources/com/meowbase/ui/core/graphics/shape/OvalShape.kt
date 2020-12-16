@file:Suppress("FunctionName")

package com.meowbase.ui.core.graphics.shape

import android.graphics.RectF
import com.meowbase.ui.core.graphics.Outline
import com.meowbase.ui.core.graphics.createOutline
import com.meowbase.ui.theme.Shapes
import com.meowbase.ui.theme.Shapes.Companion.resolveShape

/*
 * author: 凛
 * date: 2020/9/27 01:33 AM
 * github: https://github.com/RinOrz
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