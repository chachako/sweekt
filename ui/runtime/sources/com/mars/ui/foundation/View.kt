@file:Suppress("FunctionName", "NestedLambdaShadowedImplicitParameter")

package com.mars.ui.foundation

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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
import android.view.View as AndroidView

/*
 * author: 凛
 * date: 2020/8/18 2:50 PM
 * github: https://github.com/oh-Rin
 * description: View 的扩展
 */
@UiKitMarker open class View @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : AndroidView(context, attrs, defStyleAttr, defStyleRes),
  Theme.User,
  BlurEffect,
  Foreground,
  ModifierProvider,
  UiKit {
  override var foregroundSupport: Drawable? = null

  override var blurHelper: BlurHelper? = null

  override var modifier: Modifier? = null
    set(value) {
      field = value
      modifier?.realize(this, parent as? ViewGroup)
    }

  override fun onDraw(canvas: Canvas) {
    blurHelper?.drawBlur(canvas)
    // 渲染途中不要渲染自身
    if (blurHelper == null || !blurHelper!!.isRendering) super.onDraw(canvas)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    blurHelper?.attach()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    blurHelper?.detach()
  }

  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.update(this, parent as? ViewGroup)
    }
  }
}