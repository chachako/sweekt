@file:Suppress("MemberVisibilityCanBePrivate", "FunctionName")

package com.mars.ui.foundation.styles

import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toPxOrNull
import com.mars.ui.core.unit.useOrElse
import com.mars.ui.foundation.Divider
import com.mars.ui.theme.Colors
import com.mars.ui.theme.Style
import com.mars.ui.theme.Styles
import com.mars.ui.theme.Styles.Companion.resolveStyle

/*
 * author: 凛
 * date: 2020/8/11 3:39 PM
 * github: https://github.com/oh-Rin
 * description: 分割线通用样式
 */
data class DividerStyle(
  /** 文本颜色, 默认 [Colors.onSurface] */
  internal val color: Color = Color.Unset,

  /** 文本字体 */
  internal val thickness: SizeUnit = SizeUnit.Unspecified,

  /** 开头缩进 */
  internal val startIndent: SizeUnit = SizeUnit.Unspecified,

  /** 结尾缩进 */
  internal val endIndent: SizeUnit = SizeUnit.Unspecified,

  /** 双向缩进，优先于 [startIndent] [endIndent] */
  internal val indent: SizeUnit? = null,
) : Style<DividerStyle> {
  /** [Styles.resolveStyle] */
  override var id: Int = -1

  fun apply(view: Divider) {
    if (indent != null) indent.toPxOrNull()?.also {
      view.startIndent = it
      view.endIndent = it
    } else {
      startIndent.toPxOrNull()?.also { view.startIndent = it }
      endIndent.toPxOrNull()?.also { view.endIndent = it }
    }
    view.thickness = thickness
    view.color = color
  }

  /** 将当前 [DividerStyle] 与 [other] 进行合并后得到一个新的样式 */
  fun merge(other: DividerStyle): DividerStyle = DividerStyle(
    color = other.color.useOrElse { this.color },
    thickness = other.thickness.useOrElse { this.thickness },
    startIndent = other.startIndent.useOrElse { this.startIndent },
    endIndent = other.endIndent.useOrElse { this.endIndent },
    indent = if (other.indent == SizeUnit.Unspecified) this.indent else other.indent,
  ).also { if (this != it) it.id = id }

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }
}