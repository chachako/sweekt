package com.mars.ui.foundation.modifies

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.ScrollingView
import androidx.core.view.isEmpty
import androidx.core.widget.NestedScrollView
import com.mars.toolkit.view.parentView
import com.mars.toolkit.view.removeFromParent
import com.mars.ui.core.Modifier
import com.mars.ui.core.Orientation


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
    if (!enabled) {
      // 禁用滑动
      if (parent is NestedScrollingParent3
        || parent is NestedScrollingChild3
        || parent is ScrollingView
        || parent is NestedScrollView
        || parent is HorizontalScrollView
      ) {
        removeFromParent()
        if (parent.isEmpty()) parent.removeFromParent()
        parent.parentView.addView(this)
      }
    } else {
      // 开启滑动
      removeFromParent()
      when (orientation) {
        Orientation.Horizontal -> HorizontalScrollView(context).apply {
          isFillViewport = fillViewport
          layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }.addView(this)
        Orientation.Vertical -> NestedScrollView(context).apply {
          isFillViewport = fillViewport
          layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        }.addView(this)
      }
    }
  }
}