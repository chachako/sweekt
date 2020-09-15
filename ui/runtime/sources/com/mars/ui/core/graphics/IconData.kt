@file:Suppress("FunctionName")

package com.mars.ui.core.graphics

import android.graphics.drawable.Drawable
import com.mars.ui.core.Padding
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.foundation.styles.IconStyle

/*
 * author: 凛
 * date: 2020/8/19 1:15 PM
 * github: https://github.com/oh-Rin
 * description: 图标数据
 */
data class IconData(
  val icon: Drawable,
  /** 图标颜色 */
  val color: Color = Color.Unset,
  /** 图标大小 */
  val size: SizeUnit = SizeUnit.Unspecified,
  /** 图标宽度，优先级比 [size] 要高 */
  val width: SizeUnit = size,
  /** 图标高度，优先级比 [size] 要高 */
  val height: SizeUnit = size,
  /** 图标的内边距 */
  val padding: Padding = Padding.Unspecified,
)

fun IconData(icon: Drawable, style: IconStyle) = style.toIconData(icon)

/** 将 [IconStyle] 转换为 [IconData] */
fun IconStyle.toIconData(icon: Drawable) = IconData(
  icon = icon,
  color = color,
  size = size,
  width = width,
  height = height,
  padding = padding
)