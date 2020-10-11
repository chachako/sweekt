@file:Suppress("FunctionName", "NestedLambdaShadowedImplicitParameter")

package com.mars.ui.extension.tab

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.mars.ui.Ui
import com.mars.ui.core.CrossAxisAlignment
import com.mars.ui.core.MainAxisAlignment
import com.mars.ui.core.Modifier
import com.mars.ui.extension.tab.implement.*
import com.mars.ui.widget.Row
import com.mars.ui.widget.VerticalDivider
import com.mars.ui.widget.With
import com.mars.ui.widget.modifier.matchParent

/**
 * 创建一个横向标签栏
 * @see Row
 */
inline fun Ui.TabRow(
  /** 标签栏整体的其他调整 */
  modifier: Modifier = Modifier,
  /** 子内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 子内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
  /**
   * 标签指示器
   * 可为每个不同的 Tab 指定不同的指示器
   * [TabBar] 在滑动时会自动过渡到另一个指示器
   */
  noinline indicator: TabBar.Scope.(index: Int) -> TabIndicator = { TabIndicator() },
  /**
   * 标签指示器的动画插值器
   * 这会决定滑动时所展示出来的指示器画面
   * @see TabIndicator.LinearInterpolator
   * @see TabIndicator.SmartInterpolator
   */
  indicatorInterpolator: TabIndicator.Interpolator = TabIndicator.SmartInterpolator.Default,
  /** 指示器在标签栏垂直方向中的对齐方式 */
  indicatorAlign: CrossAxisAlignment = CrossAxisAlignment.End,
  /**
   * 返回每个标签间的分割线（可以是任意 [View]）
   * 默认不显示分割线
   */
  noinline divider: TabBar.Scope.(index: Int) -> View? = { VerticalDivider() },
  /** [divider] 于标签栏垂直方向中的对齐方式 */
  dividerAlign: CrossAxisAlignment = CrossAxisAlignment.Center,
  /**
   * 是否允许滑动（建议在超过 4 个 Tab 时开启）
   * True: 包装 Tab 的最小长度，超出范围后循环滑动标签栏
   * False: 扩展 Tab 的最大长度，约等于 Linear 的 weight = 1.0
   */
  scrollable: Boolean = false,
  /**
   * 每个标签项
   * WARN: 如果与 [ViewPager2] 绑定
   * 则 [tabs] 内所添加的标签数量必须与 [ViewPager2.getAdapter] 的页数保持一致
   */
  tabs: TabBar.() -> Unit,
): TabBar = TabBarContainer(scrollable, modifier) {
  With(::TabBar, if (scrollable) Modifier.matchParent() else modifier) {
    it.mainAxisAlign = mainAxisAlign
    it.crossAxisAlign = crossAxisAlign
    it.indicatorCreator = indicator
    it.indicatorInterpolator = indicatorInterpolator
    it.indicatorAlign = indicatorAlign
    it.dividerCreator = divider
    it.dividerAlign = dividerAlign
    tabs(it)
  }
}

/**
 * 创建一个可滑动的横向标签栏
 * @see TabRow
 * @see Row
 */
@PublishedApi internal inline fun Ui.TabBarContainer(
  enable: Boolean,
  modifier: Modifier,
  children: Ui.() -> TabBar
): TabBar = when (enable) {
  true -> With(::TabBarContainer, modifier) { it.tabBar = it.children() }.tabBar
  else -> children()
}
