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