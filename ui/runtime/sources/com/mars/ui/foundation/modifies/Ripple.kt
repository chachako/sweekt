@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.foundation.modifies

import android.content.res.ColorStateList
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.MaterialShapeDrawable
import com.mars.toolkit.graphics.drawable.setRadius
import com.mars.toolkit.view.arrange
import com.mars.ui.core.Foreground
import com.mars.ui.core.Modifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.RectangleShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toIntPxOrNull
import com.mars.ui.theme.currentColors
import kotlin.math.max


/**
 * 将水波纹 [RippleDrawable] 设置为视图的前景
 *
 * @param color 涟漪颜色
 * @param shape 涟漪的遮罩形状，默认为 [RectangleShape]
 * @param radius 默认使用 [View] 的大小
 */
fun Modifier.rippleForeground(
  color: Color = currentColors.ripple,
  shape: Shape = RectangleShape,
  radius: SizeUnit = SizeUnit.Unspecified,
) = +RippleEffectModifier(color, shape, radius, AttachMode.Foreground)

/**
 * 将水波纹 [RippleDrawable] 设置为视图的背景
 *
 * @param color 涟漪颜色
 * @param shape 涟漪的遮罩形状，默认为 [RectangleShape]
 * @param radius 默认使用 [View] 的大小
 */
fun Modifier.rippleBackground(
  color: Color = currentColors.ripple,
  shape: Shape = RectangleShape,
  radius: SizeUnit = SizeUnit.Unspecified,
) = +RippleEffectModifier(color, shape, radius, AttachMode.Background)


private class RippleEffectModifier(
  val color: Color,
  val shape: Shape,
  val radius: SizeUnit,
  val mode: AttachMode,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    if (mode == AttachMode.Foreground && this !is Foreground) return
    val radius = radius.toIntPxOrNull()
    if (radius == null) {
      arrange { apply(max(width, height) / 2) }
    } else {
      apply(radius)
    }
  }

  private fun View.apply(radius: Int) {
    val rippleDrawable = MaterialShapeDrawable(shape.toModelBuilder().build()).run {
      setTint(Color.White.argb)
      RippleDrawable(
        ColorStateList.valueOf(color.useOrElse {
          currentColors.ripple.useOrElse {
            Color.White.copy(alpha = 0.2f)
          }
        }.argb),
        null, this
      ).also { setRadius(it, radius) }
    }
    when (mode) {
      AttachMode.Background -> background = if (background != null) {
        LayerDrawable(
          arrayOf(
            background,
            rippleDrawable,
          )
        )
      } else {
        rippleDrawable
      }
      AttachMode.Foreground -> (this as Foreground).setSupportForeground(
        if (foregroundSupport != null) {
          LayerDrawable(
            arrayOf(
              foregroundSupport,
              rippleDrawable,
            )
          )
        } else {
          rippleDrawable
        }
      )
    }
  }
}

enum class AttachMode { Background, Foreground }