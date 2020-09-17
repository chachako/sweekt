@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.mars.ui.foundation

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import com.mars.ui.Theme
import com.mars.ui.UiKit
import com.mars.ui.UiKitMarker
import com.mars.ui.asLayout
import com.mars.ui.core.Foreground
import com.mars.ui.core.Modifier
import com.mars.ui.core.ModifierManager
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.unit.Px
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.foundation.modifies.BlurEffect
import com.mars.ui.foundation.modifies.height
import com.mars.ui.foundation.modifies.size
import com.mars.ui.foundation.modifies.width
import com.mars.ui.util.BlurHelper

/*
 * author: 凛
 * date: 2020/8/8 9:59 PM
 * github: https://github.com/oh-Rin
 * description: 分频器/分割线
 */
@UiKitMarker class Spacer @JvmOverloads constructor(
  context: Context?,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes),
  Theme.User,
  BlurEffect,
  Foreground,
  ModifierProvider,
  UiKit {
  override var foregroundSupport: Drawable? = null

  override var blurHelper: BlurHelper? = null

  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(parent as? ViewGroup) }
    }

  var enabledDraw = false

  /**
   * 只有当 [enabledDraw] 开启后才绘制
   */
  override fun draw(canvas: Canvas?) {
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

  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(parent as? ViewGroup) }
    }
  }
}

/**
 * 创建一个任意大小的空白视图
 * NOTE: [Spacer.draw] 不会进行任何绘制
 * @param width
 * @param height
 */
fun UiKit.Spacer(
  width: SizeUnit? = null,
  height: SizeUnit? = null,
): Spacer = With(::Spacer) {
  when {
    width != null && height != null -> Modifier.size(width, height)
    width != null && height == null -> Modifier.width(width)
    width == null && height != null -> Modifier.height(height)
    else -> null
  }?.apply { it.realize(this@Spacer.asLayout) }
}

/**
 * 创建一个正方形的空白视图
 * NOTE: [Spacer.draw] 不会进行任何绘制
 * @param size 高宽大小
 */
fun UiKit.Spacer(
  size: SizeUnit,
): Spacer = With(::Spacer) {
  Modifier.size(size).apply { it.realize(this@Spacer.asLayout) }
}

/**
 * 创建一个拥有完整功能的空间视图
 * NOTE: [Spacer.draw] 会进行绘制，并且取决于 [modifier]
 * @param modifier 高宽大小
 */
fun UiKit.Spacer(
  modifier: Modifier
): Spacer = With(::Spacer) {
  it.modifier = modifier
  it.enabledDraw = true
  modifier.apply { it.realize(this@Spacer.asLayout) }
}

/**
 * 以剩余的空间创建一个最大宽度的空白视图以划分两个区域
 * RESULT: ————————————————————————————————
 *        | View1 View2 ---Spacer--- View3 |
 *         ————————————————————————————————
 */
fun Row.Spacer(): Spacer = With(::Spacer) {
  Modifier.width(Px.Zero).weight(1).apply { it.realize(this@Spacer) }
}

/**
 * 以剩余的空间创建一个最大高度的空白视图以划分两个区域
 * RESULT: ———————
 *        | View1 |
 *        | View2 |
 *        |   |   |
 *        |   |   |
 *        | Spacer|
 *        |   |   |
 *        |   |   |
 *        | View3 |
 *         ———————
 */
fun Column.Spacer(): Spacer = With(::Spacer) {
  Modifier.height(Px.Zero).weight(1).apply { it.realize(this@Spacer) }
}