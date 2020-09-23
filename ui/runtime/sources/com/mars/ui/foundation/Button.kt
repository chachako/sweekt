@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate", "PropertyName", "RestrictedApi")

package com.mars.ui.foundation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.mars.ui.Ui
import com.mars.ui.UiKitMarker
import com.mars.ui.core.Alignment
import com.mars.ui.core.Border
import com.mars.ui.core.Modifier
import com.mars.ui.core.Padding
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.text.*
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.TextUnit
import com.mars.ui.foundation.modifies.clickable
import com.mars.ui.foundation.styles.ButtonStyle
import com.mars.ui.foundation.styles.IconStyle
import com.mars.ui.foundation.styles.TextStyle
import com.mars.ui.theme.*
import com.mars.ui.theme.Styles.Companion.resolveStyle

/*
 * author: 凛
 * date: 2020/8/9 11:54 PM
 * github: https://github.com/oh-Rin
 * description: 按钮的扩展
 */
@UiKitMarker class Button @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : Text(context, attrs, defStyleAttr) {
  /** 记录最后设置的值，以便主题系统来判断是否要更新对应的值 */
  internal var buttonStyle: ButtonStyle? = null
    set(value) {
      field = value
      value?.apply(this)
    }

  fun update(
    /** [ButtonStyle.color] */
    color: Color = Color.Unset,
    /** [ButtonStyle.colorRipple] */
    colorRipple: Color = Color.Unset,
    /** [ButtonStyle.colorHighlight] */
    colorHighlight: Color = Color.Unset,
    /** [ButtonStyle.colorDisabled] */
    colorDisabled: Color = Color.Unset,
    /** [ButtonStyle.border] */
    border: Border? = null,
    /** [ButtonStyle.padding] */
    padding: Padding = Padding.Unspecified,
    /** [ButtonStyle.shape] */
    shape: Shape = currentShapes.small,
    /** 按钮的样式 */
    style: ButtonStyle = this.buttonStyle!!,
  ) = also {
    it.buttonStyle = style.merge(
      ButtonStyle(
        color,
        colorRipple,
        colorHighlight,
        colorDisabled,
        border,
        padding,
        shape
      )
    )
  }

  override fun updateUiKitTheme() {
    super.updateUiKitTheme()
    buttonStyle?.resolveStyle()?.also { buttonStyle = it }
  }
}

/**
 * 标记是一个外观为 Button 的 View
 */
interface ButtonUI


/** 创建一个按钮 */
fun Ui.Button(
  /** [ButtonStyle.color] */
  color: Color = Color.Unset,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unset,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unset,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unset,
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
  textColor: Color = Color.Unset,
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
  style: ButtonStyle = currentStyles.button,
  /** 按钮按下后的回调 */
  onClick: (View) -> Unit,
): Button = With(::Button) {
  it.update(
    text = text,
    modifier = modifier.clickable(onClick = onClick),
    isAutoSize = isAutoSize,
    align = align,
    overflow = overflow,
    marquee = marquee,
    maxLines = maxLines,
    maxLength = maxLength,
    icons = icons,
    iconsStyle = iconsStyle,
    spaceBetween = spaceBetween,
    color = textColor,
    lineHeight = lineHeight,
    font = font,
    fontStyle = fontStyle,
    fontSize = fontSize,
    fontName = fontName,
    letterSpacing = letterSpacing,
    style = textStyle,
  )
  // 要在后面进行更新，因为 Text 中的 update 方法会覆盖 modifier
  it.update(
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
  color: Color = Color.Unset,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unset,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unset,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unset,
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
  textColor: Color = Color.Unset,
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
  style: ButtonStyle = currentStyles.button,
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
  color: Color = Color.Unset,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unset,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unset,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unset,
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
  textColor: Color = Color.Unset,
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
  style: ButtonStyle = currentStyles.button,
  /** 按钮按下后的回调 */
  onClick: (View) -> Unit,
) = Button(
  // 线框按钮不需要背景
  color = Color.Transparent,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  border = Border(borderSize, color.useOrElse { currentColors.onSurface.copy(alpha = 0.12f) }),
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
  color: Color = Color.Unset,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unset,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unset,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unset,
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
  textColor: Color = Color.Unset,
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
  style: ButtonStyle = currentStyles.button,
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