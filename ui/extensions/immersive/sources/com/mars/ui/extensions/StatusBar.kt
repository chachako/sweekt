@file:Suppress("FunctionName", "DEPRECATION")

package com.mars.ui.extensions

import android.view.View
import androidx.annotation.FloatRange
import androidx.core.view.WindowInsetsCompat
import com.gyf.immersionbar.ImmersionBar
import com.mars.toolkit.addFlags
import com.mars.toolkit.content.windowView
import com.mars.ui.UiKit.Companion.currentActivity
import com.mars.ui.core.graphics.Color

class StatusBar {
  internal var compatMask: Float = 0.4f
  internal var brightness: Brightness = Brightness.Auto
  internal var hide: Boolean = false
  private val windowView = currentActivity.windowView

  fun immerse() {
    windowView.systemUiVisibility
    val uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    currentActivity.windowView.systemUiVisibility
//    WindowInsetsCompat.toWindowInsetsCompat()
//    uiFlags.addFlags()
  }
}

fun StatusBar(
  color: Color = Color.Transparent,
  @FloatRange(from = .0, to = 1.0) compatMask: Float = 0.4f,
  brightness: Brightness = Brightness.Auto,
  hide: Boolean = false,
) = StatusBar().also {
//  it.color = color
  it.compatMask = compatMask
  it.brightness = brightness
  it.hide = hide
}


