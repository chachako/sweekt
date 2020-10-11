@file:Suppress("FunctionName", "NestedLambdaShadowedImplicitParameter")

package com.mars.ui.widget.implement

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.mars.ui.Theme
import com.mars.ui.Ui
import com.mars.ui.UiKitMarker
import com.mars.ui.core.Modifier
import com.mars.ui.core.ModifierManager
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.decoupling.*
import com.mars.ui.realParent
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
  ViewCanvasProvider,
  ForegroundProvider,
  ModifierProvider,
  Ui {
  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(realParent) }
    }
  }


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var foregroundSupport: Drawable? = null
  override var beforeDispatchDrawCallbacks: ArrayDeque<DrawEventWithValue>? = null
  override var afterDispatchDrawCallbacks: ArrayDeque<DrawEvent>? = null
  override var beforeDrawCallbacks: ArrayDeque<DrawEventWithValue>? = null
  override var afterDrawCallbacks: ArrayDeque<DrawEvent>? = null
  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(realParent) }
    }

  override fun dispatchDraw(canvas: Canvas) {
    var intercepted = false

    // 执行调用前的回调
    beforeDispatchDrawCallbacks?.forEach {
      if (it.invoke(canvas)) intercepted = true
    }

    // 如果上一个回调返回的值为 false 则代表不拦截 super()
    if (!intercepted) super.dispatchDraw(canvas)

    // 执行调用后的回调
    afterDispatchDrawCallbacks?.forEach {
      it.invoke(canvas)
    }
  }

  override fun draw(canvas: Canvas) {
    var intercepted = false

    // 执行调用前的回调
    beforeDrawCallbacks?.forEach {
      if (it.invoke(canvas)) intercepted = true
    }

    // 如果上一个回调返回的值为 false 则代表不拦截 super()
    if (!intercepted) super.draw(canvas)

    // 执行调用后的回调
    afterDrawCallbacks?.forEach {
      it.invoke(canvas)
    }
  }
}