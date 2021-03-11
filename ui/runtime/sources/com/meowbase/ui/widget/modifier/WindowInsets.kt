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

@file:Suppress("NAME_SHADOWING", "OverridingDeprecatedMember", "UnusedImport")

package com.meowbase.ui.widget.modifier

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.meowbase.toolkit.that
import com.meowbase.toolkit.view.marginBottom
import com.meowbase.toolkit.view.marginLeft
import com.meowbase.toolkit.view.marginRight
import com.meowbase.toolkit.view.marginTop
import com.meowbase.toolkit.widget.MarginLayoutParams
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.widget.SafeArea

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
    // 记录此 View 第一次的边距
    val paddingLeft = paddingLeft
    val paddingTop = paddingTop
    val paddingRight = paddingRight
    val paddingBottom = paddingBottom

    val marginLeft = marginLeft
    val marginTop = marginTop
    val marginRight = marginRight
    val marginBottom = marginBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { myself, insets ->
      // FIXME 我们需要用一种办法来监听用户进行 view padding/margin 更改
      // FIXME 因为这会导致一旦后面 Insets 发生变化，将忽略用户在第一次更改后的所有 view padding/margin 更改
      // FIXME 暂时解决方案是如果需要 view padding/margin, 要在 safeArea, safeContentArea 调用前调用
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      val top = _top that systemBars.top ?: 0
      val left = _left that systemBars.left ?: 0
      val right = _right that systemBars.right ?: 0
      val bottom = _bottom that systemBars.bottom ?: 0
      when (_mode) {
        SafeAreaApplyMode.Padding -> myself.updatePadding(
          left = paddingLeft + left,
          top = paddingTop + top,
          right = paddingRight + right,
          bottom = paddingBottom + bottom
        )
        SafeAreaApplyMode.Margin -> myself.layoutParams = MarginLayoutParams {
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