package com.mars.ui.core.graphics

import android.graphics.Bitmap
import androidx.annotation.FloatRange
import com.mars.ui.core.unit.SizeUnit

/*
 * author: 凛
 * date: 2020/9/28 下午5:58
 * github: https://github.com/oh-Rin
 * description: 定义一个阴影数据
 */
data class Shadow(
  val color: Color,
  val y: SizeUnit,
  val x: SizeUnit,
  val spread: SizeUnit,
  @FloatRange(from = .0, to = 1.0) val alpha: Float,
) {
  constructor(
    color: Color,
    spread: SizeUnit,
    y: SizeUnit,
    x: SizeUnit,
    blur: Float,
    @FloatRange(from = .0, to = 1.0) alpha: Float,
  ) : this(color, y, x, spread, alpha) {
    this.blur = blur
  }

  /** 阴影半径 */
  @FloatRange(from = .0, to = 1.0) var blur: Float = DefaultBlur

  /** 渲染后的阴影位图 */
  var bitmap: Bitmap? = null

  companion object {
    const val DefaultBlur = 0.4f
  }
}