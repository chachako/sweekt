@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.foundation.modifies

import android.view.View
import android.view.ViewGroup
import com.mars.toolkit.view.marginBottom
import com.mars.toolkit.widget.MarginLayoutParams
import com.mars.ui.core.Modifier
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toIntPxOrNull

/** 单独调整 View 离父布局的四个方向的距离 */
fun Modifier.margin(
  start: SizeUnit? = null,
  top: SizeUnit? = null,
  end: SizeUnit? = null,
  bottom: SizeUnit? = null,
) = +MarginModifier(start, top, end, bottom)

/** 调整 View 离父布局的横纵向距离 */
fun Modifier.margin(
  horizontal: SizeUnit? = null,
  vertical: SizeUnit? = null,
) = +MarginModifier(
  _start = horizontal,
  _top = vertical,
  _end = horizontal,
  _bottom = vertical,
)

/** 调整 View 离父布局的四个方向的距离 */
fun Modifier.margin(all: SizeUnit) =
  margin(all, all)

/** 调整 View 离父布局的左右边缘的距离 */
fun Modifier.marginHorizontal(size: SizeUnit) =
  margin(horizontal = size)

/** 调整 View 离父布局的上下边缘的距离 */
fun Modifier.marginVertical(size: SizeUnit) =
  margin(vertical = size)


/** View 自身离父布局的边距调整的具体实现 */
private data class MarginModifier(
  val _start: SizeUnit? = null,
  val _top: SizeUnit? = null,
  val _end: SizeUnit? = null,
  val _bottom: SizeUnit? = null,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    MarginLayoutParams {
      _start?.toIntPxOrNull()?.also(::setMarginStart)
      _end?.toIntPxOrNull()?.also(::setMarginEnd)
      _top?.toIntPxOrNull()?.also { topMargin = it }
      _bottom?.toIntPxOrNull()?.also { marginBottom = it }
    }
  }
}