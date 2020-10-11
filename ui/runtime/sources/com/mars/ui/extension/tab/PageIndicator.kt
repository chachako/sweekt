@file:Suppress("FunctionName")

package com.mars.ui.extension.tab

import android.graphics.Paint
import androidx.viewpager2.widget.ViewPager2
import com.mars.ui.Ui
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.LayoutSize
import com.mars.ui.core.Modifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.gradient.LinearGradient
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.useOrElse
import com.mars.ui.extension.tab.implement.PageIndicator
import com.mars.ui.extension.tab.implement.TabBar
import com.mars.ui.extension.tab.implement.TabIndicator
import com.mars.ui.extension.tab.implement.TabIndicatorRenderer
import com.mars.ui.widget.With
import com.mars.ui.theme.currentColors


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