//@file:Suppress("OverridingDeprecatedMember")
//
//package com.meowbase.ui.widget.modifier
//
//import android.content.res.ColorStateList
//import android.graphics.ColorFilter
//import android.graphics.drawable.ColorDrawable
//import android.graphics.drawable.Drawable
//import android.view.View
//import android.view.ViewGroup
//import androidx.annotation.FloatRange
//import com.google.android.material.shape.MaterialShapeDrawable
//import com.meowbase.toolkit.graphics.drawable.DrawableBuilder
//import com.meowbase.toolkit.graphics.drawable.buildDrawable
//import com.meowbase.ui.Ui
//import com.meowbase.ui.core.Border
//import com.meowbase.ui.core.Modifier
//import com.meowbase.ui.core.UpdatableModifier
//import com.meowbase.ui.core.graphics.Color
//import com.meowbase.ui.core.graphics.isSet
//import com.meowbase.ui.core.graphics.shape.RectangleShape
//import com.meowbase.ui.core.graphics.shape.Shape
//import com.meowbase.ui.core.graphics.useOrElse
//import com.meowbase.ui.core.graphics.useOrNull
//import com.meowbase.ui.core.decoupling.ForegroundProvider
//import com.meowbase.ui.theme.Colors.Companion.resolveColor
//
///**
// * 用 [ColorDrawable] 或 [MaterialShapeDrawable] 调整 View 的前景
// *
// * @param color 前景色
// * @param alpha 前景透明度
// * @param border 前景的边框
// * @param colorFilter 前景的滤色镜
// * @param shape 前景形状，默认为 [RectangleShape]
// */
//fun Modifier.foreground(
//  color: Color = Color.Unset,
//  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
//  border: Border? = null,
//  colorFilter: ColorFilter? = null,
//  shape: Shape? = null,
//) = +ForegroundModifier(color, null, alpha, border, colorFilter, shape)
//
///**
// * 用 [ColorDrawable] 或 [MaterialShapeDrawable] 调整 View 的前景
// *
// * @param normal 默认前景色
// * @param pressed 按下后的前景色（默认为 [normal]）
// * @param selected [View.isSelected] == true 状态下的前景色（默认为 [normal]）
// * @param disabled 禁用后的前景色（默认为 20 透明度的 [normal]）
// * @param alpha 前景透明度
// * @param border 前景的边框
// * @param colorFilter 前景的滤色镜
// * @param shape 前景形状，默认为 [RectangleShape]
// */
//fun Modifier.foreground(
//  normal: Color = Color.Unset,
//  pressed: Color = Color.Unset,
//  selected: Color = Color.Unset,
//  disabled: Color = Color.Unset,
//  border: Border? = null,
//  colorFilter: ColorFilter? = null,
//  shape: Shape? = null,
//  @FloatRange(from = .0, to = 1.0) alpha: Float = 1f,
//) = +ForegroundModifier(
//  colorStateList = ColorStateList(
//    arrayOf(
//      intArrayOf(android.R.attr.state_pressed),
//      intArrayOf(-android.R.attr.state_enabled),
//      intArrayOf(android.R.attr.state_selected),
//      intArrayOf(),
//    ),
//    normal.useOrElse { Color.Transparent }.let {
//      intArrayOf(
//        pressed.useOrElse { it }.argb,
//        selected.useOrElse { it }.argb,
//        disabled.useOrElse { it.copy(alpha = 0.2f) }.argb,
//        it.argb,
//      )
//    },
//  ),
//  alpha = alpha,
//  border = border,
//  colorFilter = colorFilter,
//  shape = shape
//)
//
///** 用任意 [Drawable] 调整 View 的前景 */
//fun Modifier.foreground(drawable: Drawable) = +DrawForegroundModifier(drawable)
//
///** 用 resId 得到的 [Drawable] 以调整 View 的前景 */
//fun Modifier.foreground(imageResource: Int) = +DrawForegroundModifier(imageResource = imageResource)
//
///** 调整 View 的前景 [DrawableBuilder] */
//fun Modifier.foreground(block: DrawableBuilder.() -> Unit) = foreground(buildDrawable(block))
//
///** 根据参数调整 View 前景的具体实现 */
//private data class ForegroundModifier(
//  val color: Color = Color.Unset,
//  val colorStateList: ColorStateList? = null,
//  val alpha: Float = 1f,
//  val border: Border? = null,
//  val colorFilter: ColorFilter? = null,
//  val shape: Shape? = null,
//) : Modifier, UpdatableModifier {
//  override fun View.realize(parent: ViewGroup?) {
//    if (this !is ForegroundProvider) return
//    if (border != null
//      || alpha != 1f
//      || shape != null
//      || colorFilter != null
//      || colorStateList != null
//    ) {
////      setSupportForeground(MaterialShapeDrawable(
////        shape?.toModelBuilder()?.build()
////          ?: (foregroundSupport as? MaterialShapeDrawable)?.shapeAppearanceModel
////          ?: RectangleShape.toModelBuilder().build()
////      ).also {
////        it.alpha = (255 * alpha).roundToInt()
////        colorFilter?.apply(it::setColorFilter)
////        color.useOrElse { Color.Transparent }.argb.run(ColorStateList::valueOf)
////          .apply(it::setFillColor)
////        border?.color?.useOrNull()?.argb?.run(ColorStateList::valueOf)?.apply(it::setStrokeColor)
////        border?.size?.toPx()?.apply(it::setStrokeWidth)
////      })
//    } else if (color.isSet) setSupportForeground(ColorDrawable(color.argb))
//  }
//
//  override fun View.update(parent: ViewGroup?) {
//    if (this !is Ui) return
//    if (this !is ForegroundProvider) return
//    val foreground = foregroundSupport
//    when (foreground) {
//      is MaterialShapeDrawable -> {
//        foreground.fillColor = color.useOrElse { Color.Transparent }.resolveColor(this).argb
//          .run(ColorStateList::valueOf)
//          .apply(foreground::setFillColor)
//
//        foreground.strokeColor = border?.color?.useOrNull()?.resolveColor(this)?.argb
//          ?.run(ColorStateList::valueOf)
//          ?.apply(foreground::setStrokeColor)
//      }
//      is ColorDrawable -> {
//        foreground.color = color.useOrElse { Color.Transparent }.resolveColor(this).argb
//      }
//    }
//    foreground?.apply(::setSupportForeground)
//  }
//}
//
///** 根据 [Drawable] 调整 View 前景的具体实现 */
//private data class DrawForegroundModifier(
//  val drawable: Drawable? = null,
//  val imageResource: Int? = null,
//) : Modifier {
//  override fun View.realize(parent: ViewGroup?) {
//    if (this !is ForegroundProvider) return
//    if (drawable != null) {
//      if (foregroundSupport != drawable) setSupportForeground(drawable)
//    }
//    if (imageResource != null) {
//      setSupportForegroundResource(imageResource)
//    }
//  }
//}