@file:Suppress("FunctionName", "OverridingDeprecatedMember")

package com.meowbase.ui.widget

import android.widget.FrameLayout
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.widget.implement.*

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
