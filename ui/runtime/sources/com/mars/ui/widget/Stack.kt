@file:Suppress("FunctionName", "OverridingDeprecatedMember")

package com.mars.ui.widget

import android.widget.FrameLayout
import com.mars.ui.Ui
import com.mars.ui.core.Modifier
import com.mars.ui.widget.implement.*

/**
 * 一个盒子布局 [Ui.Row]
 * 可让子视图在其中堆叠、平放
 *
 * @see FrameLayout
 */
inline fun Ui.Box(
  modifier: Modifier = Modifier,
  children: Box.() -> Unit
): Box = With(::Box) {
  it.modifier = modifier
  it.children()
}
