/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

@file:Suppress("FunctionName", "ClassName", "NestedLambdaShadowedImplicitParameter")

package com.meowbase.ui.widget

import android.view.View
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Orientation
import com.meowbase.ui.core.native
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.useOrNull
import com.meowbase.ui.widget.implement.*
import com.meowbase.ui.widget.modifier.margin

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