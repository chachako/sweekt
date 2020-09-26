@file:Suppress("FunctionName")

package com.mars.ui.foundation

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.mars.toolkit.content.res.resource
import com.mars.ui.Ui
import com.mars.ui.asLayout
import com.mars.ui.core.Modifier
import com.mars.ui.core.Orientation
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.foundation.styles.ButtonStyle
import com.mars.ui.foundation.styles.IconStyle
import com.mars.ui.theme.currentButtons
import com.mars.ui.theme.currentIcons


/**
 * 由多个样式相同的图标按钮组成的按钮栏
 * @see ButtonBar
 */
fun Ui.IconButtonBar(
  /** 指定多个按钮的 [Drawable] 类型图标 */
  icons: Array<Drawable>,
  /** 在按钮栏内部按钮按下后的回调 */
  onClick: (index: Int, button: View) -> Unit,
  /** 对于按钮栏的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 多个按钮之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮图标的样式 */
  iconStyle: IconStyle = currentIcons.button,
  /** 按钮的样式 */
  buttonStyle: ButtonStyle = currentButtons.icon,
) = ButtonBar(onClick, modifier, orientation, spaceBetween) {
  icons.forEach { IconButton(it, iconStyle = iconStyle, style = buttonStyle) }
}


/**
 * 由多个样式相同的图标按钮组成的按钮栏
 * @see ButtonBar
 */
fun Ui.IconButtonBar(
  /** 指定多个按钮的 [Bitmap] 类型图标 */
  icons: Array<Bitmap>,
  /** 在按钮栏内部按钮按下后的回调 */
  onClick: (index: Int, button: View) -> Unit,
  /** 对于按钮栏的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 多个按钮之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮图标的样式 */
  iconStyle: IconStyle = currentIcons.button,
  /** 按钮的样式 */
  buttonStyle: ButtonStyle = currentButtons.icon,
) = ButtonBar(onClick, modifier, orientation, spaceBetween) {
  icons.forEach {
    IconButton(
      icon = BitmapDrawable(asLayout.resources, it),
      iconStyle = iconStyle,
      style = buttonStyle
    )
  }
}


/**
 * 由多个样式相同的图标按钮组成的按钮栏
 * @see ButtonBar
 */
fun Ui.IconButtonBar(
  /** 指定多个按钮的 [Resources] 图标，根据 id 寻找 [Drawable] */
  icons: IntArray,
  /** 在按钮栏内部按钮按下后的回调 */
  onClick: (index: Int, button: View) -> Unit,
  /** 对于按钮栏的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 多个按钮之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮图标的样式 */
  iconStyle: IconStyle = currentIcons.button,
  /** 按钮的样式 */
  buttonStyle: ButtonStyle = currentButtons.icon,
) = ButtonBar(onClick, modifier, orientation, spaceBetween) {
  icons.forEach {
    IconButton(
      icon = asLayout.resource(it),
      iconStyle = iconStyle,
      style = buttonStyle
    )
  }
}