package com.mars.ui.widget.implement

import android.content.Context
import android.util.AttributeSet
import com.mars.toolkit.float
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.MainAxisAlignment
import com.mars.ui.core.Modifier
import android.widget.LinearLayout as AndroidLinearLayout

/*
 * author: 凛
 * date: 2020/9/29 上午4:18
 * github: https://github.com/oh-Rin
 * description: UiKit 中行布局的实现
 */
class Row @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : Linear(context, attrs, defStyleAttr, defStyleRes) {
  init {
    orientation = HORIZONTAL
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
