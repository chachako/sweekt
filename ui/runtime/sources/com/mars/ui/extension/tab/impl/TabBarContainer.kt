@file:Suppress("MemberVisibilityCanBePrivate")

package com.mars.ui.extension.tab.impl

import android.content.Context
import android.widget.HorizontalScrollView
import androidx.core.view.get
import com.mars.ui.UiKit
import com.mars.ui.UiKitMarker
import com.mars.ui.core.unit.dp
import com.mars.ui.core.unit.toIntPx
import com.mars.ui.extension.pager.Pager
import kotlin.math.roundToInt


/*
 * author: 凛
 * date: 2020/8/19 8:35 PM
 * github: https://github.com/oh-Rin
 * description: Tab 容器（子项需要且只能是标签栏）
 * from: https://github.com/ogaclejapan/SmartTabLayout
 * todo: may need rtl support
 */
@UiKitMarker @PublishedApi
internal class TabBarContainer(context: Context) : HorizontalScrollView(context), UiKit {
  lateinit var tabBar: TabBar

  /** 当第一次滑动后指示器的偏移位置 */
  var indicatorOffset = 24.dp

  /** 指示器滑动偏移模式 */
  var indicatorOffsetMode = TabIndicator.OffsetMode.KeepFirst
    set(value) {
      field = value
      isFillViewport = field != TabIndicator.OffsetMode.AlwaysInCenter
    }

  val pager: Pager? get() = tabBar.pager

  val tabCount: Int get() = tabBar.childCount

  init {
    isFillViewport = indicatorOffsetMode != TabIndicator.OffsetMode.AlwaysInCenter
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    super.onLayout(changed, l, t, r, b)
    // Ensure first scroll
    if (changed && pager != null) {
      scrollToTab(pager!!.currentItem, 0f)
    }
  }

  internal fun scrollToTab(tabIndex: Int, positionOffset: Float) {
    if (tabCount == 0 || tabIndex < 0 || tabIndex >= tabCount) return

    val selectedTab = tabBar[tabIndex]
    val widthPlusMargin = Utils.getWidth(selectedTab) + Utils.getMarginHorizontally(selectedTab)
    var extraOffset = (positionOffset * widthPlusMargin).toInt()
    if (indicatorOffsetMode == TabIndicator.OffsetMode.AlwaysInCenter) {
      if (0f < positionOffset && positionOffset < 1f) {
        val nextTab = tabBar[tabIndex + 1]
        val selectHalfWidth = Utils.getWidth(selectedTab) / 2 + Utils.getMarginEnd(selectedTab)
        val nextHalfWidth = Utils.getWidth(nextTab) / 2 + Utils.getMarginStart(nextTab)
        extraOffset = (positionOffset * (selectHalfWidth + nextHalfWidth)).roundToInt()
      }
      val firstTab = tabBar[0]
      val first = Utils.getWidth(firstTab) + Utils.getMarginStart(firstTab)
      val selected = Utils.getWidth(selectedTab) + Utils.getMarginStart(selectedTab)
      val x: Int = (Utils.getStart(selectedTab) - Utils.getMarginStart(selectedTab) + extraOffset) - (first - selected) / 2
      scrollTo(x, 0)
      return
    }
    var x: Int
    if (indicatorOffsetMode == TabIndicator.OffsetMode.AutoInCenter) {
      if (0f < positionOffset && positionOffset < 1f) {
        val nextTab = tabBar[tabIndex + 1]
        val selectHalfWidth = Utils.getWidth(selectedTab) / 2 + Utils.getMarginEnd(selectedTab)
        val nextHalfWidth = Utils.getWidth(nextTab) / 2 + Utils.getMarginStart(nextTab)
        extraOffset = (positionOffset * (selectHalfWidth + nextHalfWidth)).roundToInt()
      }
      x = (Utils.getWidthWithMargin(selectedTab) / 2 - width / 2) + Utils.getPaddingStart(this)
    } else {
      x = if (tabIndex > 0 || positionOffset > 0) -indicatorOffset.toIntPx() else 0
    }
    val start = Utils.getStart(selectedTab)
    val startMargin = Utils.getMarginStart(selectedTab)
    x += start - startMargin + extraOffset
    scrollTo(x, 0)
  }
}