@file:Suppress("FunctionName")

package com.meowbase.ui.widget

import com.meowbase.ui.Ui
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.MainAxisAlignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.widget.implement.*
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