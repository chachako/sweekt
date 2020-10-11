@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.widget.modifier

import android.view.View
import android.view.ViewGroup
import com.mars.toolkit.widget.LayoutParams
import com.mars.ui.core.LayoutSize
import com.mars.ui.core.Modifier
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toIntPxOrNull

/** 调整 View 的高宽为 [size] */
fun Modifier.size(size: SizeUnit) = size(size, size)

/** 调整 View 的大小 */
fun Modifier.size(width: SizeUnit, height: SizeUnit) =
  +LayoutSizeModifier(width, height)

/** 调整 View 的宽度 */
fun Modifier.width(width: SizeUnit) =
  +LayoutSizeModifier(w = width)

/** 调整 View 的高度 */
fun Modifier.height(height: SizeUnit) =
  +LayoutSizeModifier(h = height)

/** 根据 View 的内容决定大小 */
fun Modifier.wrapContent() =
  size(LayoutSize.Wrap, LayoutSize.Wrap)

/** 根据 View 的内容决定宽度 */
fun Modifier.wrapContentWidth() =
  width(LayoutSize.Wrap)

/** 根据 View 的内容决定高度 */
fun Modifier.wrapContentHeight() =
  height(LayoutSize.Wrap)

/** View 填满父布局大小 */
fun Modifier.matchParent() =
  size(LayoutSize.Match, LayoutSize.Match)

/** 根据 View 的内容决定宽度 */
fun Modifier.matchParentWidth() =
  width(LayoutSize.Match)

/** 根据 View 的内容决定高度 */
fun Modifier.matchParentHeight() =
  height(LayoutSize.Match)


/** 布局大小调整的具体实现 [ViewGroup.LayoutParams] */
private data class LayoutSizeModifier(
  val w: SizeUnit? = null,
  val h: SizeUnit? = null,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    layoutParams = LayoutParams {
      w?.toIntPxOrNull()?.also { width = it }
      h?.toIntPxOrNull()?.also { height = it }
    }
  }
}