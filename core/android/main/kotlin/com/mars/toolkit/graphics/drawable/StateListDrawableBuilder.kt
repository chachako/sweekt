package com.mars.toolkit.graphics.drawable

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.StateSet
import android.view.View

fun buildStateDrawable(block: StateListDrawableBuilder.() -> Unit) =
  StateListDrawableBuilder().apply(block).build()

fun View.drawBackgroundByState(block: StateListDrawableBuilder.() -> Unit) {
  background = StateListDrawableBuilder().apply(block).build()
}

fun View.drawForegroundByState(block: StateListDrawableBuilder.() -> Unit) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    foreground = StateListDrawableBuilder().apply(block).build()
  }
}

class StateListDrawableBuilder {
  private var pressed: Drawable? = null
  private var disabled: Drawable? = null
  private var selected: Drawable? = null
  private var normal: Drawable = ColorDrawable(Color.TRANSPARENT)

  fun pressed(pressed: Drawable?) = apply { this.pressed = pressed }
  fun disabled(disabled: Drawable?) = apply { this.disabled = disabled }
  fun selected(selected: Drawable?) = apply { this.selected = selected }
  fun normal(normal: Drawable) = apply { this.normal = normal }

  fun build(): StateListDrawable {
    val stateListDrawable = StateListDrawable()
    setupStateListDrawable(stateListDrawable)
    return stateListDrawable
  }

  private fun setupStateListDrawable(stateListDrawable: StateListDrawable) {
    pressed?.let {
      stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), it)
    }
    disabled?.let {
      stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), it)
    }
    selected?.let {
      stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), it)
    }
    stateListDrawable.addState(StateSet.WILD_CARD, normal)
  }
}