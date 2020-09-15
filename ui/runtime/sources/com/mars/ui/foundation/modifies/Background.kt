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
import com.mars.ui.core.Modifier
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.isSet
import com.mars.ui.core.graphics.shape.RectangleShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.graphics.useOrNull
import com.mars.ui.core.unit.toPx
import com.mars.ui.theme.Colors
import com.mars.ui.theme.Colors.Companion.resolveColor
import com.mars.ui.theme.currentColors
import kotlin.math.roundToInt

/**
 * 将主题的背景颜色设置到视图上
 * @see Colors.background
 */
fun Modifier.background() = background(color = currentColors.background)

/**
 * 用 [ColorDrawable] 或 [MaterialShapeDrawable] 调整 View 的背景
 *
 * @param alpha 背景透明度
 * @param border 背景的边框
 * @param colorFilter 背景的滤色镜
 * @param shape 背景形状，默认为 [RectangleShape]
 */
fun Modifier.background(
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
  border: Border? = null,
  colorFilter: ColorFilter? = null,
  shape: Shape? = null,
) = +BackgroundModifier(Color.Unset, null, alpha, border, colorFilter, shape)

/**
 * 用 [ColorDrawable] 或 [MaterialShapeDrawable] 调整 View 的背景
 *
 * @param color 背景色
 * @param colorStates 不同状态的背景色
 * @param alpha 背景透明度
 * @param border 背景的边框
 * @param colorFilter 背景的滤色镜
 * @param shape 背景形状，默认为 [RectangleShape]
 */
fun Modifier.background(
  color: Color = Color.Unset,
  colorStates: ColorStateList? = null,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
  border: Border? = null,
  colorFilter: ColorFilter? = null,
  shape: Shape? = null,
) = +BackgroundModifier(color, colorStates, alpha, border, colorFilter, shape)

/**
 * 用 [ColorDrawable] 或 [MaterialShapeDrawable] 调整 View 的背景
 *
 * @param normal 默认背景色
 * @param pressed 按下后的背景色（默认为 [normal]）
 * @param selected [View.isSelected] == true 状态下的背景色（默认为 [normal]）
 * @param disabled 禁用后的背景色（默认为 20 透明度的 [normal]）
 * @param alpha 背景透明度
 * @param border 背景的边框
 * @param colorFilter 背景的滤色镜
 * @param shape 背景形状，默认为 [RectangleShape]
 */
fun Modifier.background(
  normal: Color = Color.Unset,
  pressed: Color = Color.Unset,
  selected: Color = Color.Unset,
  disabled: Color = Color.Unset,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
  border: Border? = null,
  colorFilter: ColorFilter? = null,
  shape: Shape? = null,
) = +BackgroundModifier(
  color = Color.Unset,
  colorStateList = ColorStateList(
    arrayOf(
      intArrayOf(android.R.attr.state_pressed),
      intArrayOf(-android.R.attr.state_enabled),
      intArrayOf(-android.R.attr.state_selected),
    ),
    normal.useOrElse { Color.Transparent }.let {
      intArrayOf(
        it.argb,
        pressed.useOrElse { it }.argb,
        selected.useOrElse { it }.argb,
        disabled.useOrElse { it.copy(alpha = 0.2f) }.argb,
      )
    },
  ),
  alpha = alpha,
  border = border,
  colorFilter = colorFilter,
  shape = shape
)

/** 用任意 [Drawable] 调整 View 的背景 */
fun Modifier.background(drawable: Drawable) = +DrawBackgroundModifier(drawable)

/** 用 resId 得到的 [Drawable] 以调整 View 的背景 */
fun Modifier.background(imageResource: Int) = +DrawBackgroundModifier(imageResource = imageResource)

/** 调整 View 的背景 [DrawableBuilder] */
fun Modifier.background(block: DrawableBuilder.() -> Unit) = background(buildDrawable(block))

/** 根据参数调整 View 背景的具体实现 */
private data class BackgroundModifier(
  val color: Color = Color.Unset,
  val colorStateList: ColorStateList? = null,
  val alpha: Float = 1f,
  val border: Border? = null,
  val colorFilter: ColorFilter? = null,
  val shape: Shape? = null,
) : Modifier, UpdatableModifier {
  override fun realize(myself: View, parent: ViewGroup?) {
    if (border != null
      || alpha != 1f
      || shape != null
    ) {
      myself.background = MaterialShapeDrawable(
        shape?.toModelBuilder()?.build()
          ?: (myself.background as? MaterialShapeDrawable)?.shapeAppearanceModel
          ?: RectangleShape.toModelBuilder().build()
      ).also {
        it.alpha = (255 * alpha).roundToInt()

        if (colorStateList != null) {
          it.fillColor = colorStateList
        } else {
          color.useOrElse { Color.Transparent }.argb
            .run(ColorStateList::valueOf)
            .run(it::setFillColor)
        }

        colorFilter?.apply(it::setColorFilter)
        border?.color?.useOrNull()?.argb?.run(ColorStateList::valueOf)?.run(it::setStrokeColor)
        border?.size?.toPx()?.run(it::setStrokeWidth)
      }
    } else if (color.isSet) myself.background = ColorDrawable(color.argb)
  }

  override fun update(myself: View, parent: ViewGroup?) {
    when (val background = myself.background) {
      is MaterialShapeDrawable -> {
        background.fillColor = color.useOrElse { Color.Transparent }.resolveColor().argb
          .run(ColorStateList::valueOf)
          .apply(background::setFillColor)

        background.strokeColor = border?.color?.useOrNull()?.resolveColor()?.argb
          ?.run(ColorStateList::valueOf)
          ?.apply(background::setStrokeColor)
      }
      is ColorDrawable -> {
        background.color = color.useOrElse { Color.Transparent }.resolveColor().argb
      }
    }
  }
}

/** 根据 [Drawable] 调整 View 背景的具体实现 */
private data class DrawBackgroundModifier(
  val drawable: Drawable? = null,
  val imageResource: Int? = null,
) : Modifier {
  override fun realize(myself: View, parent: ViewGroup?) {
    if (drawable != null) if (myself.background != drawable) myself.background = drawable
    if (imageResource != null) myself.setBackgroundResource(imageResource)
  }
}