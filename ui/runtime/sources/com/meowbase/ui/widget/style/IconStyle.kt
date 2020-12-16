@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.ui.widget.style

import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.useOrElse
import com.meowbase.ui.core.useOrElse
import com.meowbase.ui.widget.implement.Icon
import com.meowbase.ui.widget.modifier.size
import com.meowbase.ui.theme.Style
import com.meowbase.ui.theme.Styles
import com.meowbase.ui.theme.Styles.Companion.resolveStyle

/*
 * author: 凛
 * date: 2020/8/8 1:09 PM
 * github: https://github.com/RinOrz
 * description: 通用的 Icon 样式
 */
data class IconStyle(
  /** 图标颜色 */
  val color: Color = Color.Unspecified,
  /** 图标大小 */
  val size: SizeUnit = SizeUnit.Unspecified,
  /** 图标宽度，优先级比 [size] 要高 */
  val width: SizeUnit = size,
  /** 图标高度，优先级比 [size] 要高 */
  val height: SizeUnit = size,
  /** 图标的内边距 */
  val padding: Padding = Padding.Unspecified,
) : Style<IconStyle> {
  /** [Styles.resolveStyle] */
  override var id: Int = -1

  fun apply(icon: Icon) {
    icon.iconPadding = padding
    icon.iconColor = color
    icon.iconWidth = width
    icon.iconHeight = height
  }

  /** 将当前 [IconStyle] 与 [other] 进行合并后得到一个新的样式 */
  fun merge(other: IconStyle): IconStyle = IconStyle(
    color = other.color.useOrElse { this.color },
    size = other.size.useOrElse { this.size },
    width = other.width.useOrElse { this.width },
    height = other.height.useOrElse { this.height },
    padding = other.padding.useOrElse { this.padding },
  ).also { if (this != it) it.id = id }

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }
}