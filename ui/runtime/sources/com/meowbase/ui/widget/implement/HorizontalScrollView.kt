@file:Suppress("FunctionName")

package com.meowbase.ui.widget.implement

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.os.Build.VERSION_CODES.P
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView as AndroidHorizontalScrollView
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.DIRECTION_RIGHT
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.SpringEdgeEffect
import com.meowbase.ui.core.decoupling.*
import com.meowbase.ui.uiParent


/*
 * author: 凛
 * date: 2020/8/8 8:30 PM
 * github: https://github.com/RinOrz
 * description: 水平方向的滚动布局
 */
open class HorizontalScrollView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : AndroidHorizontalScrollView(context, attrs, defStyleAttr),
  Ui,
  ViewCatcher,
  ViewCanvasProvider,
  LayoutCanvasProvider,
  ModifierProvider {
  private var scrollable: Boolean = true
  private val springManager = SpringEdgeEffect.Manager()

  init {
    replaceEdgesEffect()
  }

  override fun setOverScrollMode(overScrollMode: Int) {
    super.setOverScrollMode(overScrollMode)
    if (Build.VERSION.SDK_INT <= P) {
      replaceEdgesEffect()
    }
  }

  private fun replaceEdgesEffect() {
    AndroidHorizontalScrollView::class.java.getDeclaredField("mEdgeGlowLeft")
      .apply { if (!isAccessible) isAccessible = true }
      .set(this, springManager.createEdgeEffect(this, DIRECTION_RIGHT, reverseAbsorb = true))

    AndroidHorizontalScrollView::class.java.getDeclaredField("mEdgeGlowRight")
      .apply { if (!isAccessible) isAccessible = true }
      .set(this, springManager.createEdgeEffect(this, DIRECTION_RIGHT))
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(ev: MotionEvent?): Boolean =
    if (scrollable) super.onTouchEvent(ev) else true

  fun update(
    /** 允许滚动 */
    enabled: Boolean = this.scrollable,
    /** 其他更多的可选调整 */
    modifier: Modifier = this.modifier,
  ) = also {
    it.scrollable = enabled
    this.modifier = modifier
  }


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var captured: ArrayDeque<View>? = null
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
    if (!intercepted) springManager.withSpring(canvas) { super.dispatchDraw(canvas) }

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