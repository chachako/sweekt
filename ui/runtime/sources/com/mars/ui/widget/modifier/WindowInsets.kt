@file:Suppress("NAME_SHADOWING", "OverridingDeprecatedMember", "UnusedImport")

package com.mars.ui.widget.modifier

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.mars.toolkit.view.marginBottom
import com.mars.toolkit.view.marginLeft
import com.mars.toolkit.view.marginRight
import com.mars.toolkit.view.marginTop
import com.mars.toolkit.widget.MarginLayoutParams
import com.mars.ui.core.Modifier
import com.mars.ui.widget.SafeArea

/**
 * 将视图的整体放置到安全的区域中
 *
 * @param top 竖屏状态下，避免 ui 内容入侵状态栏区域
 * @param bottom 竖屏状态下，避免 ui 内容入侵到导航栏区域
 * @param left 横屏状态下，避免 ui 内容入侵到导航栏区域
 * @param right 横屏状态下，避免 ui 内容入侵到导航栏区域
 * @see padding
 * @see SafeArea
 */
fun Modifier.safeArea(
  top: Boolean = true,
  bottom: Boolean = true,
  left: Boolean = true,
  right: Boolean = true,
) = +WindowInsetsModifier(top, bottom, left, right, SafeAreaApplyMode.Margin)

/**
 * 将视图的内容（子视图）放置到安全的区域中
 *
 * @param top 竖屏状态下，避免 ui 内容入侵状态栏区域
 * @param bottom 竖屏状态下，避免 ui 内容入侵到导航栏区域
 * @param left 横屏状态下，避免 ui 内容入侵到导航栏区域
 * @param right 横屏状态下，避免 ui 内容入侵到导航栏区域
 * @see margin
 * @see SafeArea
 */
fun Modifier.safeContentArea(
  top: Boolean = true,
  bottom: Boolean = true,
  left: Boolean = true,
  right: Boolean = true,
) = +WindowInsetsModifier(top, bottom, left, right, SafeAreaApplyMode.Padding)


/** 限制视图在安全区域的具体实现 */
private class WindowInsetsModifier(
  val _top: Boolean,
  val _bottom: Boolean,
  val _left: Boolean,
  val _right: Boolean,
  val _mode: SafeAreaApplyMode,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    val paddingLeft = paddingLeft
    val paddingTop = paddingTop
    val paddingRight = paddingRight
    val paddingBottom = paddingBottom

    val marginLeft = marginLeft
    val marginTop = marginTop
    val marginRight = marginRight
    val marginBottom = marginBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { myself, insets ->
      val top = if (_top) insets.getInsets(WindowInsetsCompat.Type.systemBars()).top else 0
      val left = if (_left) insets.getInsets(WindowInsetsCompat.Type.systemBars()).left else 0
      val right = if (_right) insets.getInsets(WindowInsetsCompat.Type.systemBars()).right else 0
      val bottom = if (_bottom) insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom else 0
      when (_mode) {
        SafeAreaApplyMode.Padding -> myself.updatePadding(
          left = paddingLeft + left,
          top = paddingTop + top,
          right = paddingRight + right,
          bottom = paddingBottom + bottom
        )
        SafeAreaApplyMode.Margin -> layoutParams = MarginLayoutParams {
          topMargin = marginTop + top
          leftMargin = marginLeft + left
          rightMargin = marginRight + right
          bottomMargin = marginBottom + bottom
        }
      }
      insets
    }
    ViewCompat.requestApplyInsets(this)
  }
}

private enum class SafeAreaApplyMode { Padding, Margin }