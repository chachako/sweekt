@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate", "RestrictedApi")

package com.mars.ui.foundation

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView
import com.mars.ui.Theme
import com.mars.ui.UiKit
import com.mars.ui.UiKitMarker
import com.mars.ui.core.Foreground
import com.mars.ui.core.Modifier
import com.mars.ui.core.ModifierManager
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.graphics.*
import com.mars.ui.foundation.modifies.BlurEffect
import com.mars.ui.theme.Colors
import com.mars.ui.theme.Colors.Companion.resolveColor
import com.mars.ui.util.BlurHelper
import kotlin.math.roundToInt

/*
 * author: 凛
 * date: 2020/8/8 6:24 PM
 * github: https://github.com/oh-Rin
 * description: 文本控件的扩展
 */
@UiKitMarker open class Image @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr),
  Theme.User,
  BlurEffect,
  Foreground,
  ModifierProvider,
  UiKit {
  override var foregroundSupport: Drawable? = null

  override var blurHelper: BlurHelper? = null

  override var modifier: Modifier? = null
    set(value) {
      field = value
      modifier?.realize(this, parent as? ViewGroup)
    }

  /**
   * 备份一份填充颜色，这是因为这个颜色可能是使用了颜色库 [Colors.Companion] 中占坑的颜色
   * 主题更新时这些坑位颜色也会动态更新，所以需要一份没有被解析过的颜色值进行判断是否使用了坑位色
   * 对于坑位色需要一些额外处理 [Colors.resolveColor]
   */
  private var backupTintColor: Color? = null

  /** 填充颜色，如果使用的颜色是 [Colors] 中的主题颜色则会跟随后续主题变化而更新 */
  fun setImageTintList(tint: Color?) {
    if (backupTintColor == tint) return
    backupTintColor = tint?.useOrNull()
    if (tint == null) supportImageTintList = null else backupTintColor?.argb
      ?.let(ColorStateList::valueOf)
      ?.apply(::setSupportImageTintList)
  }

  override fun onDraw(canvas: Canvas) {
    blurHelper?.drawBlur(canvas)
    // 渲染途中不要渲染自身
    if (blurHelper == null || !blurHelper!!.isRendering) super.onDraw(canvas)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    blurHelper?.attach()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    blurHelper?.detach()
  }

  /**
   * 创建一个 Image 视图
   * @param imageResource 用 resId [Resources.getDrawable] 设置图片
   * @param imageBitmap 设置 [Bitmap] 类型的图片
   * @param imageDrawable 设置 [Drawable] 类型的图片
   * @param modifier 对于 View 的其他额外调整
   * @param scaleType 将原图显示在视图上时的缩放类型
   * @param tint 对图片进行着色，如果为 null 则取消之前的所有填色
   * @param tintMode 着色模式
   * @param colorFilter 滤色镜, [EraseColorFilter] 会取消所有滤色镜的设置
   * @param alpha 图片显示的透明度（不是控件的 [View.getAlpha] 透明度）
   */
  fun update(
    imageResource: Int? = null,
    imageBitmap: Bitmap? = null,
    imageDrawable: Drawable? = null,
    modifier: Modifier? = null,
    scaleType: com.mars.ui.core.graphics.ScaleType? = null,
    tint: Color? = this.backupTintColor,
    tintList: ColorStateList? = null,
    tintMode: BlendMode? = null,
    colorFilter: ColorFilter = this.colorFilter ?: EraseColorFilter,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float? = null,
  ) = also {
    imageResource?.apply(it::setImageResource)
    imageBitmap?.apply(it::setImageBitmap)
    imageDrawable?.apply(it::setImageDrawable)
    setImageTintList(tint)
    tintList?.apply(it::setSupportImageTintList)
    tintMode?.toPorterDuffMode().apply(it::setSupportImageTintMode)
    when (colorFilter) {
      EraseColorFilter -> if (it.colorFilter != null) it.colorFilter = null
      else -> it.colorFilter = colorFilter
    }
    if (alpha != null) (255 * alpha).roundToInt().apply {
      if (it.imageAlpha != this) it.imageAlpha = this
    }
    if (scaleType != null && it.scaleType != scaleType.real) setScaleType(scaleType.real)
    it.modifier = modifier
  }


  /**
   * 更新主题
   *
   * NOTE: 如果之前使用了主题，主题更新时我们也要更新对应的值
   * 为了避免逻辑错误，后续手动修改了某个样式时将不会被主题覆盖
   * 除非使用的是主题中的值，如 [Colors.onPrimary] 等
   */
  override fun updateUiKitTheme() {
    backupTintColor?.resolveColor()?.apply(::setImageTintList)

    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.update(this, parent as? ViewGroup)
    }
  }
}

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
fun UiKit.Image(
  asset: Drawable? = null,
  modifier: Modifier = Modifier,
  scaleType: ScaleType = ScaleType.Fit,
  tint: Color = Color.Unset,
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
fun UiKit.Image(
  asset: Bitmap,
  modifier: Modifier = Modifier,
  scaleType: ScaleType = ScaleType.Fit,
  tint: Color = Color.Unset,
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
fun UiKit.Image(
  asset: Int,
  modifier: Modifier = Modifier,
  scaleType: ScaleType = ScaleType.Fit,
  tint: Color = Color.Unset,
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