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

package com.meowbase.ui.widget.modifier

import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.view.*
import androidx.core.widget.NestedScrollView
import com.meowbase.toolkit.view.removeFromParent
import com.meowbase.toolkit.widget.MarginLayoutParams
import com.meowbase.toolkit.widget.copyInto
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Orientation
import com.meowbase.ui.uiParent
import com.meowbase.ui.widget.implement.HorizontalScrollView
import com.meowbase.ui.widget.implement.VerticalScrollView


/**
 * 用 [ScrollView] 包装元素，以开启垂直滚动功能
 *
 * @param enabled 允许滚动或禁止滚动
 * @param fillViewport 让元素填满滚动视图
 */
fun Modifier.verticalScroll(
  enabled: Boolean = true,
  fillViewport: Boolean = true
) = +ScrollModifier(enabled, fillViewport, Orientation.Vertical)


/**
 * 用 [ScrollView] 包装元素，以开启水平滚动功能
 *
 * @param enabled 允许滚动或禁止滚动
 * @param fillViewport 让元素填满滚动视图
 */
fun Modifier.horizontalScroll(
  enabled: Boolean = true,
  fillViewport: Boolean = true
) = +ScrollModifier(enabled, fillViewport, Orientation.Horizontal)


/** Scroller wrapping */
private data class ScrollModifier(
  val enabled: Boolean,
  val fillViewport: Boolean,
  val orientation: Orientation,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    if (parent == null) return
    if (!enabled) {
      // 禁用滑动
      if (parent is NestedScrollingParent3
        || parent is NestedScrollingChild3
        || parent is ScrollingView
        || parent is NestedScrollView
        || parent is HorizontalScrollView
      ) {
        // 删掉此 View
        removeFromParent()
        // 在删除此 View 后它的父滑动布局可能也要删掉（当没有子 View 时）
        if (parent.isEmpty()) parent.removeFromParent()
        parent.uiParent?.addView(this)
      }
    } else {
      // 开启滑动
      this.apply { removeFromParent() }
      val scrollView = when (orientation) {
        Orientation.Horizontal -> HorizontalScrollView(context).also {
          it.isFillViewport = fillViewport
          val scrollLayoutParams = MarginLayoutParams()
          this.layoutParams.copyInto(scrollLayoutParams)
          it.layoutParams = scrollLayoutParams
          it += this
        }
        Orientation.Vertical -> VerticalScrollView(context).also {
          it.isFillViewport = fillViewport
          val scrollLayoutParams = MarginLayoutParams()
          this.layoutParams.copyInto(scrollLayoutParams)
          it.layoutParams = scrollLayoutParams
          it += this
        }
      }
      parent += scrollView
    }
  }
}