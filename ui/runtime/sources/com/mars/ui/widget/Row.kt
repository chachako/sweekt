@file:Suppress("FunctionName")

package com.mars.ui.widget

import com.mars.ui.Ui
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.MainAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.widget.implement.*
import android.widget.LinearLayout as AndroidLinearLayout

/**
 * 水平布局
 * 所有子控件都排放在一行中 [AndroidLinearLayout.HORIZONTAL]
 * @see [Ui.Column]
 */
inline fun Ui.Row(
  modifier: Modifier = Modifier,
  /** 子内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 子内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /** 子控件权重总数，这会影响每个子控件的 [AndroidLinearLayout.LayoutParams.weight] */
  weightSum: Number? = null,
  children: Row.() -> Unit
): Row = With(::Row) {
  it.update(modifier, mainAxisAlign, crossAxisAlign, weightSum)
  it.children()
}