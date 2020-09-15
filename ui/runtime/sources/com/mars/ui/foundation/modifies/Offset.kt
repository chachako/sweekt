@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.foundation.modifies

import android.view.View
import android.view.ViewGroup
import com.mars.ui.core.Modifier
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toPxOrNull

/** 调整 View 的 xy 轴偏移 */
fun Modifier.offset(x: SizeUnit? = null, y: SizeUnit? = null) =
  +OffsetModifier(x, y)


/**
 * 布局偏移调整的具体实现
 * @see [View.setTranslationX]
 * @see [View.setTranslationY]
 */
private data class OffsetModifier(
  val x: SizeUnit? = null,
  val y: SizeUnit? = null,
) : Modifier {
  override fun realize(myself: View, parent: ViewGroup?) {
    x?.toPxOrNull()?.apply(myself::setTranslationX)
    y?.toPxOrNull()?.apply(myself::setTranslationY)
  }
}