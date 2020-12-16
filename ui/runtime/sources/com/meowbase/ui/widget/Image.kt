@file:Suppress("FunctionName")

package com.meowbase.ui.widget

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.FloatRange
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.graphics.BlendMode
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.EraseColorFilter
import com.meowbase.ui.core.graphics.ScaleType
import com.meowbase.ui.widget.implement.*

/**
 * 创建一个 Image 视图
 * @param asset 设置 [Drawable] 类型的图片
 * @param modifier 对于 View 的其他额外调整
 * @param scaleType 将原图显示在视图上时的缩放类型
 * @param tint 对图片进行着色
 * @param tintMode 着色模式
 * @param colorFilter 滤色镜, [EraseColorFilter] 会取消所有滤色镜的设置
 * @param alpha 图片显示的透明度（不是控件的 [View.getAlpha] 透明度）
 */
fun Ui.Image(
  asset: Drawable? = null,
  modifier: Modifier = Modifier,
  scaleType: ScaleType = ScaleType.Fit,
  tint: Color = Color.Unspecified,
  tintList: ColorStateList? = null,
  tintMode: BlendMode = BlendMode.Unset,
  colorFilter: ColorFilter = EraseColorFilter,
  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
): Image = With(::Image) {
  it.update(
    imageResource = null,
    imageBitmap = null,
    imageDrawable = asset,
    modifier = modifier,
    scaleType = scaleType,
    tint = tint,
    tintList = tintList,
    tintMode = tintMode,
    colorFilter = colorFilter,
    alpha = alpha
  )
}

/**
 * 创建一个 Image 视图
 * @param asset 设置 [Bitmap] 类型的图片
 * @param modifier 对于 View 的其他额外调整
 * @param scaleType 将原图显示在视图上时的缩放类型
 * @param tint 对图片进行着色
 * @param tintMode 着色模式
 * @param colorFilter 滤色镜, [EraseColorFilter] 会取消所有滤色镜的设置
 * @param alpha 图片显示的透明度（不是控件的 [View.getAlpha] 透明度）
 */
fun Ui.Image(
  asset: Bitmap,
  modifier: Modifier = Modifier,
  scaleType: ScaleType = ScaleType.Fit,
  tint: Color = Color.Unspecified,
  tintList: ColorStateList? = null,
  tintMode: BlendMode = BlendMode.Unset,
  colorFilter: ColorFilter = EraseColorFilter,
  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
): Image = With(::Image) {
  it.update(
    imageResource = null,
    imageBitmap = asset,
    imageDrawable = null,
    modifier = modifier,
    scaleType = scaleType,
    tint = tint,
    tintList = tintList,
    tintMode = tintMode,
    colorFilter = colorFilter,
    alpha = alpha
  )
}

/**
 * 创建一个 Image 视图
 * @param asset 用 resId [Resources.getDrawable] 设置图片
 * @param modifier 对于 View 的其他额外调整
 * @param scaleType 将原图显示在视图上时的缩放类型
 * @param tint 对图片进行着色
 * @param tintMode 着色模式
 * @param colorFilter 滤色镜, [EraseColorFilter] 会取消所有滤色镜的设置
 * @param alpha 图片显示的透明度（不是控件的 [View.getAlpha] 透明度）
 */
fun Ui.Image(
  asset: Int,
  modifier: Modifier = Modifier,
  scaleType: ScaleType = ScaleType.Fit,
  tint: Color = Color.Unspecified,
  tintList: ColorStateList? = null,
  tintMode: BlendMode = BlendMode.Unset,
  colorFilter: ColorFilter = EraseColorFilter,
  @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
): Image = With(::Image) {
  it.update(
    imageResource = asset,
    imageBitmap = null,
    imageDrawable = null,
    modifier = modifier,
    scaleType = scaleType,
    tint = tint,
    tintList = tintList,
    tintMode = tintMode,
    colorFilter = colorFilter,
    alpha = alpha
  )
}