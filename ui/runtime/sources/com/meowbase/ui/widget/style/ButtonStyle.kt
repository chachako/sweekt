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

package com.meowbase.ui.widget.style

import com.meowbase.ui.Ui
import com.meowbase.ui.core.Border
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.core.decoupling.ModifierProvider
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.core.useOrElse
import com.meowbase.ui.widget.modifier.background
import com.meowbase.ui.widget.modifier.padding
import com.meowbase.ui.widget.modifier.rippleForeground
import com.meowbase.ui.theme.*
import com.meowbase.ui.theme.Styles.Companion.resolveStyle

/*
 * author: 凛
 * date: 2020/8/8 1:09 PM
 * github: https://github.com/RinOrz
 * description: 通用的 Button 样式
 */
data class ButtonStyle(
  /** 按钮默认背景色 */
  val color: Color = Color.Unspecified,

  /** 按钮按压情况下覆盖在高亮层上的水波纹颜色, 默认 [Colors.ripple] */
  val colorRipple: Color = Color.Unspecified,

  /** 按钮按压后的高亮颜色 */
  val colorHighlight: Color = Color.Unspecified,

  /** 按钮禁用后的颜色 */
  val colorDisabled: Color = Color.Unspecified,

  /** 按钮边框 */
  val border: Border? = null,

  /** 按钮的内部边距 */
  val padding: Padding = Padding(horizontal = 16.dp, vertical = 12.dp),

  /** 按钮形状, null 则使用默认的 [Shapes.small] */
  val shape: Shape? = null,
) : Style<ButtonStyle> {
  /** [Styles.resolveStyle] */
  override var id: Int = -1

  fun apply(provider: ModifierProvider) {
    provider as Ui
    val realShape = shape ?: provider.currentShapes.small
    provider.modifier = Modifier.apply { +provider.modifier }.padding(padding)
      .background(
        normal = color,
        pressed = colorHighlight,
        disabled = colorDisabled,
        border = border,
        shape = realShape
      ).rippleForeground(
        color = colorRipple,
        shape = realShape
      )
  }

  /** 将当前 [ButtonStyle] 与 [other] 进行合并后得到一个新的样式 */
  fun merge(other: ButtonStyle): ButtonStyle = ButtonStyle(
    color = other.color.useOrElse { this.color },
    colorRipple = other.colorRipple.useOrElse { this.colorRipple },
    colorHighlight = other.colorHighlight.useOrElse { this.colorHighlight },
    padding = other.padding.useOrElse { this.padding },
    border = other.border ?: this.border,
    shape = other.shape ?: this.shape,
  ).also { if (this != it) it.id = id }

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }
}