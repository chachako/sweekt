@file:Suppress("FunctionName")

package com.meowbase.ui.widget

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.meowbase.ui.Ui
import com.meowbase.ui.asLayout
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.widget.implement.*
import com.meowbase.ui.widget.style.IconStyle
import com.meowbase.ui.theme.currentIcons

/**
 * 创建一个 Icon 视图
 * @param asset 设置 [Drawable] 类型的图标
 * @param modifier 对于 View 的其他额外调整
 * @param color [IconStyle.color]
 * @param padding 图标内边距
 * @param size [IconStyle.size]
 * @param style 图标样式 [IconStyle]
 * @see Image
 */
fun Ui.Icon(
  asset: Drawable,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  padding: Padding = Padding.Unspecified,
  size: SizeUnit = SizeUnit.Unspecified,
  style: IconStyle = currentIcons.medium,
): Icon = With(::Icon) {
  it.update(
    asset = asset,
    modifier = modifier,
    padding = padding,
    color = color,
    size = size,
    style = style
  )
}

/**
 * 创建一个 Icon 视图
 * @param asset 设置 [Bitmap] 类型的图标
 * @param modifier 对于 View 的其他额外调整
 * @param color [IconStyle.color]
 * @param padding 图标内边距
 * @param size [IconStyle.size]
 * @param style 图标样式 [IconStyle]
 * @see Image
 */
fun Ui.Icon(
  asset: Bitmap,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  padding: Padding = Padding.Unspecified,
  size: SizeUnit = SizeUnit.Unspecified,
  style: IconStyle = currentIcons.medium,
) = Icon(
  asset = BitmapDrawable(this.asLayout.resources, asset),
  modifier = modifier,
  color = color,
  padding = padding,
  size = size,
  style = style
)

/**
 * 创建一个 Icon 视图
 * @param asset 用 resId [Resources.getDrawable] 设置图标
 * @param modifier 对于 View 的其他额外调整
 * @param color [IconStyle.color]
 * @param padding 图标内边距
 * @param size [IconStyle.size]
 * @param style 图标样式 [IconStyle]
 * @see Image
 */
fun Ui.Icon(
  asset: Int,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  padding: Padding = Padding.Unspecified,
  size: SizeUnit = SizeUnit.Unspecified,
  style: IconStyle = currentIcons.medium,
) = Icon(
  asset = ContextCompat.getDrawable(this.asLayout.context, asset)!!,
  modifier = modifier,
  color = color,
  padding = padding,
  size = size,
  style = style
)