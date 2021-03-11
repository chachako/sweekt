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

package com.meowbase.ui.extension.tab

import android.graphics.Paint
import androidx.viewpager2.widget.ViewPager2
import com.meowbase.ui.Ui
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.LayoutSize
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.gradient.LinearGradient
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.useOrElse
import com.meowbase.ui.extension.tab.implement.PageIndicator
import com.meowbase.ui.extension.tab.implement.TabBar
import com.meowbase.ui.extension.tab.implement.TabIndicator
import com.meowbase.ui.extension.tab.implement.TabIndicatorRenderer
import com.meowbase.ui.widget.With
import com.meowbase.ui.theme.currentColors


/**
 * 创建一个分页的指示器 View
 * @see TabRow
 */
fun Ui.PageIndicator(
  /**
   * 初始总页数，用于决定指示器的数量
   * NOTE: 如果后续与 [ViewPager2] 连接则会覆盖此设置
   */
  pageCount: Int = 1,
  /** 指示器的颜色 */
  color: Color = currentColors.primary,
  /**
   * 指示器的渐变效果
   * ```
   * 例子:
   * LinearGradient(arrayOf(currentColors.primary, currentColors.secondary))
   * ```
   * TODO Support other gradient effects
   */
  gradient: LinearGradient? = null,
  /**
   * 指示器的宽度
   * 默认为 View 的总宽度 / [pageCount]
   */
  width: SizeUnit = SizeUnit.Unspecified,
  /** 自定义渲染画笔参数 [TabBar.indicatorPaint] */
  updatePaint: (Paint.(start: Float, end: Float, top: Float, bottom: Float, offset: Float) -> Unit)? = null,
  /**
   * 渲染器，用于渲染并显示指示器
   * @see TabIndicatorRenderer.Rectangle
   * @see TabIndicatorRenderer.Rounded
   */
  renderer: TabIndicatorRenderer = TabIndicatorRenderer.Rounded,
  /**
   * 标签指示器的动画插值器
   * 这会决定滑动时所展示出来的指示器画面
   * @see TabIndicator.LinearInterpolator
   * @see TabIndicator.SmartInterpolator
   */
  interpolator: TabIndicator.Interpolator = TabIndicator.SmartInterpolator.Default,
  /** 指示器的其他可选修饰 */
  modifier: Modifier = Modifier,
): PageIndicator = With(::PageIndicator, modifier) {
  it.indicatorCreator = {
    TabIndicator(
      color = color,
      gradient = gradient,
      width = width.useOrElse { LayoutSize.Match },
      updatePaint = updatePaint,
      renderer = renderer,
    )
  }
  it.indicatorInterpolator = interpolator
  it.indicatorAlign = CrossAxisAlignment.Stretch
  check(pageCount > 0) { "指示器至少需要一个页面才能正常工作！" }
  repeat(pageCount) { _ -> it.fakeTab() }
}