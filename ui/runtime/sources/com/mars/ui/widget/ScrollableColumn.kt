@file:Suppress("FunctionName")

package com.mars.ui.widget

import android.widget.LinearLayout
import com.mars.ui.Ui
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.MainAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.widget.implement.*


/** 可滚动的列视图 [Ui.Column] */
fun Ui.ScrollableColumn(
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
  it.column = it.Column(
    mainAxisAlign = mainAxisAlign,
    crossAxisAlign = crossAxisAlign,
    weightSum = weightSum,
    children = children
  )
  it.update(enabled, modifier, mainAxisAlign, crossAxisAlign, weightSum)
}