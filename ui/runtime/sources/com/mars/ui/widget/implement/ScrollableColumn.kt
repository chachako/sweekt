@file:Suppress("FunctionName")

package com.mars.ui.widget.implement

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.MainAxisAlignment
import com.mars.ui.core.Modifier


/*
 * author: 凛
 * date: 2020/8/8 8:30 PM
 * github: https://github.com/oh-Rin
 * description: 可滚动的列视图
 */
class ScrollableColumn @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : VerticalScrollView(context, attrs, defStyleAttr) {
  internal var column: Column? = null

  fun update(
    /** 允许滚动 */
    enabled: Boolean = this.scrollable,
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


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var captured: ArrayDeque<View>?
    get() = column?.captured ?: super.captured
    set(value) {
      column?.captured = value
    }
  override var modifier: Modifier
    get() = column?.modifier ?: super.modifier
    set(value) {
      column?.modifier = value
    }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    column?.addView(child, index, params) ?: super.addView(child, index, params)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: ViewGroup.LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean = column?.addViewInLayout(child, index, params, preventRequestLayout)
    ?: super.addViewInLayout(child, index, params, preventRequestLayout)
}