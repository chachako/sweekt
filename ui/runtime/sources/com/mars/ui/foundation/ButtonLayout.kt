@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate",
  "PropertyName", "NestedLambdaShadowedImplicitParameter"
)

package com.mars.ui.foundation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.mars.ui.UiKit
import com.mars.ui.UiKitMarker
import com.mars.ui.core.*
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.isUnspecified
import com.mars.ui.foundation.modifies.clickable
import com.mars.ui.foundation.modifies.margin
import com.mars.ui.foundation.styles.ButtonStyle
import com.mars.ui.theme.Styles.Companion.resolveStyle
import com.mars.ui.theme.currentColors
import com.mars.ui.theme.currentShapes
import com.mars.ui.theme.currentStyles

/*
 * author: 凛
 * date: 2020/8/9 11:54 PM
 * github: https://github.com/oh-Rin
 * description: 按钮的扩展
 */
@UiKitMarker class ButtonLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : Linear(context, attrs, defStyleAttr, defStyleRes), ButtonUI {
  /** 记录最后设置的值，以便主题系统来判断是否要更新对应的值 */
  private var style: ButtonStyle? = null
    set(value) {
      field = value
      value?.apply(this)
    }

  /** 更新 [ButtonLayout] 的一些参数 */
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
    shape: Shape? = null,
    /** 对于按钮的其他额外调整 */
    modifier: Modifier = Modifier,
    /** 按钮的样式 */
    style: ButtonStyle = this.style!!,
  ) = also {
    it.modifier = modifier
    it.style = style.merge(
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
    style?.resolveStyle()?.also { style = it }
  }
}


/** 创建并添加按钮视图 */
inline fun UiKit.Button(
  /** 按钮按下后的回调 */
  noinline onClick: ((View) -> Unit)?,
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
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
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentStyles.button,
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
  it.startCapture()
  children(it)
  val spaceModifier = when (orientation) {
    Orientation.Horizontal -> Modifier.margin(start = spaceBetween)
    else -> Modifier.margin(top = spaceBetween)
  }
  it.captured.forEachIndexed { index, view ->
    if (index > 0) {
      spaceModifier.realize(view, it)
    }
  }
  it.endCapture()
}


/** 创建并添加按钮视图 */
fun ButtonBar.Button(
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
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
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentStyles.button,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
) = (this as UiKit).Button(
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
inline fun UiKit.OutlinedButton(
  /** 按钮内容的水平方向对齐 */
  mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start,
  /** 按钮内容的垂直方向对齐 */
  crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start,
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
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentStyles.button,
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
  /** 按钮内控件与控件之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 按钮的样式 */
  style: ButtonStyle = currentStyles.button,
  /** 按钮内容 */
  children: ButtonLayout.() -> Unit,
) = (this as UiKit).OutlinedButton(
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