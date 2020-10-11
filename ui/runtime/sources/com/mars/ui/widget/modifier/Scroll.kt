package com.mars.ui.widget.modifier

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ScrollView
import androidx.core.view.*
import androidx.core.widget.NestedScrollView
import com.mars.toolkit.view.parentView
import com.mars.toolkit.view.removeFromParent
import com.mars.ui.core.Modifier
import com.mars.ui.core.Orientation
import com.mars.ui.realParent
import com.mars.ui.widget.implement.HorizontalScrollView
import com.mars.ui.widget.implement.VerticalScrollView


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
        parent.realParent?.addView(this)
      }
    } else {
      // 开启滑动
      this.apply { removeFromParent() }
      val scrollView = when (orientation) {
        Orientation.Horizontal -> HorizontalScrollView(context).also {
          it.isFillViewport = fillViewport
          it.layoutParams = ViewGroup.LayoutParams(layoutParams)
          it += this
        }
        Orientation.Vertical -> VerticalScrollView(context).also {
          it.isFillViewport = fillViewport
          it.layoutParams = ViewGroup.LayoutParams(layoutParams)
          it += this
        }
      }
      parent += scrollView
    }
  }
}