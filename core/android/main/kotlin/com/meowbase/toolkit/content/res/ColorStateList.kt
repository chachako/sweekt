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

package com.meowbase.toolkit.content.res

import android.content.res.ColorStateList
import android.util.StateSet

inline fun buildColorStates(block: ColorStateListBuilder.() -> Unit) =
  ColorStateListBuilder().apply(block).build()

class ColorStateListBuilder {
  private val stateAndColor = mutableMapOf<IntArray, Int>()

  fun add(state: Int, color: Int) = apply {
    stateAndColor[intArrayOf(state)] = color
  }

  fun add(states: IntArray, color: Int) = apply {
    stateAndColor[states] = color
  }

  fun pressed(pressed: Int) = add(android.R.attr.state_pressed, pressed)

  fun enabled(enabled: Int) = add(android.R.attr.state_enabled, enabled)

  fun disabled(disabled: Int) = add(-android.R.attr.state_enabled, disabled)

  fun selected(selected: Int) = add(android.R.attr.state_selected, selected)

  fun normal(normal: Int) = add(StateSet.WILD_CARD, normal)

  fun build(): ColorStateList = ColorStateList(
    stateAndColor.keys.toTypedArray(),
    stateAndColor.values.toIntArray(),
  )
}