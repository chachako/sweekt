/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

@file:Suppress("FunctionName")

package com.meowbase.ui.widget.implement

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.meowbase.toolkit.data.wrapContent
import com.meowbase.toolkit.widget.LayoutParams
import com.meowbase.toolkit.widget.copyInto
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.MainAxisAlignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.uiParent


/*
 * author: 凛
 * date: 2020/8/8 8:30 PM
 * github: https://github.com/RinOrz
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
    it.modifier = modifier
    column!!.update(
      mainAxisAlign = mainAxisAlign,
      crossAxisAlign = crossAxisAlign,
      weightSum = weightSum
    )
  }


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var captured: ArrayDeque<View>?
    get() = column?.captured ?: super.captured
    set(value) {
      column?.captured = value
    }
  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(uiParent) }
      layoutParams.copyInto(column?.layoutParams)
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