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

package com.meowbase.ui.widget.implement

import android.content.Context
import android.util.AttributeSet
import com.meowbase.toolkit.float
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.MainAxisAlignment
import com.meowbase.ui.core.Modifier
import android.widget.LinearLayout as AndroidLinearLayout

/*
 * author: 凛
 * date: 2020/9/29 上午4:18
 * github: https://github.com/RinOrz
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
