package com.meowbase.toolkit.view

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.createBitmap
import com.meowbase.toolkit.other.logError

/**
 * Pixel copy to copy SurfaceView/VideoView into Bitmap
 */
@RequiresApi(Build.VERSION_CODES.N)
fun SurfaceView.pixelCopy(callback: (Bitmap?) -> Unit) {
  val bitmap = createBitmap(width, height)
  try {
    // Create a handler thread to offload the processing of the image.
    val handlerThread = HandlerThread("PixelCopier")
    handlerThread.start()
    PixelCopy.request(
      this, bitmap, { copyResult ->
        if (copyResult == PixelCopy.SUCCESS) {
          callback(bitmap)
        } else {
          callback(null)
        }
        handlerThread.quitSafely()
      },
      Handler(handlerThread.looper)
    )
  } catch (e: IllegalArgumentException) {
    // Don't crash
    callback(null)
    // PixelCopy may throw IllegalArgumentException, make sure to handle it
    e.printStackTrace()
  }
}

@RequiresApi(Build.VERSION_CODES.O)
fun View.pixelCopy(activity: Activity, callback: (Bitmap?) -> Unit) {
  activity.window?.let { window ->
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val locationOfViewInWindow = IntArray(2)
    getLocationInWindow(locationOfViewInWindow)
    try {
      // Create a handler thread to offload the processing of the image.
      val handlerThread = HandlerThread("PixelCopier")
      handlerThread.start()
      PixelCopy.request(
        window,
        Rect(
          locationOfViewInWindow[0],
          locationOfViewInWindow[1],
          locationOfViewInWindow[0] + width,
          locationOfViewInWindow[1] + height
        ), bitmap, { copyResult ->
          if (copyResult == PixelCopy.SUCCESS) {
            callback(bitmap)
          } else {
            callback(null)
            logError("PixelCopy onError callback")
          }
          handlerThread.quitSafely()
        },
        Handler(handlerThread.looper)
      )
    } catch (e: IllegalArgumentException) {
      // Don't crash
      callback(null)
      // PixelCopy may throw IllegalArgumentException, make sure to handle it
      e.printStackTrace()
    }
  }
}