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

@file:Suppress("FunctionName")

package com.meowbase.ui.core.graphics

import android.graphics.drawable.Drawable
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.widget.style.IconStyle

/*
 * author: 凛
 * date: 2020/8/19 1:15 PM
 * github: https://github.com/RinOrz
 * description: 图标数据
 */
data class IconData(
  val icon: Drawable,
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
)

fun IconData(icon: Drawable, style: IconStyle) = style.toIconData(icon)

/** 将 [IconStyle] 转换为 [IconData] */
fun IconStyle.toIconData(icon: Drawable) = IconData(
  icon = icon,
  color = color,
  size = size,
  width = width,
  height = height,
  padding = padding
)