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