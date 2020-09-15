@file:Suppress("FunctionName")

package com.mars.ui.foundation

import android.content.Context
import android.util.AttributeSet
import com.mars.toolkit.float
import com.mars.ui.UiKit
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.MainAxisAlignment
import com.mars.ui.core.Modifier
import android.widget.LinearLayout as AndroidLinearLayout

/** 垂直布局 */
class Column @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : Linear(context, attrs, defStyleAttr, defStyleRes) {
  init {
    orientation = VERTICAL
  }
}

/**
 * 垂直布局
 * 所有子控件都排放在一列中 [AndroidLinearLayout.VERTICAL]
 * @receiver 自动将线性布局添加进父布局中
 */
inline fun UiKit.Column(
  modifier: Modifier = Modifier,
  /** 子内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 子内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /** 子控件权重总数，这会影响每个子控件的 [AndroidLinearLayout.LayoutParams.weight] */
  weightSum: Number? = null,
  children: Column.() -> Unit
): Column = With(::Column) {
  weightSum?.float?.apply(it::setWeightSum)
  it.mainAxisAlign = mainAxisAlign
  it.crossAxisAlign = crossAxisAlign
  it.modifier = modifier
  it.children()
}
