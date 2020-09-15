@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.foundation.modifies

import android.view.View
import android.view.ViewGroup
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
  start = horizontal,
  top = vertical,
  end = horizontal,
  bottom = vertical,
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
  val start: SizeUnit? = null,
  val top: SizeUnit? = null,
  val end: SizeUnit? = null,
  val bottom: SizeUnit? = null,
) : Modifier {
  override fun realize(myself: View, parent: ViewGroup?) {
    myself.layoutParams = when (val lp = myself.layoutParams) {
      is ViewGroup.MarginLayoutParams -> updateLayoutParams(lp)
      null -> updateLayoutParams(
        ViewGroup.MarginLayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT,
        )
      )
      else -> updateLayoutParams(ViewGroup.MarginLayoutParams(lp))
    }
  }

  fun updateLayoutParams(lp: ViewGroup.MarginLayoutParams) = lp.apply {
    start?.toIntPxOrNull()?.also(::setMarginStart)
    end?.toIntPxOrNull()?.also(::setMarginEnd)
    top?.toIntPxOrNull()?.also { topMargin = it }
    bottom?.toIntPxOrNull()?.also { bottomMargin = it }
  }
}