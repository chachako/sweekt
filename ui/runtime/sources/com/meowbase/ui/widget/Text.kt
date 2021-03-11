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

@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate",
  "PropertyName", "OverridingDeprecatedMember"
)

package com.meowbase.ui.widget

import com.meowbase.ui.Ui
import com.meowbase.ui.core.Alignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.text.*
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.TextUnit
import com.meowbase.ui.widget.implement.*
import com.meowbase.ui.widget.style.IconStyle
import com.meowbase.ui.widget.style.TextStyle
import com.meowbase.ui.theme.PingFangFont
import com.meowbase.ui.theme.Typography
import com.meowbase.ui.theme.currentTypography


/** 创建一个 [Text] 视图 */
fun Ui.Text(
  text: String? = null,
  modifier: Modifier = Modifier,
  /** 自动调整文本大小以适应容器，默认关闭 */
  isAutoSize: Boolean? = null,
  /** 文本对齐 */
  align: Alignment? = null,
  /** 文本溢出时的处理 */
  overflow: TextOverflow? = null,
  /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
  marquee: TextMarquee? = null,
  /** 限制文本行数 */
  maxLines: Int? = null,
  /** 限制文本最长数量 */
  maxLength: Int? = null,
  /** 长按选择文本 */
  isSelectable: Boolean? = null,
  /** 文本旁边的图标，可设置上下左右四个方向 */
  icons: EdgeIcons? = null,
  /** 文本旁边的图标的样式 */
  iconsStyle: IconStyle? = null,
  /** 文本与图标 [icons] 之间的间隔 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.color] */
  color: Color = Color.Unspecified,
  /** [TextStyle.lineHeight] */
  lineHeight: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.decoration] */
  decoration: TextDecoration? = null,
  /** [TextStyle.font] */
  font: Font? = null,
  /** [TextStyle.fontStyle] */
  fontStyle: FontStyle = FontStyle.Normal,
  /** [TextStyle.fontSize] */
  fontSize: SizeUnit = TextUnit.Inherit,
  /** [TextStyle.fontName] */
  fontName: String? = PingFangFont.Regular,
  /** [TextStyle.letterSpacing] */
  letterSpacing: SizeUnit = SizeUnit.Unspecified,
  /** 文本样式（不是字体样式 [FontStyle]） */
  style: TextStyle = currentTypography.body1,
): Text = With(::Text) {
  it.update(
    text = text,
    modifier = modifier,
    isAutoSize = isAutoSize,
    align = align,
    overflow = overflow,
    marquee = marquee,
    maxLines = maxLines,
    maxLength = maxLength,
    isSelectable = isSelectable,
    icons = icons,
    iconsStyle = iconsStyle,
    spaceBetween = spaceBetween,
    color = color,
    lineHeight = lineHeight,
    decoration = decoration,
    font = font,
    fontStyle = fontStyle,
    fontSize = fontSize,
    fontName = fontName,
    letterSpacing = letterSpacing,
    style = style,
  )
}


/**
 * 为按钮添加一个 [Text] 视图
 * NOTE: 对于按钮内的文本，我们应该默认使用 [Typography.button] 样式
 */
fun ButtonLayout.Text(
  text: String? = null,
  modifier: Modifier = Modifier,
  /** 自动调整文本大小以适应容器，默认关闭 */
  isAutoSize: Boolean? = null,
  /** 文本对齐 */
  align: Alignment? = null,
  /** 文本溢出时的处理 */
  overflow: TextOverflow? = null,
  /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
  marquee: TextMarquee? = null,
  /** 限制文本行数 */
  maxLines: Int? = null,
  /** 限制文本最长数量 */
  maxLength: Int? = null,
  /** 长按选择文本 */
  isSelectable: Boolean? = null,
  /** 文本旁边的图标，可设置上下左右四个方向 */
  icons: EdgeIcons? = null,
  /** 文本旁边的图标的样式 */
  iconsStyle: IconStyle? = null,
  /** 文本与图标 [icons] 之间的间隔 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.color] */
  color: Color = Color.Unspecified,
  /** [TextStyle.lineHeight] */
  lineHeight: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.decoration] */
  decoration: TextDecoration? = null,
  /** [TextStyle.font] */
  font: Font? = null,
  /** [TextStyle.fontStyle] */
  fontStyle: FontStyle = FontStyle.Normal,
  /** [TextStyle.fontSize] */
  fontSize: SizeUnit = TextUnit.Inherit,
  /** [TextStyle.fontName] */
  fontName: String? = PingFangFont.Regular,
  /** [TextStyle.letterSpacing] */
  letterSpacing: SizeUnit = SizeUnit.Unspecified,
  /** 文本样式（不是字体样式 [FontStyle]） */
  style: TextStyle = currentTypography.body1,
) = (this as Ui).Text(
  text = text,
  modifier = modifier,
  isAutoSize = isAutoSize,
  align = align,
  overflow = overflow,
  marquee = marquee,
  maxLines = maxLines,
  maxLength = maxLength,
  isSelectable = isSelectable,
  icons = icons,
  iconsStyle = iconsStyle,
  spaceBetween = spaceBetween,
  color = color,
  lineHeight = lineHeight,
  decoration = decoration,
  font = font,
  fontStyle = fontStyle,
  fontSize = fontSize,
  fontName = fontName,
  letterSpacing = letterSpacing,
  style = style
)