package com.mars.ui.foundation

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.mars.ui.Theme
import com.mars.ui.UiKit
import com.mars.ui.UiKitMarker
import com.mars.ui.core.Foreground
import com.mars.ui.core.Modifier
import com.mars.ui.core.ModifierManager
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.foundation.modifies.BlurEffect
import com.mars.ui.util.BlurHelper

/*
 * author: 凛
 * date: 2020/8/12 2:34 PM
 * github: https://github.com/oh-Rin
 * description: View 捕捉器，当所有子 View 添加时都进行捕获处理
 */
interface ViewCatcher {
  /** 捕获所有在范围内添加的 [View] */
  val captured: MutableList<View>

  // 开始捕获 View
  fun startCapture()

  // 结束捕获 View
  fun endCapture()
}

/*
 * author: 凛
 * date: 2020/8/12 2:34 PM
 * github: https://github.com/oh-Rin
 * description: 以控件形成一个布局范围
 */
@UiKitMarker
abstract class LayoutScope @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes),
  Theme.User,
  ViewCatcher,
  BlurEffect,
  Foreground,
  UiKit {
  override var foregroundSupport: Drawable? = null

  override var blurHelper: BlurHelper? = null

  override val captured = mutableListOf<View>()

  private var capture = false

  open var modifier: Modifier = Modifier
    set(value) {
      if (field == value) return
      field = value
      modifier.apply { realize(parent as? ViewGroup) }
    }

  override fun startCapture() {
    capture = true
  }

  override fun endCapture() {
    capture = false
    captured.clear()
  }

  override fun addView(child: View?, index: Int, params: LayoutParams?) {
    if (capture && child != null) captured.add(child)
    super.addView(child, index, params)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    if (capture && child != null) captured.add(child)
    return super.addViewInLayout(child, index, params, preventRequestLayout)
  }

  override fun dispatchDraw(canvas: Canvas) {
    when {
      // 渲染途中不要渲染自身和子视图
      blurHelper == null || !blurHelper!!.isRendering -> super.dispatchDraw(canvas)
      else -> blurHelper!!.drawBlur(canvas)
    }
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    blurHelper?.attach()
  }

  override fun onDetachedFromWindow() {
    blurHelper?.detach()
    super.onDetachedFromWindow()
  }

  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(parent as? ViewGroup) }
    }
  }
}