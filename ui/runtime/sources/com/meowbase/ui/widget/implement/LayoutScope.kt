@file:Suppress("MemberVisibilityCanBePrivate", "ViewConstructor")

package com.meowbase.ui.widget.implement

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.meowbase.ui.Theme
import com.meowbase.ui.Ui
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.ModifierManager
import com.meowbase.ui.core.UpdatableModifier
import com.meowbase.ui.core.decoupling.*
import com.meowbase.ui.uiParent


/*
 * author: 凛
 * date: 2020/8/12 2:34 PM
 * github: https://github.com/RinOrz
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
  ViewCanvasProvider,
  LayoutCanvasProvider,
  ForegroundProvider,
  ModifierProvider,
  Ui {
  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(uiParent) }
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
      modifier.apply { realize(uiParent) }
    }

  override fun addView(child: View?, index: Int, params: LayoutParams?) {
    captureChildView(child)
    super.addView(child, index, params)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: LayoutParams?,
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

/*
 * author: 凛
 * date: 2020/9/29 上午4:19
 * github: https://github.com/RinOrz
 * description: 定义一个假的布局范围
 * 大多时候用来让一些非 Ui.() -> Unit 的代码块内允许调用 UiKit 组件
 */
open class FakeLayoutScope(
  val realParent: ViewGroup,
  context: Context = realParent.context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : LayoutScope(context, attrs, defStyleAttr, defStyleRes) {
  init {
    giveRealParent()
  }

  /** 这是一个假 [ViewGroup]，所以无需重写 [onLayout] */
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

  override fun addView(child: View?, index: Int, params: LayoutParams?) {
    child?.giveRealParent()
    captureChildView(child)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    child?.giveRealParent()
    captureChildView(child)
    return true
  }

  /** 提供一个真实的 ViewParent 给此视图 */
  fun View.giveRealParent() = View::class.java.getDeclaredField("mParent")
    .apply { isAccessible = true }
    .set(this, uiParent)
}