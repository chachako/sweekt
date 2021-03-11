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

package com.meowbase.ui.widget

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.meowbase.toolkit.content.res.resource
import com.meowbase.ui.Ui
import com.meowbase.ui.asLayout
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Orientation
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.widget.style.ButtonStyle
import com.meowbase.ui.widget.style.IconStyle
import com.meowbase.ui.theme.currentButtons
import com.meowbase.ui.theme.currentIcons


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