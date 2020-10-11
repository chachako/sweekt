package com.mars.toolkit.content.res

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