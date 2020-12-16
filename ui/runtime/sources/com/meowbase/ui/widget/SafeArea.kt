@file:Suppress("FunctionName")

package com.meowbase.ui.widget

import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.widget.implement.*

/**
 * 创建一个安全的布局区域，类似 Flutter 的 SafeArea
 *
 * @param top 竖屏状态下，避免 Ui 内容入侵 "状态栏" 区域
 * @param bottom 竖屏状态下，避免 Ui 内容入侵到 "导航栏" 区域
 * @param left 横屏状态下，避免 Ui 内容入侵到 "状态栏" 区域
 * @param right 横屏状态下，避免 Ui 内容入侵到 "导航栏" 区域
 * @param children 区域内的所有 View 都会被限制在安全区域内
 */
inline fun Ui.SafeArea(
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