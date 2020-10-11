@file:Suppress("EXPERIMENTAL_FEATURE_WARNING", "FunctionName")

package com.mars.ui.extension.tab.implement

import android.graphics.Canvas
import android.graphics.Paint

/*
 * author: 凛
 * date: 2020/9/1 11:02 PM
 * github: https://github.com/oh-Rin
 * description: 定义一个渲染器来提供真正的 TabIndicator
 */
inline class TabIndicatorRenderer(val render: Canvas.(Paint, Float, Float, Float, Float, Float) -> Unit) {
  companion object {
    /**
     * 渲染一个外观为四方形的指示器
     */
    val Rectangle by lazy {
      Render { paint, start, end, top, bottom, _ ->
        drawRect(start, top, end, bottom, paint)
      }
    }

    /**
     * 渲染一个左右两边都是圆角的矩形指示器
     */
    val Rounded by lazy {
      Render { paint, start, end, top, bottom, _ ->
        val rounded = (end - start) / 2
        drawRoundRect(
          start, top, end, bottom,
          rounded, rounded,
          paint
        )
      }
    }
  }
}

fun Render(
  render: Canvas.(
    paint: Paint, // 渲染画笔
    start: Float,
    end: Float,
    top: Float,
    bottom: Float,
    offset: Float // 滑动偏移量
  ) -> Unit
): TabIndicatorRenderer = TabIndicatorRenderer(render)