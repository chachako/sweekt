@file:Suppress("FunctionName")

package com.meowbase.ui.widget.implement

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.meowbase.toolkit.widget.copyInto
import com.meowbase.ui.Ui
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.MainAxisAlignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.decoupling.LayoutCanvasProvider
import com.meowbase.ui.core.decoupling.ModifierProvider
import com.meowbase.ui.core.decoupling.ViewCanvasProvider
import com.meowbase.ui.core.decoupling.ViewCatcher
import com.meowbase.ui.uiParent


/*
 * author: 凛
 * date: 2020/8/8 8:30 PM
 * github: https://github.com/RinOrz
 * description: 可滚动的行视图
 */
class ScrollableRow @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : HorizontalScrollView(context, attrs, defStyleAttr),
  Ui,
  ViewCatcher,
  ViewCanvasProvider,
  LayoutCanvasProvider,
  ModifierProvider {
  internal var row: Row? = null
  private var scrollable: Boolean = true

  fun update(
    /** 允许滚动 */
    enabled: Boolean = this.scrollable,
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
    it.modifier = modifier
    row!!.update(
      mainAxisAlign = mainAxisAlign,
      crossAxisAlign = crossAxisAlign,
      weightSum = weightSum
    )
  }


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var captured: ArrayDeque<View>?
    get() = row?.captured ?: super.captured
    set(value) {
      row?.captured = value
    }
  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(uiParent) }
      layoutParams.copyInto(row?.layoutParams)
    }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    row?.addView(child, index, params) ?: super.addView(child, index, params)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: ViewGroup.LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean = row?.addViewInLayout(child, index, params, preventRequestLayout)
    ?: super.addViewInLayout(child, index, params, preventRequestLayout)
}