@file:Suppress("FunctionName", "ClassName", "NestedLambdaShadowedImplicitParameter")

package com.mars.ui.foundation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.mars.ui.UiKit
import com.mars.ui.core.Modifier
import com.mars.ui.core.Orientation
import com.mars.ui.core.native
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.useOrNull
import com.mars.ui.foundation.modifies.margin

/** 按钮栏 */
class _ButtonBar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : Linear(context, attrs, defStyleAttr, defStyleRes), ButtonBar

/** 定义一个按钮栏区域，以限制 Block 块内只能使用 Button */
interface ButtonBar

/**
 * 由多个按钮组成的按钮栏
 * @see Row
 * @see Column
 */
inline fun UiKit.ButtonBar(
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
  it.captured.forEachIndexed { index, view ->
    if (view is ButtonUI) view.setOnClickListener { onClick(index, view) }
    spaceModifier?.realize(view, it)
  }
  it.endCapture()
}