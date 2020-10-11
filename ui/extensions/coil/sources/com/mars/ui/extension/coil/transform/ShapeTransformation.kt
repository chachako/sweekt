@file:Suppress("unused")

package com.mars.ui.extension.coil.transform

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.CircleCropTransformation
import coil.transform.Transformation
import com.mars.ui.core.graphics.shape.Shape

/**
 * 一个用于裁剪图片为给定的形状的转换类
 * @param shape 需要裁剪的形状
 */
data class ShapeTransformation(private val shape: Shape) : Transformation {
  override fun key(): String = CircleCropTransformation::class.java.name

  override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    return pool.get(
      width = input.width,
      height = input.height,
      config = input.config ?: Bitmap.Config.ARGB_8888
    ).applyCanvas {
      val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
      val outline = shape.getOutline(rect)

      if (outline.usePath) {
        drawPath(outline.forcePath, paint)
      } else when {
        outline.isOval -> drawOval(rect, paint)
        outline.rectRadius == 0F -> drawRect(rect, paint)
        else -> drawRoundRect(rect, outline.rectRadius, outline.rectRadius, paint)
      }
    }
  }
}
