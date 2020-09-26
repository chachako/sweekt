@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate", "PropertyName",
  "NestedLambdaShadowedImplicitParameter"
)

package com.mars.ui.foundation

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.PointerIcon
import android.view.View
import com.mars.ui.Ui
import com.mars.ui.UiKitMarker
import com.mars.ui.core.Border
import com.mars.ui.core.Modifier
import com.mars.ui.core.Padding
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.CircleShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.foundation.modifies.clickable
import com.mars.ui.foundation.styles.ButtonStyle
import com.mars.ui.foundation.styles.IconStyle
import com.mars.ui.theme.Styles.Companion.resolveStyle
import com.mars.ui.theme.currentButtons
import com.mars.ui.theme.currentColors
import com.mars.ui.theme.currentIcons


/*
 * author: 凛
 * date: 2020/8/9 11:54 PM
 * github: https://github.com/oh-Rin
 * description: 图标按钮的扩展
 */
@UiKitMarker
class IconButton @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : Icon(context, attrs, defStyleAttr, defStyleRes), ButtonUi {
  /** 记录最后设置的值，以便主题系统来判断是否要更新对应的值 */
  private var buttonStyle: ButtonStyle? = null
    set(value) {
      field = value
      value?.apply(this)
    }

  init {
    isClickable = true
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
    /** [ButtonStyle.shape] */
    shape: Shape? = null,
    /** 对于按钮的其他额外调整 */
    modifier: Modifier = this.modifier,
    /** 按钮中的图标 */
    icon: Drawable? = null,
    /** [IconStyle.color] */
    iconColor: Color = Color.Unset,
    /** [IconStyle.padding] */
    padding: Padding = Padding.Unspecified,
    /** [IconStyle.size] */
    iconSize: SizeUnit = SizeUnit.Unspecified,
    /** [IconStyle.height] */
    iconHeight: SizeUnit = iconSize,
    /** [IconStyle.width] */
    iconWidth: SizeUnit = iconSize,
    /** 按钮图标的样式 */
    iconStyle: IconStyle = this.style!!,
    /** 按钮的样式 */
    style: ButtonStyle = this.buttonStyle!!,
  ) = also {
    update(
      asset = icon,
      color = iconColor,
      padding = padding,
      size = iconSize,
      height = iconHeight,
      width = iconWidth,
      style = iconStyle,
    )
    it.modifier = modifier
    it.buttonStyle = style.merge(
      ButtonStyle(
        color = color,
        colorRipple = colorRipple,
        colorHighlight = colorHighlight,
        colorDisabled = colorDisabled,
        border = border,
        shape = shape
      )
    )
  }

  override fun onResolvePointerIcon(event: MotionEvent?, pointerIndex: Int): PointerIcon? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

      if (pointerIcon == null && isClickable && isEnabled) {
        PointerIcon.getSystemIcon(context, PointerIcon.TYPE_HAND)
      } else super.onResolvePointerIcon(event, pointerIndex)

    } else super.onResolvePointerIcon(event, pointerIndex)
  }

  override fun updateUiKitTheme() {
    super.updateUiKitTheme()
    buttonStyle?.resolveStyle(this)?.also { buttonStyle = it }
  }
}


/** 创建并添加仅有图标的按钮视图 */
fun Ui.IconButton(
  /** 按钮中的图标 */
  icon: Drawable,
  /** 按钮按下后的回调 */
  onClick: ((View) -> Unit)? = null,
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
  /** [ButtonStyle.shape] */
  shape: Shape = CircleShape,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** [IconStyle.color] */
  iconColor: Color = Color.Unset,
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
  color: Color = Color.Unset,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unset,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unset,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unset,
  /** [ButtonStyle.border] */
  border: Border? = null,
  /** [ButtonStyle.shape] */
  shape: Shape = CircleShape,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** [IconStyle.color] */
  iconColor: Color = Color.Unset,
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
  color: Color = Color.Unset,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unset,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unset,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unset,
  /** [ButtonStyle.border] -> [Border.size] */
  borderSize: SizeUnit = SizeUnit.Unspecified,
  /** [IconStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = CircleShape,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** [IconStyle.color] */
  iconColor: Color = Color.Unset,
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
  color: Color = Color.Unset,
  /** [ButtonStyle.colorRipple] */
  colorRipple: Color = Color.Unset,
  /** [ButtonStyle.colorHighlight] */
  colorHighlight: Color = Color.Unset,
  /** [ButtonStyle.colorDisabled] */
  colorDisabled: Color = Color.Unset,
  /** [ButtonStyle.border] -> [Border.size] */
  borderSize: SizeUnit = SizeUnit.Unspecified,
  /** [IconStyle.padding] */
  padding: Padding = Padding.Unspecified,
  /** [ButtonStyle.shape] */
  shape: Shape = CircleShape,
  /** 对于按钮的其他额外调整 */
  modifier: Modifier = Modifier,
  /** [IconStyle.color] */
  iconColor: Color = Color.Unset,
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