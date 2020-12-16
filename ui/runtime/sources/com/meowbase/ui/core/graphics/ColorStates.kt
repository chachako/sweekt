package com.meowbase.ui.core.graphics

import android.content.res.ColorStateList
import android.util.StateSet
import com.meowbase.toolkit.data.ColorState
import com.meowbase.toolkit.iterations.toList
import com.meowbase.ui.Ui
import com.meowbase.ui.theme.Colors.Companion.resolveColor

class ColorStates {
  private val colorStates = mutableMapOf<IntArray, Color>()
  internal var singleColor: Color? = null

  fun add(color: Color, state: ColorState) {
    colorStates[state.res?.let { intArrayOf(it) } ?: StateSet.WILD_CARD] = color
  }

  fun add(color: Color, states: Array<ColorState>) {
    colorStates[states.mapNotNull { it.res }.toIntArray()] = color
  }

  fun add(color: Color, states: Sequence<ColorState>) = add(color, states.toList())

  fun add(color: Color, states: Iterable<ColorState>) = add(color, states.toList().toTypedArray())

  operator fun set(state: ColorState, color: Color) = add(color, state)

  operator fun set(color: Color, state: ColorState) = add(color, state)

  operator fun set(color: Color, state: Array<ColorState>) = add(color, state)

  operator fun set(color: Color, state: Sequence<ColorState>) = add(color, state)

  operator fun set(color: Color, state: Iterable<ColorState>) = add(color, state)

  infix fun ColorState.to(color: Color) = add(color, this)

  infix fun Color.to(state: ColorState) = add(this, state)

  infix fun Color.to(states: Array<ColorState>) = add(this, states)

  infix fun Color.to(states: Sequence<ColorState>) = add(this, states)

  infix fun Color.to(states: Iterable<ColorState>) = add(this, states)

  fun toColorStateList(): ColorStateList =
    singleColor?.argb?.let { ColorStateList.valueOf(it) }
      ?: ColorStateList(
        colorStates.keys.toTypedArray(),
        colorStates.values.map { it.argb }.toIntArray(),
      )

  fun updateColors(ui: Ui) = apply {
    singleColor?.resolveColor(ui)?.also { singleColor = it }
    colorStates.keys.forEach {
      colorStates[it]?.resolveColor(ui)?.apply {
        colorStates[it] = this
      }
    }
  }
}

inline fun buildColorStates(block: ColorStates.() -> Unit): ColorStates =
  ColorStates().apply(block)

fun Color.toColorStates() = ColorStates().apply {
  singleColor = this@toColorStates
}