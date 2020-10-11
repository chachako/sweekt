package com.mars.ui.core.decoupling

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.core.content.ContextCompat

/*
 * author: 凛
 * date: 2020/8/18 12:10 PM
 * github: https://github.com/oh-Rin
 * description: 提供前景支持
 */
interface ForegroundProvider {
  var foregroundSupport: Drawable?

  fun setSupportForegroundResource(resId: Int) {
    ContextCompat.getDrawable((this as View).context, resId)?.apply(::setSupportForeground)
  }

  fun setSupportForeground(drawable: Drawable) {
    (this as View).apply {
      foregroundSupport = drawable
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        foreground = foregroundSupport
      } else {
        background = LayerDrawable(
          arrayOf(
            background,
            foregroundSupport
          )
        )
      }
    }
  }

  fun getSupportForeground() = foregroundSupport
}