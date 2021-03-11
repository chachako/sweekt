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

package com.meowbase.ui.core.text

import android.graphics.drawable.Drawable
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.IconData
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.widget.style.IconStyle

/*
 * author: 凛
 * date: 2020/8/19 1:15 PM
 * github: https://github.com/RinOrz
 * description: 定义某个物件边缘的图标
 */
data class EdgeIcons(
  val start: IconData? = null,
  val end: IconData? = null,
  val top: IconData? = null,
  val bottom: IconData? = null,
)

/** 定义某个物件横纵向边缘的图标 */
fun EdgeIcons(
  horizontal: IconData? = null,
  vertical: IconData? = null,
) = EdgeIcons(
  horizontal, horizontal,
  vertical, vertical,
)

/**定义 某个物件边缘的四个图标 */
fun EdgeIcons(all: IconData? = null) = EdgeIcons(all, all, all, all)


// 定义边缘结尾的图标

fun EdgeStartIcon(
  icon: Drawable,
  /** 图标颜色 */
  color: Color = Color.Unspecified,
  /** 图标大小 */
  size: SizeUnit = SizeUnit.Unspecified,
  /** 图标宽度，优先级比 [size] 要高 */
  width: SizeUnit = size,
  /** 图标高度，优先级比 [size] 要高 */
  height: SizeUnit = size,
  /** 图标的内边距 */
  padding: Padding = Padding.Unspecified,
) = EdgeIcons(start = IconData(icon, color, size, width, height, padding))

fun EdgeStartIcon(
  icon: Drawable,
  style: IconStyle,
) = EdgeIcons(start = IconData(icon, style))


// 定义边缘结尾的图标

fun EdgeEndIcon(
  icon: Drawable,
  /** 图标颜色 */
  color: Color = Color.Unspecified,
  /** 图标大小 */
  size: SizeUnit = SizeUnit.Unspecified,
  /** 图标宽度，优先级比 [size] 要高 */
  width: SizeUnit = size,
  /** 图标高度，优先级比 [size] 要高 */
  height: SizeUnit = size,
  /** 图标的内边距 */
  padding: Padding = Padding.Unspecified,
) = EdgeIcons(end = IconData(icon, color, size, width, height, padding))

fun EdgeEndIcon(
  icon: Drawable,
  style: IconStyle,
) = EdgeIcons(end = IconData(icon, style))


// 定义上边缘的图标

fun EdgeTopIcon(
  icon: Drawable,
  /** 图标颜色 */
  color: Color = Color.Unspecified,
  /** 图标大小 */
  size: SizeUnit = SizeUnit.Unspecified,
  /** 图标宽度，优先级比 [size] 要高 */
  width: SizeUnit = size,
  /** 图标高度，优先级比 [size] 要高 */
  height: SizeUnit = size,
  /** 图标的内边距 */
  padding: Padding = Padding.Unspecified,
) = EdgeIcons(top = IconData(icon, color, size, width, height, padding))

fun EdgeTopIcon(
  icon: Drawable,
  style: IconStyle,
) = EdgeIcons(top = IconData(icon, style))


// 定义下边缘的图标

fun EdgeBottomIcon(
  icon: Drawable,
  /** 图标颜色 */
  color: Color = Color.Unspecified,
  /** 图标大小 */
  size: SizeUnit = SizeUnit.Unspecified,
  /** 图标宽度，优先级比 [size] 要高 */
  width: SizeUnit = size,
  /** 图标高度，优先级比 [size] 要高 */
  height: SizeUnit = size,
  /** 图标的内边距 */
  padding: Padding = Padding.Unspecified,
) = EdgeIcons(bottom = IconData(icon, color, size, width, height, padding))

fun EdgeBottomIcon(
  icon: Drawable,
  style: IconStyle,
) = EdgeIcons(bottom = IconData(icon, style))