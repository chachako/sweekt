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
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toPxOrNull

/** 调整 View 的 xy 轴偏移 */
fun Modifier.offset(x: SizeUnit? = null, y: SizeUnit? = null) =
  +OffsetModifier(x, y)


/**
 * 布局偏移调整的具体实现
 * @see [View.setTranslationX]
 * @see [View.setTranslationY]
 */
private data class OffsetModifier(
  val _x: SizeUnit? = null,
  val _y: SizeUnit? = null,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    _x?.toPxOrNull()?.apply(::setTranslationX)
    _y?.toPxOrNull()?.apply(::setTranslationY)
  }
}