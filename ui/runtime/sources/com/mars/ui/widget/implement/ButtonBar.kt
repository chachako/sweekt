@file:Suppress("FunctionName", "ClassName", "NestedLambdaShadowedImplicitParameter")

package com.mars.ui.widget.implement

import android.content.Context
import android.util.AttributeSet
import com.mars.ui.Ui

/** 按钮栏 */
class _ButtonBar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : Linear(context, attrs, defStyleAttr, defStyleRes), ButtonBar

/** 定义一个按钮栏区域，以限制 Block 块内只能使用 Button */
interface ButtonBar : Ui