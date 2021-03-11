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

@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate", "PropertyName", "RestrictedApi")

package com.meowbase.ui.widget

import android.view.View
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Alignment
import com.meowbase.ui.core.Border
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.core.text.*
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.TextUnit
import com.meowbase.ui.widget.implement.*
import com.meowbase.ui.widget.modifier.clickable
import com.meowbase.ui.widget.modifier.wrapContent
import com.meowbase.ui.widget.style.ButtonStyle
import com.meowbase.ui.widget.style.IconStyle
import com.meowbase.ui.widget.style.TextStyle
import com.meowbase.ui.theme.*

/** 创建一个按钮 */
fun Ui.Button(
  /** [ButtonStyle.color] */
  color: Color = currentColors.primary,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = currentColors.ripple,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = variantColorFor(color, default = Color.Unspecified),
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] */
  border: Border? = null,
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮中文本旁边的图标，可设置上下左右四个方向 */
  icons: EdgeIcons? = null,
  /** 按钮图标的样式 */
  iconsStyle: IconStyle = currentIcons.button,
  /** 按钮文本 */
  text: String? = null,
  /** 自动调整按钮中文本大小以适应容器，默认关闭 */
  isAutoSize: Boolean? = null,
  /** 文本对齐 */
  align: Alignment = Alignment.Center,
  /** 文本溢出时的处理 */
  overflow: TextOverflow = TextOverflow.End,
  /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
  marquee: TextMarquee? = null,
  /** 限制按钮文本行数 */
  maxLines: Int = 1,
  /** 限制按钮文本最长数量 */
  maxLength: Int? = null,
  /** 按钮中文本与图标 [icons] 之间的间隔 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.color] */
  textColor: Color = contentColorFor(color, default = currentColors.onPrimary),
  /** [TextStyle.lineHeight] */
  lineHeight: SizeUnit = SizeUnit.Unspecified,
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
  textStyle: TextStyle = currentTypography.button,
  /** 按钮样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮按下后的回调 */
  onClick: (View) -> Unit,
): Button = With(::Button) {
  it.update(
    text = text,
    // 原生创建 Button 默认是宽度填满父布局的，这里我们让它默认最小化
    modifier = Modifier.clickable(onClick = onClick).wrapContent().plus(modifier),
    isAutoSize = isAutoSize,
    align = align,
    overflow = overflow,
    marquee = marquee,
    maxLines = maxLines,
    maxLength = maxLength,
    icons = icons,
    iconsStyle = iconsStyle,
    spaceBetween = spaceBetween,
    textColor = textColor,
    lineHeight = lineHeight,
    font = font,
    fontStyle = fontStyle,
    fontSize = fontSize,
    fontName = fontName,
    letterSpacing = letterSpacing,
    textStyle = textStyle,
    color = color,
    colorRipple = colorRipple,
    colorHighlight = colorHighlight,
    colorDisabled = colorDisabled,
    border = border,
    padding = padding,
    shape = shape,
    style = style
  )
}


/** 创建一个按钮 */
fun ButtonBar.Button(
  /** [ButtonStyle.color] */
  color: Color = currentColors.primary,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = currentColors.ripple,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = variantColorFor(color, default = Color.Unspecified),
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] */
  border: Border? = null,
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮中文本旁边的图标，可设置上下左右四个方向 */
  icons: EdgeIcons? = null,
  /** 按钮图标的样式 */
  iconsStyle: IconStyle = currentIcons.button,
  /** 按钮文本 */
  text: String? = null,
  /** 自动调整按钮中文本大小以适应容器，默认关闭 */
  isAutoSize: Boolean? = null,
  /** 文本对齐 */
  align: Alignment = Alignment.Center,
  /** 文本溢出时的处理 */
  overflow: TextOverflow = TextOverflow.End,
  /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
  marquee: TextMarquee? = null,
  /** 限制按钮文本行数 */
  maxLines: Int = 1,
  /** 限制按钮文本最长数量 */
  maxLength: Int? = null,
  /** 按钮中文本与图标 [icons] 之间的间隔 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.color] */
  textColor: Color = contentColorFor(color, default = currentColors.onPrimary),
  /** [TextStyle.lineHeight] */
  lineHeight: SizeUnit = SizeUnit.Unspecified,
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
  textStyle: TextStyle = currentTypography.button,
  /** 按钮样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮按下后的回调 */
  onClick: (View) -> Unit,
) = (this as Ui).Button(
  color = color,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  border = border,
  padding = padding,
  shape = shape,
  modifier = modifier,
  icons = icons,
  iconsStyle = iconsStyle,
  text = text,
  isAutoSize = isAutoSize,
  align = align,
  overflow = overflow,
  marquee = marquee,
  maxLines = maxLines,
  maxLength = maxLength,
  spaceBetween = spaceBetween,
  textColor = textColor,
  lineHeight = lineHeight,
  font = font,
  fontStyle = fontStyle,
  fontSize = fontSize,
  fontName = fontName,
  letterSpacing = letterSpacing,
  textStyle = textStyle,
  style = style,
  onClick = onClick
)


