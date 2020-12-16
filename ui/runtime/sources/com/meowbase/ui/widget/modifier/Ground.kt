@file:Suppress("OverridingDeprecatedMember")

package com.meowbase.ui.widget.modifier

import android.graphics.ColorFilter
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import com.meowbase.toolkit.content.res.drawableResource
import com.meowbase.toolkit.data.ColorState
import com.meowbase.toolkit.data.reversed
import com.meowbase.toolkit.graphics.drawable.DrawableBuilder
import com.meowbase.toolkit.graphics.drawable.buildDrawable
import com.meowbase.toolkit.int
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Border
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.UpdatableModifier
import com.meowbase.ui.core.decoupling.ForegroundProvider
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.ColorStates
import com.meowbase.ui.core.graphics.buildColorStates
import com.meowbase.ui.core.graphics.drawable.Drawable
import com.meowbase.ui.core.graphics.drawable.NativeDrawable
import com.meowbase.ui.core.graphics.drawable.drawWith
import com.meowbase.ui.core.graphics.shape.RectangleShape
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.theme.Colors
import com.meowbase.ui.theme.Colors.Companion.resolveColor
import com.meowbase.ui.theme.currentColors

private val ColorStub = Color.Transparent.apply { id = -100 }

/**
 * 将主题的背景颜色设置到视图上
 * @see Colors.background
 */
fun Modifier.background() = background(ColorStub)

/**
 * 将主题的前景颜色设置到视图上
 * @see Colors.background
 */
fun Modifier.foreground() = foreground(ColorStub)

/**
 * 为视图绘制背景
 *
 * @param color 背景色
 * @param colorStates 不同状态的背景色
 * @param alpha 背景透明度
 * @param border 背景的边框
 * @param colorFilter 背景的滤色镜
 * @param shape 背景形状，默认为 [RectangleShape]
 */
fun Modifier.background(
  color: Color = Color.Unspecified,
  colorStates: ColorStates? = null,
  border: Border? = null,
  shape: Shape? = null,
  colorFilter: ColorFilter? = null,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
) = +GroundModifier(
  GroundModifier.Type.Background,
  _color = color,
  _alpha = alpha,
  _border = border,
  _shape = shape,
  _colorStates = colorStates,
  _colorFilter = colorFilter,
)

/**
 * 为视图绘制前景
 *
 * @param color 前景色
 * @param colorStates 不同状态的前景色
 * @param alpha 前景透明度
 * @param border 前景的边框
 * @param colorFilter 前景的滤色镜
 * @param shape 前景形状，默认为 [RectangleShape]
 */
fun Modifier.foreground(
  color: Color = Color.Unspecified,
  colorStates: ColorStates? = null,
  border: Border? = null,
  shape: Shape? = null,
  colorFilter: ColorFilter? = null,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
) = +GroundModifier(
  GroundModifier.Type.Background,
  _color = color,
  _alpha = alpha,
  _border = border,
  _shape = shape,
  _colorStates = colorStates,
  _colorFilter = colorFilter,
)

/**
 * 为视图绘制背景
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
  normal: Color = Color.Unspecified,
  pressed: Color = Color.Unspecified,
  selected: Color = Color.Unspecified,
  disabled: Color = Color.Unspecified,
  border: Border? = null,
  shape: Shape? = null,
  colorFilter: ColorFilter? = null,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
) = +GroundModifier(
  GroundModifier.Type.Background,
  _alpha = alpha,
  _border = border,
  _shape = shape,
  _colorFilter = colorFilter,
  _colorStates = buildColorStates {
    val default = normal.useOrElse { Color.Transparent }
    ColorState.Pressed to pressed.useOrElse { default }
    ColorState.Selected to selected.useOrElse { default }
    ColorState.Enabled.reversed() to disabled.useOrElse { default.copy(alpha = 0.2f) }
    ColorState.Normal to default
  },
)

/**
 * 为视图绘制前景
 *
 * @param normal 默认前景色
 * @param pressed 按下后的前景色（默认为 [normal]）
 * @param selected [View.isSelected] == true 状态下的前景色（默认为 [normal]）
 * @param disabled 禁用后的前景色（默认为 20 透明度的 [normal]）
 * @param alpha 前景透明度
 * @param border 前景的边框
 * @param colorFilter 前景的滤色镜
 * @param shape 前景形状，默认为 [RectangleShape]
 */
