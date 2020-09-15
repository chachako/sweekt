package com.mars.toolkit.widget

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