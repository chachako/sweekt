@file:Suppress("FunctionName")

package com.mars.ui.foundation

import android.content.Context
import android.util.AttributeSet
import com.mars.ui.UiKit
import com.mars.ui.UiKitMarker
import com.mars.ui.core.Modifier
import com.mars.ui.foundation.modifies.safeArea

/**
 * 屏幕安全区域
 */
@UiKitMarker class SafeArea @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : Stack(context, attrs, defStyleAttr, defStyleRes) {
  var protectedLeft = true
  var protectedRight = true
  var protectedTop = true
  var protectedBottom = true

  /** 对区域上锁 */
  fun lockup() = modifier.safeArea(
    top = protectedTop,
    bottom = protectedBottom,
    left = protectedLeft,
    right = protectedRight
  ).apply { realize(null) }
}


/**
 * 创建一个安全的布局区域，类似 Flutter 的 SafeArea
 *
 * @param top 竖屏状态下，避免 Ui 内容入侵 "状态栏" 区域
 * @param bottom 竖屏状态下，避免 Ui 内容入侵到 "导航栏" 区域
 * @param left 横屏状态下，避免 Ui 内容入侵到 "状态栏" 区域
 * @param right 横屏状态下，避免 Ui 内容入侵到 "导航栏" 区域
 * @param children 区域内的所有 View 都会被限制在安全区域内
 */
inline fun UiKit.SafeArea(
  left: Boolean = true,
  right: Boolean = true,
  top: Boolean = true,
  bottom: Boolean = true,
  modifier: Modifier = Modifier,
  children: SafeArea.() -> Unit,
): SafeArea = With(::SafeArea) {
  with(it) {
    protectedTop = top
    protectedBottom = bottom
    protectedLeft = left
    protectedRight = right
    this.modifier = modifier
    lockup()
    children()
  }
}