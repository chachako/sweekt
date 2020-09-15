@file:Suppress("unused")

package com.mars.ui.extension.transforms

import android.graphics.*
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.decode.DecodeUtils
import coil.size.OriginalSize
import coil.size.PixelSize
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import com.google.android.material.shape.CornerSize
import com.mars.ui.core.graphics.shape.ZeroCornerSize
import kotlin.math.roundToInt

/**
 * A [Transformation] that crops the image to fit the target's dimensions and rounds the corners of the image.
 *
 * @param topLeft The radius for the top left corner.
 * @param topRight The radius for the top right corner.
 * @param bottomLeft The radius for the bottom left corner.
 * @param bottomRight The radius for the bottom right corner.
 */
class RoundedCornersTransformation(
  private val topLeft: CornerSize = ZeroCornerSize,
  private val topRight: CornerSize = ZeroCornerSize,
  private val bottomRight: CornerSize = ZeroCornerSize,
  private val bottomLeft: CornerSize = ZeroCornerSize,
) : Transformation {

  override fun key() =
    "${RoundedCornersTransformation::class.java.name}-$topLeft,$topRight,$bottomLeft,$bottomRight"

  override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    val outputWidth: Int
    val outputHeight: Int
    when (size) {
      is PixelSize -> {
        val multiplier = DecodeUtils.computeSizeMultiplier(
          srcWidth = input.width,
          srcHeight = input.height,
          dstWidth = size.width,
          dstHeight = size.height,
          scale = Scale.FILL
        )
        outputWidth = (size.width / multiplier).roundToInt()
        outputHeight = (size.height / multiplier).roundToInt()
      }
      is OriginalSize -> {
        outputWidth = input.width
        outputHeight = input.height
      }
    }

    val output = pool.get(outputWidth, outputHeight, input.config ?: Bitmap.Config.ARGB_8888)
    output.applyCanvas {
      drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

      val matrix = Matrix()
      matrix.setTranslate((outputWidth - input.width) / 2f, (outputHeight - input.height) / 2f)
      val shader = BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
      shader.setLocalMatrix(matrix)
      paint.shader = shader

      val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
      val topLeft = topLeft.getCornerSize(rect)
      val topRight = topRight.getCornerSize(rect)
      val bottomLeft = bottomLeft.getCornerSize(rect)
      val bottomRight = bottomRight.getCornerSize(rect)
      val radii = floatArrayOf(
        topLeft,
        topLeft,
        topRight,
        topRight,
        bottomRight,
        bottomRight,
        bottomLeft,
        bottomLeft
      )
      val path = Path().apply { addRoundRect(rect, radii, Path.Direction.CW) }
      drawPath(path, paint)
    }

    return output
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    return other is RoundedCornersTransformation &&
      topLeft == other.topLeft &&
      topRight == other.topRight &&
      bottomLeft == other.bottomLeft &&
      bottomRight == other.bottomRight
  }

  override fun hashCode(): Int {
    var result = topLeft.hashCode()
    result = 31 * result + topRight.hashCode()
    result = 31 * result + bottomLeft.hashCode()
    result = 31 * result + bottomRight.hashCode()
    return result
  }

  override fun toString(): String {
    return "RoundedCornersTransformation(topLeft=$topLeft, topRight=$topRight, " +
      "bottomLeft=$bottomLeft, bottomRight=$bottomRight)"
  }
}
