package com.meowbase.toolkit.graphics.drawable

import android.graphics.drawable.Drawable

class FlipDrawableBuilder : DrawableWrapperBuilder<FlipDrawableBuilder>() {

  private var orientation: Int = FlipDrawable.ORIENTATION_HORIZONTAL

  fun orientation(orientation: Int) = apply { this.orientation = orientation }

  override fun build(): Drawable {
    return FlipDrawable(drawable!!, orientation)
  }
}