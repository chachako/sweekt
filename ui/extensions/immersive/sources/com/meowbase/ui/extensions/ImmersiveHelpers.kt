@file:Suppress("DEPRECATION")

package com.meowbase.ui.extensions

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS


internal fun setLightStatusBar(activity: Activity) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    activity.window.insetsController?.setSystemBarsAppearance(
      APPEARANCE_LIGHT_STATUS_BARS,
      APPEARANCE_LIGHT_STATUS_BARS
    )
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    var flags = activity.window.decorView.systemUiVisibility
    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    activity.window.decorView.systemUiVisibility = flags
  }
}

private fun removeLightStatusBarIfSupported(activity: Activity) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    activity.window.insetsController?.setSystemBarsAppearance(
      0,
      WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
    )
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    var flags = activity.window.decorView.systemUiVisibility
    flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    activity.window.decorView.systemUiVisibility = flags
  }
}

private fun setLightNavigationBarIfSupported(activity: Activity) {
  when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
      activity.window.insetsController?.setSystemBarsAppearance(
        APPEARANCE_LIGHT_NAVIGATION_BARS,
        APPEARANCE_LIGHT_NAVIGATION_BARS
      )
    }
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
      var flags = activity.window.decorView.systemUiVisibility
      flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      activity.window.decorView.systemUiVisibility = flags
    }
    else -> {
    }
  }
}

@Suppress("deprecation")
private fun removeLightNavigationBarIfSupported(activity: Activity) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    activity.window.insetsController?.setSystemBarsAppearance(
      0,
      APPEARANCE_LIGHT_NAVIGATION_BARS
    )
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    var flags = activity.window.decorView.systemUiVisibility
    flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    activity.window.decorView.systemUiVisibility = flags
  }
}