/** 创建一个线框按钮 */
fun Ui.OutlinedButton(
  /** [ButtonStyle.border] -> [Border.color] */
  color: Color = currentColors.primaryVariant,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = currentColors.ripple,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] -> [Border.size] */
  borderSize: SizeUnit = SizeUnit.Unspecified,
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮中文本旁边的图标，可设置上下左右四个方向 */
  icons: EdgeIcons? = null,
  /** 按钮图标的样式 */
  iconsStyle: IconStyle = currentIcons.button,
  /** 按钮文本 */
  text: String? = null,
  /** 自动调整按钮中文本大小以适应容器，默认关闭 */
  isAutoSize: Boolean? = null,
  /** 文本对齐 */
  align: Alignment = Alignment.Center,
  /** 文本溢出时的处理 */
  overflow: TextOverflow = TextOverflow.End,
  /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
  marquee: TextMarquee? = null,
  /** 限制按钮文本行数 */
  maxLines: Int = 1,
  /** 限制按钮文本最长数量 */
  maxLength: Int? = null,
  /** 按钮中文本与图标 [icons] 之间的间隔 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.color] */
  textColor: Color = currentColors.primary,
  /** [TextStyle.lineHeight] */
  lineHeight: SizeUnit = SizeUnit.Unspecified,
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
  textStyle: TextStyle = currentTypography.button,
  /** 按钮样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮按下后的回调 */
  onClick: (View) -> Unit,
) = Button(
  // 线框按钮不需要背景
  color = Color.Transparent,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  border = Border(borderSize, color.useOrElse { currentColors.onPrimary }),
  padding = padding,
  shape = shape,
  modifier = modifier,
  icons = icons,
  iconsStyle = iconsStyle,
  text = text,
  isAutoSize = isAutoSize,
  align = align,
  overflow = overflow,
  marquee = marquee,
  maxLines = maxLines,
  maxLength = maxLength,
  spaceBetween = spaceBetween,
  textColor = textColor,
  lineHeight = lineHeight,
  font = font,
  fontStyle = fontStyle,
  fontSize = fontSize,
  fontName = fontName,
  letterSpacing = letterSpacing,
  textStyle = textStyle,
  style = style,
  onClick = onClick
)


/** 创建一个线框按钮 */
fun ButtonBar.OutlinedButton(
  /** [ButtonStyle.border] -> [Border.color] */
  color: Color = currentColors.primaryVariant,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = currentColors.ripple,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] -> [Border.size] */
  borderSize: SizeUnit = SizeUnit.Unspecified,
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮中文本旁边的图标，可设置上下左右四个方向 */
  icons: EdgeIcons? = null,
  /** 按钮图标的样式 */
  iconsStyle: IconStyle = currentIcons.button,
  /** 按钮文本 */
  text: String? = null,
  /** 自动调整按钮中文本大小以适应容器，默认关闭 */
  isAutoSize: Boolean? = null,
  /** 文本对齐 */
  align: Alignment = Alignment.Center,
  /** 文本溢出时的处理 */
  overflow: TextOverflow = TextOverflow.End,
  /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
  marquee: TextMarquee? = null,
  /** 限制按钮文本行数 */
  maxLines: Int = 1,
  /** 限制按钮文本最长数量 */
  maxLength: Int? = null,
  /** 按钮中文本与图标 [icons] 之间的间隔 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.color] */
  textColor: Color = currentColors.primary,
  /** [TextStyle.lineHeight] */
  lineHeight: SizeUnit = SizeUnit.Unspecified,
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
  textStyle: TextStyle = currentTypography.button,
  /** 按钮样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮按下后的回调 */
  onClick: (View) -> Unit,
) = (this as Ui).OutlinedButton(
  color = color,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  borderSize = borderSize,
  padding = padding,
  shape = shape,
  modifier = modifier,
  icons = icons,
  iconsStyle = iconsStyle,
  text = text,
  isAutoSize = isAutoSize,
  align = align,
  overflow = overflow,
  marquee = marquee,
  maxLines = maxLines,
  maxLength = maxLength,
  spaceBetween = spaceBetween,
  textColor = textColor,
  lineHeight = lineHeight,
  font = font,
  fontStyle = fontStyle,
  fontSize = fontSize,
  fontName = fontName,
  letterSpacing = letterSpacing,
  textStyle = textStyle,
  style = style,
  onClick = onClick
)