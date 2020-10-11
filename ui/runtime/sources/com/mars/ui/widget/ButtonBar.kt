@file:Suppress("FunctionName", "ClassName", "NestedLambdaShadowedImplicitParameter")

package com.mars.ui.widget

import android.view.View
import com.mars.ui.Ui
import com.mars.ui.core.Modifier
import com.mars.ui.core.Orientation
import com.mars.ui.core.native
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.useOrNull
import com.mars.ui.widget.implement.*
import com.mars.ui.widget.modifier.margin

/**
 * 由多个按钮组成的按钮栏
 *
 * @param children 内只能定义按钮
 * @see Row
 * @see Column
 */
inline fun Ui.ButtonBar(
  /** 在按钮栏内部按钮按下后的回调 */
  crossinline onClick: (index: Int, button: View) -> Unit,
  /** 对于按钮栏的其他额外调整 */
  modifier: Modifier = Modifier,
  /** 按钮内控件的摆放方向 */
  orientation: Orientation = Orientation.Horizontal,
  /** 多个按钮之间的间隙大小 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** 按钮栏里面的按钮 */
  children: ButtonBar.() -> Unit
) = With(::_ButtonBar) {
  it.orientation = orientation.native
  it.modifier = modifier

  it.startCapture()
  it.children()
  val spaceModifier = spaceBetween.useOrNull()?.let {
    when (orientation) {
      Orientation.Horizontal -> Modifier.margin(start = it)
      else -> Modifier.margin(top = it)
    }
  }
  it.captured?.forEachIndexed { index, view ->
    if (view is ButtonUi) view.setOnClickListener { onClick(index, view) }
    spaceModifier?.apply { view.realize(it) }
  }
  it.endCapture()
}