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
import com.mars.toolkit.content.res.drawableResource
import com.mars.toolkit.content.res.resource
import com.mars.toolkit.graphics.drawable.DrawableBuilder
import com.mars.toolkit.graphics.drawable.buildDrawable
import com.mars.toolkit.view.backgroundResource
import com.mars.ui.Ui
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

private val BackgroundStub = Color.Transparent.apply { id = -100 }

/**
 * 将主题的背景颜色设置到视图上
 * @see Colors.background
 */
fun Modifier.background() = background(color = BackgroundStub)

/**
 * 用 [ColorDrawable] 或 [MaterialShapeDrawable] 调整 View 的背景
 *
 * @param alpha 背景透明度
 * @param border 背景的边框
 * @param colorFilter 背景的滤色镜
 * @param shape 背景形状，默认为 [RectangleShape]
 */
fun Modifier.background(
  border: Border? = null,
  colorFilter: ColorFilter? = null,
  shape: Shape? = null,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
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
  border: Border? = null,
  colorFilter: ColorFilter? = null,
  shape: Shape? = null,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
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
  border: Border? = null,
  colorFilter: ColorFilter? = null,
  shape: Shape? = null,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
) = +BackgroundModifier(
  colorStateList = ColorStateList(
    arrayOf(
      intArrayOf(android.R.attr.state_pressed),
      intArrayOf(-android.R.attr.state_enabled),
      intArrayOf(android.R.attr.state_selected),
      intArrayOf(),
    ),
    normal.useOrElse { Color.Transparent }.let {
      intArrayOf(
        pressed.useOrElse { it }.argb,
        selected.useOrElse { it }.argb,
        disabled.useOrElse { it.copy(alpha = 0.2f) }.argb,
        it.argb,
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
  override fun View.realize(parent: ViewGroup?) {
    var color = color
    if (color == BackgroundStub) color = (this as Ui).currentColors.background

    if (border != null
      || alpha != 1f
      || shape != null
      || colorFilter != null
      || colorStateList != null
    ) {
      background = MaterialShapeDrawable(
        shape?.toModelBuilder()?.build()
          ?: (background as? MaterialShapeDrawable)?.shapeAppearanceModel
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
    } else if (color.isSet) background = ColorDrawable(color.argb)
  }

  override fun View.update(parent: ViewGroup?) {
    if (this !is Ui) return

    val background = background
    when (background) {
      is MaterialShapeDrawable -> {
        background.fillColor = color.useOrElse { Color.Transparent }.resolveColor(this).argb
          .run(ColorStateList::valueOf)
          .apply(background::setFillColor)

        background.strokeColor = border?.color?.useOrNull()?.resolveColor(this)?.argb
          ?.run(ColorStateList::valueOf)
          ?.apply(background::setStrokeColor)
      }
      is ColorDrawable -> {
        background.color = color.useOrElse { Color.Transparent }.resolveColor(this).argb
      }
    }
    this.background = background
  }
}

/** 根据 [Drawable] 调整 View 背景的具体实现 */
private data class DrawBackgroundModifier(
  val drawable: Drawable? = null,
  val imageResource: Int? = null,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    when {
      drawable != null -> background = drawable
      imageResource != null -> backgroundResource = imageResource
    }
  }
}