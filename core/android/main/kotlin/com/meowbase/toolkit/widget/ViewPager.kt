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

package com.meowbase.toolkit.widget

import androidx.viewpager2.widget.ViewPager2

/** 判断是否有上一页 */
val ViewPager2.hasPreviousPage get() = currentItem > 0

/** 判断是否有下一页 */
val ViewPager2.hasNextPage get() = adapter != null && currentItem < adapter!!.itemCount - 1

/** 跳转到上一页 */
fun ViewPager2.toPreviousPage() { if (hasPreviousPage) currentItem -= 1 }

/** 跳转到下一页 */
fun ViewPager2.toNextPage() { if (hasNextPage) currentItem += 1 }

/**
 * 添加 [ViewPager2] 的页面变化监听
 *
 * ```
 * viewPager2.onPageChanged {
 *   selected { position ->
 *     // onPageSelected
 *   }
 *   scrolled { position, offset, offsetPixels ->
 *     // onPageScrolled
 *   }
 *   scrollStateChanged { state ->
 *     // onPageScrollStateChanged
 *   }
 * }
 * ```
 * @see ViewPagerChangeListener
 */
fun ViewPager2.onPageChanged(listener: ViewPager2.OnPageChangeCallback.() -> Unit) =
  registerOnPageChangeCallback(ViewPagerChangeListener().apply(listener))

private class ViewPagerChangeListener : ViewPager2.OnPageChangeCallback() {
  private var onPageScrollStateChanged: ((Int) -> Unit)? = null
  private var onPageSelected: ((Int) -> Unit)? = null
  private var onPageScrolled: ((Int, Float, Int) -> Unit)? = null

  override fun onPageScrollStateChanged(state: Int) {
    onPageScrollStateChanged?.invoke(state)
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    onPageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
  }

  override fun onPageSelected(position: Int) {
    onPageSelected?.invoke(position)
  }

  fun scrolled(callback: (position: Int, offset: Float, offsetPixels: Int) -> Unit) {
    onPageScrolled = callback
  }

  fun selected(callback: (position: Int) -> Unit) {
    onPageSelected = callback
  }

  fun scrollStateChanged(callback: (state: Int) -> Unit) {
    onPageScrollStateChanged = callback
  }
}