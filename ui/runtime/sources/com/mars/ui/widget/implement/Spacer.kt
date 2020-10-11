@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.mars.ui.widget.implement

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.Space
import com.mars.ui.UiKitMarker

/*
 * author: 凛
 * date: 2020/8/8 9:59 PM
 * github: https://github.com/oh-Rin
 * description: 定义一个用来占位的空白空间
 */
@UiKitMarker class Spacer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
  var enabledDraw = false

  /**
   * 只有当 [enabledDraw] 开启后才绘制
   */
  override fun draw(canvas: Canvas) {
    if (enabledDraw) super.draw(canvas)
  }

  /** See also [Space] */
  private fun getDefaultSize2(size: Int, measureSpec: Int): Int {
    var result = size
    val specMode = MeasureSpec.getMode(measureSpec)
    val specSize = MeasureSpec.getSize(measureSpec)
    when (specMode) {
      MeasureSpec.UNSPECIFIED -> result = size
      MeasureSpec.AT_MOST -> result = size.coerceAtMost(specSize)
      MeasureSpec.EXACTLY -> result = specSize
    }
    return result
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    setMeasuredDimension(
      getDefaultSize2(suggestedMinimumWidth, widthMeasureSpec),
      getDefaultSize2(suggestedMinimumHeight, heightMeasureSpec)
    )
  }
}