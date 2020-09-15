@file:Suppress("MemberVisibilityCanBePrivate")

package com.mars.ui.foundation.styles

import com.mars.ui.core.Border
import com.mars.ui.core.Modifier
import com.mars.ui.core.Padding
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.useOrElse
import com.mars.ui.foundation.ModifierProvider
import com.mars.ui.foundation.modifies.background
import com.mars.ui.foundation.modifies.padding
import com.mars.ui.foundation.modifies.rippleForeground
import com.mars.ui.theme.*
import com.mars.ui.theme.Styles.Companion.resolveStyle

/*
 * author: 凛
 * date: 2020/8/8 1:09 PM
 * github: https://github.com/oh-Rin
 * description: 通用的 Button 样式
 */
data class ButtonStyle(
  /** 按钮默认背景色 */
  val color: Color = Color.Unset,

  /** 按钮按压情况下覆盖在高亮层上的水波纹颜色, 默认 [Colors.ripple] */
  val colorRipple: Color = Color.Unset,

  /** 按钮按压后的高亮颜色 */
  val colorHighlight: Color = Color.Unset,

  /** 按钮禁用后的颜色 */
  val colorDisabled: Color = Color.Unset,

  /** 按钮边框 */
  val border: Border? = null,

  /** 按钮的内部边距 */
  val padding: Padding = Padding.Unspecified,

  /** 按钮形状, null 则使用默认的 [Shapes.small] */
  val shape: Shape? = null,
) : Style<ButtonStyle> {
  /** [Styles.resolveStyle] */
  override var id: Int = -1

  fun apply(provider: ModifierProvider) {
    provider.modifier = (provider.modifier ?: Modifier)
      .padding(padding)
      .rippleForeground(colorRipple, shape ?: currentShapes.small)
      .background(
        normal = color,
        pressed = colorHighlight,
        disabled = colorDisabled,
        border = border,
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