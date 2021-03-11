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

@file:Suppress(
  "FunctionName", "NestedLambdaShadowedImplicitParameter",
  "MemberVisibilityCanBePrivate"
)
package com.meowbase.ui.extension.tab.implement

import android.content.Context
import com.meowbase.toolkit.widget.LinearLayoutParams
import com.meowbase.ui.core.LayoutSize
import com.meowbase.ui.core.unit.Px
import com.meowbase.ui.extension.pager.implement.Pager
import com.meowbase.ui.widget.Spacer


/*
 * author: 凛
 * date: 2020/8/19 9:10 PM
 * github: https://github.com/RinOrz
 * description: 分页指示器，实现原理是直接基于标签且不显示 Tab 与 Divider
 */
class PageIndicator(context: Context) : TabBar(context) {
  val indicator get() = scope.indicatorCreator(0)

  override var pager: Pager? = super.pager
    set(value) {
      field = value
      field?.fakeTabs()
      super.pager = value
    }

  private fun Pager.fakeTabs() {
    // 为每个新增的页添加假标签
    val new = (adapter?.itemCount ?: 0) - tabCount
    repeat(new) { fakeTab() }
  }

  @PublishedApi internal fun fakeTab() = Spacer(width = indicator.width, height = Px.Zero).also {
    if (indicator.width == LayoutSize.Match) {
      it.layoutParams = LinearLayoutParams { weight = 1f }
    }
  }
}