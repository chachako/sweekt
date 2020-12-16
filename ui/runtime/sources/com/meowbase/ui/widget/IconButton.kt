@file:Suppress("FunctionName")

package com.meowbase.ui.widget

import android.graphics.drawable.Drawable
import android.view.View
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Border
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.shape.OvalShape
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.widget.implement.*
import com.meowbase.ui.widget.modifier.clickable
import com.meowbase.ui.widget.style.ButtonStyle
import com.meowbase.ui.widget.style.IconStyle
import com.meowbase.ui.theme.currentButtons
import com.meowbase.ui.theme.currentColors
import com.meowbase.ui.theme.currentIcons


/** 创建并添加仅有图标的按钮视图 */
fun Ui.IconButton(
  /** 按钮中的图标 */
  icon: Drawable,
  /** 按钮按下后的回调 */
  onClick: ((View) -> Unit)? = null,
  /** [ButtonStyle.color] */
  color: Color = Color.Unspecified,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unspecified,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] */
  border: Border? = null,
  /** [ButtonStyle.shape] */
  shape: Shape = OvalShape,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** [IconStyle.color] */
  iconColor: Color = Color.Unspecified,
  /** [IconStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [IconStyle.size] */
  iconSize: SizeUnit = SizeUnit.Unspecified,
  /** [IconStyle.height] */
  iconHeight: SizeUnit = iconSize,
  /** [IconStyle.width] */
  iconWidth: SizeUnit = iconSize,
  /** 按钮图标的样式 */
  iconStyle: IconStyle = currentIcons.button,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.icon,
): IconButton = With(::IconButton) {
  it.update(
    color = color,
    colorRipple = colorRipple,
    colorHighlight = colorHighlight,
    colorDisabled = colorDisabled,
    border = border,
    padding = padding,
    shape = shape,
    modifier = onClick?.let { modifier.clickable(onClick = it) } ?: modifier,
    icon = icon,
    iconColor = iconColor,
    iconSize = iconSize,
    iconHeight = iconHeight,
    iconWidth = iconWidth,
    iconStyle = iconStyle,
    style = style
  )
}


/** 创建并添加仅有图标的按钮视图 */
fun ButtonBar.IconButton(
  /** 按钮中的图标 */
  icon: Drawable,
  /** [ButtonStyle.color] */
  color: Color = Color.Unspecified,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unspecified,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] */
  border: Border? = null,
  /** [ButtonStyle.shape] */
  shape: Shape = OvalShape,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** [IconStyle.color] */
  iconColor: Color = Color.Unspecified,
  /** [IconStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [IconStyle.size] */
  iconSize: SizeUnit = SizeUnit.Unspecified,
  /** [IconStyle.height] */
  iconHeight: SizeUnit = iconSize,
  /** [IconStyle.width] */
  iconWidth: SizeUnit = iconSize,
  /** 按钮图标的样式 */
  iconStyle: IconStyle = currentIcons.button,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.icon,
) = (this as Ui).IconButton(
  onClick = null,
  color = color,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  border = border,
  padding = padding,
  shape = shape,
  modifier = modifier,
  icon = icon,
  iconColor = iconColor,
  iconSize = iconSize,
  iconHeight = iconHeight,
  iconWidth = iconWidth,
  iconStyle = iconStyle,
  style = style
)


/** 创建并添加仅有图标的线框按钮视图 */
fun Ui.OutlinedIconButton(
  /** 按钮中的图标 */
  icon: Drawable,
  /** 按钮按下后的回调 */
  onClick: (View) -> Unit,
  /** [ButtonStyle.border] -> [Border.color] */
  color: Color = Color.Unspecified,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unspecified,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] -> [Border.size] */
  borderSize: SizeUnit = SizeUnit.Unspecified,
  /** [IconStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = OvalShape,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** [IconStyle.color] */
  iconColor: Color = Color.Unspecified,
  /** [IconStyle.size] */
  iconSize: SizeUnit = SizeUnit.Unspecified,
  /** [IconStyle.height] */
  iconHeight: SizeUnit = iconSize,
  /** [IconStyle.width] */
  iconWidth: SizeUnit = iconSize,
  /** 按钮图标的样式 */
  iconStyle: IconStyle = currentIcons.button,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.icon,
) = IconButton(
  onClick = onClick,
  // 线框按钮不需要背景
  color = Color.Transparent,
  colorRipple = colorRipple,
  colorDisabled = colorDisabled,
  colorHighlight = colorHighlight,
  border = Border(borderSize, color.useOrElse { currentColors.onSurface.copy(alpha = 0.12f) }),
  padding = padding,
  shape = shape,
  modifier = modifier,
  icon = icon,
  iconColor = iconColor,
  iconSize = iconSize,
  iconHeight = iconHeight,
  iconWidth = iconWidth,
  iconStyle = iconStyle,
  style = style
)


/** 创建并添加仅有图标的线框按钮视图 */
fun ButtonBar.OutlinedIconButton(
  /** 按钮中的图标 */
  icon: Drawable,
  /** 按钮按下后的回调 */
  onClick: (View) -> Unit,
  /** [ButtonStyle.border] -> [Border.color] */
  color: Color = Color.Unspecified,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unspecified,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unspecified,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unspecified,
  /** [ButtonStyle.border] -> [Border.size] */
  borderSize: SizeUnit = SizeUnit.Unspecified,
  /** [IconStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = OvalShape,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** [IconStyle.color] */
  iconColor: Color = Color.Unspecified,
  /** [IconStyle.size] */
  iconSize: SizeUnit = SizeUnit.Unspecified,
  /** [IconStyle.height] */
  iconHeight: SizeUnit = iconSize,
  /** [IconStyle.width] */
  iconWidth: SizeUnit = iconSize,
  /** 按钮图标的样式 */
  iconStyle: IconStyle = currentIcons.button,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.icon,
) = (this as Ui).OutlinedIconButton(
  onClick = onClick,
  color = color,
  colorRipple = colorRipple,
  colorDisabled = colorDisabled,
  colorHighlight = colorHighlight,
  borderSize = borderSize,
  padding = padding,
  shape = shape,
  modifier = modifier,
  icon = icon,
  iconColor = iconColor,
  iconSize = iconSize,
  iconHeight = iconHeight,
  iconWidth = iconWidth,
  iconStyle = iconStyle,
  style = style
)