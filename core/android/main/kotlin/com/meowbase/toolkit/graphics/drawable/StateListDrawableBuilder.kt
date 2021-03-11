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

package com.meowbase.toolkit.graphics.drawable

import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.StateSet
import android.view.View

inline fun buildStateDrawable(block: StateListDrawableBuilder.() -> Unit) =
  StateListDrawableBuilder().apply(block).build()

inline fun View.drawBackgroundByState(block: StateListDrawableBuilder.() -> Unit) {
  background = StateListDrawableBuilder().apply(block).build()
}

inline fun View.drawForegroundByState(block: StateListDrawableBuilder.() -> Unit) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    foreground = StateListDrawableBuilder().apply(block).build()
  }
}

class StateListDrawableBuilder {
  private val stateListDrawable = StateListDrawable()

  fun add(state: Int, drawable: Drawable?) = apply {
    stateListDrawable.addState(intArrayOf(state), drawable)
  }

  fun add(states: IntArray, drawable: Drawable?) = apply {
    stateListDrawable.addState(states, drawable)
  }

  fun pressed(pressed: Drawable?) = apply {
    pressed?.let {
      stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), it)
    }
  }

  fun enabled(enabled: Drawable?) = apply {
    enabled?.let {
      stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), it)
    }
  }

  fun disabled(disabled: Drawable?) = apply {
    disabled?.let {
      stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), it)
    }
  }

  fun selected(selected: Drawable?) = apply {
    selected?.let {
      stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), it)
    }
  }

  fun normal(normal: Drawable) = apply {
    stateListDrawable.addState(StateSet.WILD_CARD, normal)
  }

  fun build(): StateListDrawable = stateListDrawable
}