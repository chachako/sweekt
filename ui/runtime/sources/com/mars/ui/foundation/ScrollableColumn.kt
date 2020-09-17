@file:Suppress("FunctionName")

package com.mars.ui.foundation

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.DIRECTION_BOTTOM
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.DIRECTION_TOP
import com.mars.ui.UiKit
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.MainAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.core.SpringEdgeEffect


/** 可滚动的垂直布局 */
class ScrollableColumn @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : NestedScrollView(context, attrs, defStyleAttr),
  UiKit,
  ViewCatcher,
  ModifierProvider {
  internal var column: Column? = null
  private var scrollable: Boolean = true
  private val springManager = SpringEdgeEffect.Manager()

  override var modifier: Modifier
    get() = column!!.modifier
    set(value) {
      column!!.modifier = value
    }

  override val captured: MutableList<View>
    get() = column!!.captured

  init {
    isFillViewport = true
    replaceEdgesEffect()
  }

  private fun replaceEdgesEffect() {
    javaClass.getDeclaredField("mEdgeGlowTop")
      .apply { if (!isAccessible) isAccessible = true }
      .set(this, springManager.createEdgeEffect(this, DIRECTION_TOP))

    javaClass.getDeclaredField("mEdgeGlowBottom")
      .apply { if (!isAccessible) isAccessible = true }
      .set(this, springManager.createEdgeEffect(this, DIRECTION_BOTTOM))
  }

  override fun startCapture() {
    column!!.startCapture()
  }

  override fun endCapture() {
    column!!.endCapture()
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(ev: MotionEvent?): Boolean =
    if (scrollable) super.onTouchEvent(ev) else true

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    column?.addView(child, width, height)
      ?: super.addView(child, width, height)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: ViewGroup.LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    return column?.addViewInLayout(child, index, params, preventRequestLayout)
      ?: super.addViewInLayout(child, index, params, preventRequestLayout)
  }

  fun update(
    /** 允许滚动 */
    enabled: Boolean = true,
    /** 其他更多的可选调整 */
    modifier: Modifier = this.modifier,
    /** 子内容的水平方向对齐 */
    mainAxisAlign: MainAxisAlignment = column!!.mainAxisAlign,
    /** 子内容的垂直方向对齐 */
    crossAxisAlign: CrossAxisAlignment = column!!.crossAxisAlign,
    /** 子控件权重总数，这会影响每个子控件的 [LinearLayout.LayoutParams.weight] */
    weightSum: Number? = column!!.weightSum,
  ) = also {
    it.scrollable = enabled
    column!!.update(modifier, mainAxisAlign, crossAxisAlign, weightSum)
  }
}


/** 可滚动的列视图 [UiKit.Column] */
fun UiKit.ScrollableColumn(
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
  children: Column.() -> Unit
): ScrollableColumn = With(::ScrollableColumn) {
  it.column = Column(
    mainAxisAlign = mainAxisAlign,
    crossAxisAlign = crossAxisAlign,
    weightSum = weightSum,
    children = children
  )
  it.update(enabled, modifier, mainAxisAlign, crossAxisAlign, weightSum)
}