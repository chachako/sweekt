@file:Suppress("FunctionName")

package com.mars.ui.widget

import android.view.View
import com.mars.ui.Ui
import com.mars.ui.core.*
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.isUnspecified
import com.mars.ui.widget.implement.ButtonBar
import com.mars.ui.widget.implement.ButtonLayout
import com.mars.ui.widget.modifier.clickable
import com.mars.ui.widget.modifier.margin
import com.mars.ui.widget.style.ButtonStyle
import com.mars.ui.theme.currentButtons
import com.mars.ui.theme.currentColors
import com.mars.ui.theme.currentShapes


/** 创建并添加按钮视图 */
inline fun Ui.Button(
  /** 按钮按下后的回调 */
  noinline onClick: ((View) -> Unit)?,
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
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
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
): ButtonLayout = With(::ButtonLayout) {
  it.orientation = orientation.native
  it.mainAxisAlign = mainAxisAlign
  it.crossAxisAlign = crossAxisAlign
  it.modifier = modifier
  it.update(
    color = color,
    colorRipple = colorRipple,
    colorHighlight = colorHighlight,
    colorDisabled = colorDisabled,
    border = border,
    padding = padding,
    shape = shape,
    modifier = modifier.clickable(onClick = onClick),
    style = style
  )

  if (spaceBetween.isUnspecified) {
    children(it)
    return@With
  }

  val spaceModifier = when (orientation) {
    Orientation.Horizontal -> Modifier.margin(start = spaceBetween)
    else -> Modifier.margin(top = spaceBetween)
  }
  ModifyScope(spaceModifier) { children(it) }
}


/** 创建并添加按钮视图 */
fun ButtonBar.Button(
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
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
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
) = (this as Ui).Button(
  onClick = null,
  mainAxisAlign = mainAxisAlign,
  crossAxisAlign = crossAxisAlign,
  color = color,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  border = border,
  padding = padding,
  shape = shape,
  modifier = modifier,
  spaceBetween = spaceBetween,
  orientation = orientation,
  style = style,
  children = children
)


/** 创建并添加线框按钮视图 */
inline fun Ui.OutlinedButton(
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
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
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
) = Button(
  onClick = null,
  mainAxisAlign = mainAxisAlign,
  crossAxisAlign = crossAxisAlign,
  // 线框按钮不需要背景
  color = Color.Transparent,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  border = Border(borderSize, color.useOrElse { currentColors.onSurface.copy(alpha = 0.12f) }),
  padding = padding,
  shape = shape,
  modifier = modifier,
  spaceBetween = spaceBetween,
  orientation = orientation,
  style = style,
  children = children
)


/** 创建并添加线框按钮视图 */
inline fun ButtonBar.OutlinedButton(
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
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
  /** [ButtonStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = currentShapes.small,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentButtons.normal,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
) = (this as Ui).OutlinedButton(
  mainAxisAlign = mainAxisAlign,
  crossAxisAlign = crossAxisAlign,
  color = color,
  colorRipple = colorRipple,
  colorHighlight = colorHighlight,
  colorDisabled = colorDisabled,
  borderSize = borderSize,
  padding = padding,
  shape = shape,
  modifier = modifier,
  spaceBetween = spaceBetween,
  orientation = orientation,
  style = style,
  children = children
)