fun Modifier.foreground(
  normal: Color = Color.Unspecified,
  pressed: Color = Color.Unspecified,
  selected: Color = Color.Unspecified,
  disabled: Color = Color.Unspecified,
  border: Border? = null,
  shape: Shape? = null,
  colorFilter: ColorFilter? = null,
  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
) = +GroundModifier(
  GroundModifier.Type.Foreground,
  _alpha = alpha,
  _border = border,
  _shape = shape,
  _colorFilter = colorFilter,
  _colorStates = buildColorStates {
    val default = normal.useOrElse { Color.Transparent }
    ColorState.Pressed to pressed.useOrElse { default }
    ColorState.Selected to selected.useOrElse { default }
    ColorState.Enabled.reversed() to disabled.useOrElse { default.copy(alpha = 0.2f) }
    ColorState.Normal to default
  },
)

/** 用任意 [NativeDrawable] 调整 View 的背景 */
fun Modifier.background(drawable: NativeDrawable) =
  +GroundModifier(GroundModifier.Type.Background, drawable)

/** 用任意 [NativeDrawable] 调整 View 的前景 */
fun Modifier.foreground(drawable: NativeDrawable) =
  +GroundModifier(GroundModifier.Type.Foreground, drawable)

/** 用 resId 得到的 [NativeDrawable] 以调整 View 的背景 */
fun Modifier.background(imageResource: Int) =
  +GroundModifier(GroundModifier.Type.Background, _imageResource = imageResource)

/** 用 resId 得到的 [NativeDrawable] 以调整 View 的前景 */
fun Modifier.foreground(imageResource: Int) =
  +GroundModifier(GroundModifier.Type.Foreground, _imageResource = imageResource)

/** 调整 View 的背景 [DrawableBuilder] */
inline fun Modifier.background(block: DrawableBuilder.() -> Unit) = background(buildDrawable(block))

/** 调整 View 的前景 [DrawableBuilder] */
inline fun Modifier.foreground(block: DrawableBuilder.() -> Unit) = foreground(buildDrawable(block))

/** 调整 View 背景/前景 */
private class GroundModifier(
  val _type: Type,
  val _drawable: NativeDrawable? = null,
  val _imageResource: Int? = null,
  val _color: Color = Color.Unspecified,
  val _colorStates: ColorStates? = null,
  val _alpha: Float? = null,
  val _border: Border? = null,
  val _shape: Shape? = null,
  val _colorFilter: ColorFilter? = null,
) : Modifier, UpdatableModifier {
  override fun View.realize(parent: ViewGroup?) {
    var color = _color
    if (color == ColorStub) {
      color = (this as Ui).currentColors.background
    }
    val drawable = when {
      _drawable != null -> _drawable
      _imageResource != null -> drawableResource(_imageResource)
      else -> drawWith(
        color = color,
        shape = _shape,
        border = _border,
        alpha = _alpha?.let { (it * 255).int },
        colorFilter = _colorFilter,
        colorStates = _colorStates
      )
    }
    when (_type) {
      Type.Background -> background = drawable
      Type.Foreground -> (this as? ForegroundProvider)?.setSupportForeground(drawable)
    }
  }

  /**
   * 只有 [Drawable] 拥有主题更新能力
   */
  override fun View.update(parent: ViewGroup?) {
    val ui = this as? Ui ?: return
    val bg = background as? Drawable
    if (bg != null) {
      bg.update(ui)
      background = bg
    }

    val fg = (this as? ForegroundProvider)?.foregroundSupport as? Drawable
    if (fg != null) {
      fg.update(ui)
      (this as ForegroundProvider).setSupportForeground(fg)
    }
  }

  private fun Drawable.update(ui: Ui) = attributes.apply {
    color = color.resolveColor(ui)
    colorStates = colorStates?.updateColors(ui)
    border = border?.run {
      copy(
        color = color.resolveColor(ui),
        colorStates = colorStates?.updateColors(ui),
      )
    }
  }

  enum class Type { Background, Foreground }
}