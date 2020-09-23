@file:Suppress("FunctionName")

package com.mars.ui.foundation

import android.content.Context
import android.util.AttributeSet
import com.mars.toolkit.float
import com.mars.ui.Ui
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

  fun update(
    modifier: Modifier = this.modifier,
    /** 子内容的水平方向对齐 */
    mainAxisAlign: MainAxisAlignment = this.mainAxisAlign,
    /** 子内容的垂直方向对齐 */
    crossAxisAlign: CrossAxisAlignment = this.crossAxisAlign,
    /** 子控件权重总数，这会影响每个子控件的 [AndroidLinearLayout.LayoutParams.weight] */
    weightSum: Number? = this.weightSum,
  ) = also {
    it.modifier = modifier
    it.mainAxisAlign = mainAxisAlign
    it.crossAxisAlign = crossAxisAlign
    if (weightSum != null && it.weightSum != weightSum) it.weightSum = weightSum.float
  }
}

/**
 * 垂直布局
 * 所有子控件都排放在一列中 [AndroidLinearLayout.VERTICAL]
 * @see [Ui.Row]
 */
inline fun Ui.Column(
  modifier: Modifier = Modifier,
  /** 子内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 子内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /** 子控件权重总数，这会影响每个子控件的 [AndroidLinearLayout.LayoutParams.weight] */
  weightSum: Number? = null,
  children: Column.() -> Unit
): Column = With(::Column) {
  it.update(modifier, mainAxisAlign, crossAxisAlign, weightSum)
  it.children()
}