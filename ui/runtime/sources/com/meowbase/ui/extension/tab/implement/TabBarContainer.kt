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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.ui.extension.tab.implement

import android.content.Context
import androidx.core.view.get
import com.meowbase.ui.Ui
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.core.unit.toIntPx
import com.meowbase.ui.extension.pager.implement.Pager
import com.meowbase.ui.widget.implement.HorizontalScrollView
import kotlin.math.roundToInt


/*
 * author: 凛
 * date: 2020/8/19 8:35 PM
 * github: https://github.com/RinOrz
 * description: Tab 容器（子项需要且只能是标签栏）
 * from: https://github.com/ogaclejapan/SmartTabLayout
 * todo: may need rtl support
 */
@UiKitMarker @PublishedApi
internal class TabBarContainer(context: Context) : HorizontalScrollView(context), Ui {
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