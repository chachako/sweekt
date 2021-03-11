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

@file:Suppress("OverridingDeprecatedMember")

package com.meowbase.ui.widget.modifier

import android.view.View
import android.view.ViewGroup
import com.meowbase.toolkit.view.marginBottom
import com.meowbase.toolkit.widget.MarginLayoutParams
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toIntPxOrNull

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
) = margin(
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