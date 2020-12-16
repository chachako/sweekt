@file:Suppress("DEPRECATION", "NON_EXHAUSTIVE_WHEN")

package com.meowbase.ui.extensions

import android.annotation.SuppressLint
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.annotation.RequiresApi
import com.meowbase.toolkit.addFlags
import com.meowbase.toolkit.removeFlags
import com.meowbase.ui.core.graphics.*

enum class Brightness {
  /** 暗色模式，Bar 内容皆为亮色 */
  Light,

  /** 亮色模式，Bar 内容皆为暗色 */
  Dark,

  /** 根据背景来决定亮度 */
  Auto,

  /** 使用当前默认 */
  Unset,
}

@RequiresApi(VERSION_CODES.R)
private fun Brightness.applyForR(isStatusBar: Boolean) {
  val flag = if (isStatusBar) APPEARANCE_LIGHT_STATUS_BARS else APPEARANCE_LIGHT_NAVIGATION_BARS
  val insetsController = Immersive.window.insetsController
  if (insetsController == null) {
    applyForM(isStatusBar)
    return
  }
  when (this) {
    Brightness.Light -> insetsController.setSystemBarsAppearance(flag, flag)
    Brightness.Dark -> insetsController.setSystemBarsAppearance(0, flag)
    // for Auto
    Brightness.Auto -> when {
      Color(Immersive.window.statusBarColor).isLight -> Brightness.Light.applyForR(isStatusBar)
      else -> Brightness.Dark.applyForR(isStatusBar)
    }
  }
}

@SuppressLint("InlinedApi")
@RequiresApi(VERSION_CODES.M)
private fun Brightness.applyForM(isStatusBar: Boolean) {
  val flag = if (isStatusBar) SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
  Immersive.windowView.also {
    val visibility = it.systemUiVisibility
    when (this) {
      Brightness.Light -> it.systemUiVisibility = visibility.addFlags(flag)
      Brightness.Dark -> it.systemUiVisibility = visibility.removeFlags(flag)
      // for Auto
      Brightness.Auto -> when {
        Color(Immersive.window.statusBarColor).isLight -> Brightness.Light.applyForM(isStatusBar)
        else -> Brightness.Dark.applyForM(isStatusBar)
      }
    }
  }
}

private fun Brightness.applyCompat(isStatusBar: Boolean) {
  when {
    isStatusBar -> {
      when (this) {
        Brightness.Light -> Immersive.window.statusBarColor = Color.Black.copy(0.2f).argb
        Brightness.Dark -> Immersive.window.statusBarColor = Color.White.copy(0.3f).argb
        // for Auto
        Brightness.Auto -> Immersive.window.statusBarColor =
          Immersive.window.statusBarColor.toColor().run {
            when {
              isLight -> Immersive.window.statusBarColor.toColor().darken(0.2f).argb
              else -> Immersive.window.statusBarColor.toColor().lighten(0.2f).argb
            }
          }
      }
    }
    else -> {
      when (this) {
        Brightness.Light -> Immersive.window.navigationBarColor = Color.Black.copy(0.2f).argb
        Brightness.Dark -> Immersive.window.navigationBarColor = Color.White.copy(0.3f).argb
        // for Auto
        Brightness.Auto -> Immersive.window.navigationBarColor =
          Immersive.window.navigationBarColor.toColor().run {
            when {
              isLight -> Immersive.window.navigationBarColor.toColor().darken(0.2f).argb
              else -> Immersive.window.navigationBarColor.toColor().lighten(0.2f).argb
            }
          }
      }
    }
  }
}

/** 将亮度变化应用到系统栏上 */
internal fun Brightness.apply(isStatusBar: Boolean) = when {
  VERSION.SDK_INT >= VERSION_CODES.R -> applyForR(isStatusBar)
  VERSION.SDK_INT >= VERSION_CODES.O -> applyForM(isStatusBar)
  // O 以下需要对亮色导航栏做一些适配
  VERSION.SDK_INT >= VERSION_CODES.M ->
    if (isStatusBar) applyForM(isStatusBar) else applyCompat(false)
  else -> applyCompat(isStatusBar)
}


