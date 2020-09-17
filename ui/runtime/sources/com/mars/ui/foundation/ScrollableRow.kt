@file:Suppress("FunctionName")

package com.mars.ui.foundation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES.P
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.DIRECTION_LEFT
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.DIRECTION_RIGHT
import com.mars.ui.UiKit
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.MainAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.SpringEdgeEffect


/** 可滚动的水平布局 */
class ScrollableRow @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : HorizontalScrollView(context, attrs, defStyleAttr),
  UiKit,
  ViewCatcher,
  ModifierProvider {
  internal var row: Row? = null
  private var scrollable: Boolean = true
  private val springManager = SpringEdgeEffect.Manager()

  override var modifier: Modifier
    get() = row!!.modifier
    set(value) {
      row!!.modifier = value
    }

  override val captured: MutableList<View>
    get() = row!!.captured

  init {
    isFillViewport = true
    replaceEdgesEffect()
  }

  override fun setOverScrollMode(overScrollMode: Int) {
    super.setOverScrollMode(overScrollMode)
    if (Build.VERSION.SDK_INT <= P) {
      replaceEdgesEffect()
    }
  }

  private fun replaceEdgesEffect() {
    javaClass.getDeclaredField("mEdgeGlowLeft")
      .apply { if (!isAccessible) isAccessible = true }
      .set(this, springManager.createEdgeEffect(this, DIRECTION_LEFT))

    javaClass.getDeclaredField("mEdgeGlowRight")
      .apply { if (!isAccessible) isAccessible = true }
      .set(this, springManager.createEdgeEffect(this, DIRECTION_RIGHT))
  }

  override fun startCapture() {
    row!!.startCapture()
  }

  override fun endCapture() {
    row!!.endCapture()
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(ev: MotionEvent?): Boolean =
    if (scrollable) super.onTouchEvent(ev) else true

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    row?.addView(child, width, height)
      ?: super.addView(child, width, height)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: ViewGroup.LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    return row?.addViewInLayout(child, index, params, preventRequestLayout)
      ?: super.addViewInLayout(child, index, params, preventRequestLayout)
  }

  fun update(
    /** 允许滚动 */
    enabled: Boolean = true,
    /** 其他更多的可选调整 */
    modifier: Modifier = this.modifier,
    /** 子内容的水平方向对齐 */
    mainAxisAlign: MainAxisAlignment = row!!.mainAxisAlign,
    /** 子内容的垂直方向对齐 */
    crossAxisAlign: CrossAxisAlignment = row!!.crossAxisAlign,
    /** 子控件权重总数，这会影响每个子控件的 [LinearLayout.LayoutParams.weight] */
    weightSum: Number? = row!!.weightSum,
  ) = also {
    it.scrollable = enabled
    row!!.update(modifier, mainAxisAlign, crossAxisAlign, weightSum)
  }
}


/** 可滚动的列视图 [UiKit.Row] */
fun UiKit.ScrollableRow(
  /** 允许滚动 */
  enabled: Boolean = true,
  /** 其他更多的可选调整 */
  modifier: Modifier = Modifier,
  /** 子内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 子内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /** 子控件权重总数，这会影响每个子控件的 [LinearLayout.LayoutParams.weight] */
  weightSum: Number? = null,
  children: Row.() -> Unit
): ScrollableRow = With(::ScrollableRow) {
  it.row = Row(
    mainAxisAlign = mainAxisAlign,
    crossAxisAlign = crossAxisAlign,
    weightSum = weightSum,
    children = children
  )
  it.update(enabled, modifier, mainAxisAlign, crossAxisAlign, weightSum)
}