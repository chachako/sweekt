package com.meowbase.toolkit.graphics

import android.graphics.*

fun Canvas.drawBitmap(bitmap: Bitmap, outRect: Rect, paint: Paint? = null) =
  drawBitmap(bitmap, Rect(0, 0, bitmap.width, bitmap.height), outRect, paint)

fun Canvas.drawBitmap(bitmap: Bitmap, outRect: RectF, paint: Paint? = null) =
  drawBitmap(bitmap, Rect(0, 0, bitmap.width, bitmap.height), outRect, paint)