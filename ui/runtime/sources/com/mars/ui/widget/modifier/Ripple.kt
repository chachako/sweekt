@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.widget.modifier

import android.view.View
import android.view.ViewGroup
import com.mars.toolkit.graphics.drawable.setRadius
import com.mars.ui.Ui
import com.mars.ui.core.Modifier
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.decoupling.ForegroundProvider
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.drawable.Drawable
import com.mars.ui.core.graphics.drawable.NativeDrawable
import com.mars.ui.core.graphics.drawable.RippleDrawable
import com.mars.ui.core.graphics.drawable.drawWith
import com.mars.ui.core.graphics.shape.RectangleShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.toColorStates
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.graphics.useOrNull
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toIntPxOrNull
import com.mars.ui.theme.currentColors


/**
 * 将水波纹 [RippleDrawable] 设置为视图的前景
 *
 * @param color 涟漪颜色
 * @param shape 涟漪的遮罩形状，默认为 [RectangleShape]
 * @param radius 默认使用 [View] 的大小
 */
fun Modifier.rippleForeground(
  color: Color = Color.Unspecified,
  radius: SizeUnit = SizeUnit.Unspecified,
  shape: Shape? = null,
) = +RippleEffectModifier(color, shape, radius, RippleEffectModifier.Type.Foreground)

/**
 * 将水波纹 [RippleDrawable] 设置为视图的背景
 *
 * @param color 涟漪颜色
 * @param shape 涟漪的遮罩形状，默认为 [RectangleShape]
 * @param radius 默认使用 [View] 的大小
 */
fun Modifier.rippleBackground(
  color: Color = Color.Unspecified,
  radius: SizeUnit = SizeUnit.Unspecified,
  shape: Shape? = null,
) = +RippleEffectModifier(color, shape, radius, RippleEffectModifier.Type.Background)


private class RippleEffectModifier(
  val color: Color,
  val shape: Shape?,
  val radius: SizeUnit,
  val type: Type,
) : Modifier, UpdatableModifier {
  override fun View.realize(parent: ViewGroup?) {
    if (type == Type.Foreground && this !is ForegroundProvider) return
    val color = color.useOrElse {
      (this as? Ui)?.currentColors?.ripple?.useOrNull()
        ?: Color.White.copy(alpha = 0.2f)
    }

    when (type) {
      Type.Background -> background = background.wrapRipple(color)
      Type.Foreground -> (this as ForegroundProvider).setSupportForeground(
        foregroundSupport.wrapRipple(color)
      )
    }
  }

  override fun View.update(parent: ViewGroup?) {
    val ui = this as? Ui ?: return
    val bg = background as? RippleDrawable
    bg?.setColor(bg.colorStates.updateColors(ui).toColorStateList())

    val fg = (this as? ForegroundProvider)?.foregroundSupport as? RippleDrawable
    fg?.setColor(fg.colorStates.updateColors(ui).toColorStateList())
  }

  private fun NativeDrawable?.wrapRipple(color: Color) = RippleDrawable(
    content = this,
    mask = drawWith(
      color = Color.White,
      shape = shape ?: if (this is Drawable) attributes.shape else null ?: RectangleShape,
    ),
    colorStates = color.toColorStates(),
  ).also {
    radius.toIntPxOrNull()?.apply { setRadius(it, this) }
  }

  enum class Type { Background, Foreground }
}