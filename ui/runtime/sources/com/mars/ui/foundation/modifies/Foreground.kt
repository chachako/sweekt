@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.foundation.modifies

import android.content.res.ColorStateList
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import com.google.android.material.shape.MaterialShapeDrawable
import com.mars.toolkit.graphics.drawable.DrawableBuilder
import com.mars.toolkit.graphics.drawable.buildDrawable
import com.mars.ui.core.Border
import com.mars.ui.core.Foreground
import com.mars.ui.core.Modifier
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.isSet
import com.mars.ui.core.graphics.shape.RectangleShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.graphics.useOrNull
import com.mars.ui.core.unit.toPx
import com.mars.ui.theme.Colors.Companion.resolveColor
import kotlin.math.roundToInt

/**
 * 用 [ColorDrawable] 或 [MaterialShapeDrawable] 调整 View 的前景
 *
 * @param color 前景色
 * @param alpha 前景透明度
 * @param border 前景的边框
 * @param colorFilter 前景的滤色镜
 * @param shape 前景形状，默认为 [RectangleShape]
 */
fun Modifier.foreground(
  color: Color = Color.Unset,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
  border: Border? = null,
  colorFilter: ColorFilter? = null,
  shape: Shape? = null,
) = +ForegroundModifier(color, alpha, border, colorFilter, shape)

/** 用任意 [Drawable] 调整 View 的前景 */
fun Modifier.foreground(drawable: Drawable) = +DrawForegroundModifier(drawable)

/** 用 resId 得到的 [Drawable] 以调整 View 的前景 */
fun Modifier.foreground(imageResource: Int) = +DrawForegroundModifier(imageResource = imageResource)

/** 调整 View 的前景 [DrawableBuilder] */
fun Modifier.foreground(block: DrawableBuilder.() -> Unit) = foreground(buildDrawable(block))

/** 根据参数调整 View 前景的具体实现 */
private data class ForegroundModifier(
  val color: Color = Color.Unset,
  val alpha: Float = 1f,
  val border: Border? = null,
  val colorFilter: ColorFilter? = null,
  val shape: Shape? = null,
) : Modifier, UpdatableModifier {
  override fun View.realize(parent: ViewGroup?) {
    if (this !is Foreground) return
    if (border != null || alpha != 1f || shape != null) {
      setSupportForeground(MaterialShapeDrawable(
        shape?.toModelBuilder()?.build()
          ?: (foregroundSupport as? MaterialShapeDrawable)?.shapeAppearanceModel
          ?: RectangleShape.toModelBuilder().build()
      ).also {
        it.alpha = (255 * alpha).roundToInt()
        colorFilter?.apply(it::setColorFilter)
        color.useOrElse { Color.Transparent }.argb.run(ColorStateList::valueOf)
          .apply(it::setFillColor)
        border?.color?.useOrNull()?.argb?.run(ColorStateList::valueOf)?.apply(it::setStrokeColor)
        border?.size?.toPx()?.apply(it::setStrokeWidth)
      })
    } else if (color.isSet) setSupportForeground(ColorDrawable(color.argb))
  }

  override fun View.update(parent: ViewGroup?) {
    if (this !is Foreground) return
    val foreground = foregroundSupport
    when (foreground) {
      is MaterialShapeDrawable -> {
        foreground.fillColor = color.useOrElse { Color.Transparent }.resolveColor().argb
          .run(ColorStateList::valueOf)
          .apply(foreground::setFillColor)

        foreground.strokeColor = border?.color?.useOrNull()?.resolveColor()?.argb
          ?.run(ColorStateList::valueOf)
          ?.apply(foreground::setStrokeColor)
      }
      is ColorDrawable -> {
        foreground.color = color.useOrElse { Color.Transparent }.resolveColor().argb
      }
    }
    foreground?.apply(::setSupportForeground)
  }
}

/** 根据 [Drawable] 调整 View 前景的具体实现 */
private data class DrawForegroundModifier(
  val drawable: Drawable? = null,
  val imageResource: Int? = null,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    if (this !is Foreground) return
    if (drawable != null) {
      if (foregroundSupport != drawable) setSupportForeground(drawable)
    }
    if (imageResource != null) {
      setSupportForegroundResource(imageResource)
    }
  }
}