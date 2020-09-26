@file:Suppress("OverridingDeprecatedMember", "RestrictedApi")

package com.mars.ui.foundation.modifies

import android.annotation.TargetApi
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.mars.toolkit.graphics.drawable.LayerDrawableCompat
import com.mars.toolkit.graphics.drawable.buildLayerDrawable
import com.mars.toolkit.int
import com.mars.toolkit.view.clipOutline
import com.mars.ui.core.Modifier
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.RectangleShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.unit.*
import kotlin.math.roundToInt
import com.google.android.material.shadow.ShadowRenderer as _ShadowRenderer


/**
 * 将水波纹 [RippleDrawable] 设置为视图的前景
 *
 * @param color 涟漪颜色
 * @param shape 涟漪的遮罩形状，默认为 [RectangleShape]
 * @param radius 默认使用 [View] 的大小
 */
fun Modifier.shadow(
  color: Color = Color.Unset,
  shape: Shape? = null,
  radius: SizeUnit = SizeUnit.Unspecified,
  offset: SizeUnit = SizeUnit.Unspecified,
  rotation: Number = 0,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 0.14f,
) = +ShadowModifier(color, shape, radius, offset, rotation, alpha)


private class ShadowModifier(
  val _color: Color = Color.Unset,
  val _shape: Shape? = null,
  val _radius: SizeUnit = SizeUnit.Unspecified,
  val _offset: SizeUnit = SizeUnit.Unspecified,
  val _rotation: Number = 0,
  val _alpha: Float = 0.14f,
) : Modifier, UpdatableModifier {
  private val _elevation = _radius.useOrElse { 6.dp }.toPx()
  override fun View.realize(parent: ViewGroup?) {
    parent?.clipChildren = false
    parent?.clipToPadding = false

    background = (background as? LayerDrawableCompat)?.apply {
      val shapeModel = _shape?.toModelBuilder()?.build()
        ?: mLayerState.mChildren
          ?.mapNotNull { it?.mDrawable }
          ?.filterIsInstance<MaterialShapeDrawable>()
          ?.first()?.shapeAppearanceModel
        ?: RectangleShape.toModelBuilder().build()

      if (getDrawable(0) is Drawable) {
        setDrawable(0, resolveDrawable(shapeModel))
      }
    } ?: buildLayerDrawable {
      val shapeModel = _shape?.toModelBuilder()?.build()
        ?: (background as? MaterialShapeDrawable)?.shapeAppearanceModel
        ?: RectangleShape.toModelBuilder().build()

      add(resolveDrawable(shapeModel))
      background?.apply(::add)
    }
  }

  override fun View.update(parent: ViewGroup?) = realize(parent)

  fun resolveDrawable(model: ShapeAppearanceModel) = Drawable((_alpha * 255).roundToInt(), model).apply {
    _offset.toIntPxOrNull()?.also { shadowVerticalOffset = it }
    elevation = _elevation
    shadowCompatRotation = _rotation.int
    fillColor = ColorStateList.valueOf(0x00000000)
    shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
    setShadowColor(_color.useOrElse { Color.Black }.argb)
  }

  private class Drawable(
    alpha: Int,
    shapeAppearanceModel: ShapeAppearanceModel = ShapeAppearanceModel()
  ) : MaterialShapeDrawable(shapeAppearanceModel) {
    init {
      MaterialShapeDrawable::class.java.getDeclaredField("shadowRenderer").apply {
        isAccessible = true
      }.set(this, ShadowRenderer(alpha))
    }

    class ShadowRenderer(val _alpha: Int) : _ShadowRenderer() {
      private var paint: Paint? = null
      private val cornerShadowPaint: Paint = Paint(Paint.DITHER_FLAG).apply {
        style = Paint.Style.FILL
        alpha = _alpha
      }
      private val edgeShadowPaint: Paint = Paint(cornerShadowPaint)
      private var shadowStartColor = 0
      private var shadowMiddleColor = 0
      private var shadowEndColor = 0
      private val scratch = Path()
      private val transparentPaint = Paint().apply { color = 0 }

      override fun setShadowColor(color: Int) {
        if (paint == null) paint = Paint()
        shadowStartColor = color
        shadowMiddleColor = ColorUtils.setAlphaComponent(shadowStartColor, (255 * 0.5).roundToInt())
        shadowEndColor = 0
        paint!!.color = shadowStartColor
        paint!!.alpha = _alpha
      }

      /** Draws an edge shadow on the canvas in the current bounds with the matrix transform applied.  */
      override fun drawEdgeShadow(
        canvas: Canvas, transform: Matrix?, bounds: RectF, elevation: Int
      ) {
        bounds.bottom += elevation.toFloat()
        bounds.offset(0f, -elevation.toFloat())
        edgeColors[0] = shadowEndColor
        edgeColors[1] = shadowMiddleColor
        edgeColors[2] = shadowStartColor
        edgeShadowPaint.shader = LinearGradient(
          bounds.left,
          bounds.top,
          bounds.left,
          bounds.bottom,
          edgeColors,
          edgePositions,
          Shader.TileMode.CLAMP
        )
        canvas.save()
        canvas.concat(transform)
        canvas.drawRect(bounds, edgeShadowPaint)
        canvas.restore()
      }

      /**
       * Draws a corner shadow on the canvas in the current bounds with the matrix transform applied.
       */
      override fun drawCornerShadow(
        canvas: Canvas,
        matrix: Matrix?,
        bounds: RectF,
        elevation: Int,
        startAngle: Float,
        sweepAngle: Float
      ) {
        val drawShadowInsideBounds = sweepAngle < 0
        val arcBounds = scratch
        if (drawShadowInsideBounds) {
          cornerColors[0] = 0
          cornerColors[1] = shadowEndColor
          cornerColors[2] = shadowMiddleColor
          cornerColors[3] = shadowStartColor
        } else {
          // Calculate the arc bounds to prevent drawing shadow in the same part of the arc.
          arcBounds.rewind()
          arcBounds.moveTo(bounds.centerX(), bounds.centerY())
          arcBounds.arcTo(bounds, startAngle, sweepAngle)
          arcBounds.close()
          bounds.inset(-elevation.toFloat(), -elevation.toFloat())
          cornerColors[0] = 0
          cornerColors[1] = shadowStartColor
          cornerColors[2] = shadowMiddleColor
          cornerColors[3] = shadowEndColor
        }
        val radius = bounds.width() / 2f
        // The shadow is not big enough to draw.
        if (radius <= 0) {
          return
        }
        val startRatio = 1f - elevation / radius
        val midRatio = startRatio + (1f - startRatio) / 2f
        cornerPositions[1] = startRatio
        cornerPositions[2] = midRatio
        cornerShadowPaint.shader = RadialGradient(
          bounds.centerX(),
          bounds.centerY(),
          radius,
          cornerColors,
          cornerPositions,
          Shader.TileMode.CLAMP
        )

        // TODO(b/117606382): handle oval bounds by scaling the canvas.
        canvas.save()
        canvas.concat(matrix)
        if (!drawShadowInsideBounds) {
          @Suppress("deprecation")
          canvas.clipPath(arcBounds, Region.Op.DIFFERENCE)
          // This line is required for the next drawArc to work correctly, I think.
          canvas.drawPath(arcBounds, transparentPaint)
        }
        canvas.drawArc(bounds, startAngle, sweepAngle, true, cornerShadowPaint)
        canvas.restore()
      }

      override fun getShadowPaint(): Paint = paint!!

      companion object {
        private val edgeColors = IntArray(3)

        /** Start, middle of shadow, and end of shadow positions  */
        private val edgePositions = floatArrayOf(0f, .5f, 1f)
        private val cornerColors = IntArray(4)

        /** Start, beginning of corner, middle of shadow, and end of shadow positions  */
        private val cornerPositions = floatArrayOf(0f, 0f, .5f, 1f)
      }
    }
  }
}