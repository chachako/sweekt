@file:Suppress("FunctionName", "OverridingDeprecatedMember")

package com.mars.ui.widget.implement

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.mars.toolkit.widget.FrameLayoutParams
import com.mars.ui.Theme
import com.mars.ui.Ui
import com.mars.ui.UiKitMarker
import com.mars.ui.core.Alignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.ModifierManager
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.decoupling.*
import com.mars.ui.realParent

/*
 * author: 凛
 * date: 2020/8/8 8:30 PM
 * github: https://github.com/oh-Rin
 * description: 盒子布局的扩展
 */
@UiKitMarker open class Box @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes),
  Theme.User,
  ViewCatcher,
  ViewCanvasProvider,
  LayoutCanvasProvider,
  ForegroundProvider,
  ModifierProvider,
  Ui {

  /** 调整子控件在此帧布局中的对齐 */
  fun Modifier.align(alignment: Alignment) =
    +ChildrenModifier(alignment)

  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(realParent) }
    }
  }

  /** 帧布局参数的调整实现 [FrameLayout.LayoutParams] */
  private data class ChildrenModifier(
    val _alignment: Alignment? = null,
  ) : Modifier {
    override fun View.realize(parent: ViewGroup?) {
      layoutParams = FrameLayoutParams {
        _alignment?.gravity?.also { gravity = it }
      }
    }
  }


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var captured: ArrayDeque<View>? = null
  override var foregroundSupport: Drawable? = null
  override var beforeDispatchDrawCallbacks: ArrayDeque<DrawEventWithValue>? = null
  override var afterDispatchDrawCallbacks: ArrayDeque<DrawEvent>? = null
  override var beforeDrawCallbacks: ArrayDeque<DrawEventWithValue>? = null
  override var afterDrawCallbacks: ArrayDeque<DrawEvent>? = null
  override var beforeDrawChildCallbacks: ArrayDeque<DrawChildEventWithValue>? = null
  override var afterDrawChildCallbacks: ArrayDeque<DrawChildEvent>? = null
  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(realParent) }
    }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    captureChildView(child)
    super.addView(child, index, params)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: ViewGroup.LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    captureChildView(child)
    return super.addViewInLayout(child, index, params, preventRequestLayout)
  }

  override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
    var intercepted: Boolean? = null

    // 执行调用前的回调
    beforeDrawChildCallbacks?.forEach {
      val result = it.invoke(canvas, child, drawingTime)
      if (result != null) intercepted = result
    }

    // 如果上一个回调返回的值为 null 则代表不拦截 super()
    val result = if (intercepted == null) {
      super.drawChild(canvas, child, drawingTime)
    } else {
      intercepted!!
    }

    // 执行调用后的回调
    afterDrawChildCallbacks?.forEach {
      it.invoke(canvas, child, drawingTime)
    }

    return result
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