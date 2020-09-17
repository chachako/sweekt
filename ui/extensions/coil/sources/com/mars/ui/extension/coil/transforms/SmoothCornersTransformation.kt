@file:Suppress("NAME_SHADOWING")

package com.mars.ui.extension.coil.transforms

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


/*
 * author: 凛
 * date: 2020/9/15 下午10:27
 * github: https://github.com/oh-Rin
 * description: 平滑圆角的转换器
 */
class SmoothCornersTransformation(
  private val topLeft: CornerSize = ZeroCornerSize,
  private val topRight: CornerSize = ZeroCornerSize,
  private val bottomRight: CornerSize = ZeroCornerSize,
  private val bottomLeft: CornerSize = ZeroCornerSize,
) : Transformation {
  private var mCenterX = 0f
  private var mCenterY = 0f

  override fun key() =
    "${SmoothCornersTransformation::class.java.name}-$topLeft,$topRight,$bottomLeft,$bottomRight"

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

    mCenterX = outputWidth * 1.0f / 2
    mCenterY = outputHeight * 1.0f / 2

    val output = pool.get(outputWidth, outputHeight, input.config ?: Bitmap.Config.ARGB_8888)
    output.applyCanvas {
      drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

      val matrix = Matrix()
      matrix.setTranslate((outputWidth - input.width) / 2f, (outputHeight - input.height) / 2f)
      val shader = BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
      shader.setLocalMatrix(matrix)
      paint.shader = shader

      val path = Path()
      val width = width.toFloat()
      val height = height.toFloat()
      val posX: Float = mCenterX - width / 2
      val posY: Float = mCenterY - height / 2

      val rectF = RectF(0f, 0f, width, height)

      val topLeft = topLeft.getCornerSize(rectF)
      val topRight = topRight.getCornerSize(rectF)
      val bottomLeft = bottomLeft.getCornerSize(rectF)
      val bottomRight = bottomRight.getCornerSize(rectF)

      val trVertexRatio: Float = if (topRight / (width / 2).coerceAtMost(height / 2) > 0.5) {
        val percentage = (topRight / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
        val clampedPer = 1f.coerceAtMost(percentage)
        1f - (1 - 1.104f / 1.2819f) * clampedPer
      } else {
        1f
      }
      val brVertexRatio: Float = if (bottomRight / (width / 2).coerceAtMost(height / 2) > 0.5) {
        val percentage = (bottomRight / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
        val clampedPer = 1f.coerceAtMost(percentage)
        1f - (1 - 1.104f / 1.2819f) * clampedPer
      } else {
        1f
      }
      val tlVertexRatio: Float = if (topLeft / (width / 2).coerceAtMost(height / 2) > 0.5) {
        val percentage = (topLeft / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
        val clampedPer = 1f.coerceAtMost(percentage)
        1f - (1 - 1.104f / 1.2819f) * clampedPer
      } else {
        1f
      }
      val blVertexRatio: Float = if (bottomLeft / (width / 2).coerceAtMost(height / 2) > 0.5) {
        val percentage = (bottomLeft / (width / 2).coerceAtMost(height / 2) - 0.5f) / 0.4f
        val clampedPer = 1f.coerceAtMost(percentage)
        1f - (1 - 1.104f / 1.2819f) * clampedPer
      } else {
        1f
      }


      val tlControlRatio: Float = if (topLeft / (width / 2).coerceAtMost(height / 2) > 0.6) {
        val percentage = (topLeft / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
        val clampedPer = 1f.coerceAtMost(percentage)
        1 + (0.8717f / 0.8362f - 1) * clampedPer
      } else {
        1f
      }
      val trControlRatio: Float = if (topRight / (width / 2).coerceAtMost(height / 2) > 0.6) {
        val percentage = (topRight / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
        val clampedPer = 1f.coerceAtMost(percentage)
        1 + (0.8717f / 0.8362f - 1) * clampedPer
      } else {
        1f
      }
      val blControlRatio: Float = if (bottomLeft / (width / 2).coerceAtMost(height / 2) > 0.6) {
        val percentage = (bottomLeft / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
        val clampedPer = 1f.coerceAtMost(percentage)
        1 + (0.8717f / 0.8362f - 1) * clampedPer
      } else {
        1f
      }
      val brControlRatio: Float = if (bottomRight / (width / 2).coerceAtMost(height / 2) > 0.6) {
        val percentage = (bottomRight / (width / 2).coerceAtMost(height / 2) - 0.6f) / 0.3f
        val clampedPer = 1f.coerceAtMost(percentage)
        1 + (0.8717f / 0.8362f - 1) * clampedPer
      } else {
        1f
      }

      path.moveTo(posX + width / 2, posY)

      path.lineTo(
        posX + (width / 2).coerceAtLeast(width - topRight / 100.0f * 128.19f * trVertexRatio),
        posY
      )
      path.cubicTo(
        posX + width - topRight / 100f * 83.62f * trControlRatio,
        posY,
        posX + width - topRight / 100f * 67.45f,
        posY + topRight / 100f * 4.64f,
        posX + width - topRight / 100f * 51.16f,
        posY + topRight / 100f * 13.36f
      )
      path.cubicTo(
        posX + width - topRight / 100f * 34.86f,
        posY + topRight / 100f * 22.07f,
        posX + width - topRight / 100f * 22.07f,
        posY + topRight / 100f * 34.86f,
        posX + width - topRight / 100f * 13.36f,
        posY + topRight / 100f * 51.16f
      )
      path.cubicTo(
        posX + width - topRight / 100f * 4.64f,
        posY + topRight / 100f * 67.45f,
        posX + width,
        posY + topRight / 100f * 83.62f * trControlRatio,
        posX + width,
        posY + (height / 2).coerceAtMost(topRight / 100f * 128.19f * trVertexRatio)
      )

      path.lineTo(
        posX + width,
        posY + (height / 2).coerceAtLeast(height - bottomRight / 100f * 128.19f * brVertexRatio)
      )
      path.cubicTo(
        posX + width,
        posY + height - bottomRight / 100f * 83.62f * brControlRatio,
        posX + width - bottomRight / 100f * 4.64f,
        posY + height - bottomRight / 100f * 67.45f,
        posX + width - bottomRight / 100f * 13.36f,
        posY + height - bottomRight / 100f * 51.16f
      )
      path.cubicTo(
        posX + width - bottomRight / 100f * 22.07f,
        posY + height - bottomRight / 100f * 34.86f,
        posX + width - bottomRight / 100f * 34.86f,
        posY + height - bottomRight / 100f * 22.07f,
        posX + width - bottomRight / 100f * 51.16f,
        posY + height - bottomRight / 100f * 13.36f
      )
      path.cubicTo(
        posX + width - bottomRight / 100f * 67.45f,
        posY + height - bottomRight / 100f * 4.64f,
        posX + width - bottomRight / 100f * 83.62f * brControlRatio,
        posY + height,
        posX + (width / 2).coerceAtLeast(width - bottomRight / 100f * 128.19f * brVertexRatio),
        posY + height
      )

      path.lineTo(
        posX + (width / 2).coerceAtMost(bottomLeft / 100f * 128.19f * blControlRatio),
        posY + height
      )
      path.cubicTo(
        posX + bottomLeft / 100f * 83.62f * blControlRatio,
        posY + height,
        posX + bottomLeft / 100f * 67.45f,
        posY + height - bottomLeft / 100f * 4.64f,
        posX + bottomLeft / 100f * 51.16f,
        posY + height - bottomLeft / 100f * 13.36f
      )
      path.cubicTo(
        posX + bottomLeft / 100f * 34.86f,
        posY + height - bottomLeft / 100f * 22.07f,
        posX + bottomLeft / 100f * 22.07f,
        posY + height - bottomLeft / 100f * 34.86f,
        posX + bottomLeft / 100f * 13.36f,
        posY + height - bottomLeft / 100f * 51.16f
      )
      path.cubicTo(
        posX + bottomLeft / 100f * 4.64f,
        posY + height - bottomLeft / 100f * 67.45f,
        posX,
        posY + height - bottomLeft / 100f * 83.62f * blControlRatio,
        posX,
        posY + (height / 2).coerceAtLeast(height - bottomLeft / 100f * 128.19f * blVertexRatio)
      )

      path.lineTo(posX, posY + (height / 2).coerceAtMost(topLeft / 100f * 128.19f * tlVertexRatio))
      path.cubicTo(
        posX,
        posY + topLeft / 100f * 83.62f * tlControlRatio,
        posX + topLeft / 100f * 4.64f,
        posY + topLeft / 100f * 67.45f,
        posX + topLeft / 100f * 13.36f,
        posY + topLeft / 100f * 51.16f
      )
      path.cubicTo(
        posX + topLeft / 100f * 22.07f,
        posY + topLeft / 100f * 34.86f,
        posX + topLeft / 100f * 34.86f,
        posY + topLeft / 100f * 22.07f,
        posX + topLeft / 100f * 51.16f,
        posY + topLeft / 100f * 13.36f
      )
      path.cubicTo(
        posX + topLeft / 100f * 67.45f,
        posY + topLeft / 100f * 4.64f,
        posX + topLeft / 100f * 83.62f * tlControlRatio,
        posY,
        posX + (width / 2).coerceAtMost(topLeft / 100f * 128.19f * tlVertexRatio),
        posY
      )
      path.close()
      drawPath(path, paint)
    }

    return output
  }

  /* https://github.com/MartinRGB/sketch-smooth-corner-android/blob/master/app/src/main/java/com/example/martinrgb/roundconrner/smoothcorners/SketchSmoothCorners.java */
}