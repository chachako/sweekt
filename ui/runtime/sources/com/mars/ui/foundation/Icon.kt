@file:Suppress(
  "FunctionName", "PropertyName", "RestrictedApi",
  "SuspiciousVarProperty", "MemberVisibilityCanBePrivate",
  "NAME_SHADOWING", "DEPRECATION"
)

package com.mars.ui.foundation

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.mars.toolkit.graphics.drawable.LayerDrawableBuilder
import com.mars.ui.Ui
import com.mars.ui.UiKitMarker
import com.mars.ui.asLayout
import com.mars.ui.core.Modifier
import com.mars.ui.core.Padding
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.useOrNull
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.isSpecified
import com.mars.ui.core.unit.toIntPxOrNull
import com.mars.ui.foundation.styles.IconStyle
import com.mars.ui.theme.Icons.Companion.resolveIcon
import com.mars.ui.theme.currentIcons


/*
 * author: 凛
 * date: 2020/8/9 11:54 PM
 * github: https://github.com/oh-Rin
 * description: 一个简单的图标
 */
@UiKitMarker
open class Icon @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
  private var realBackground: Drawable? = null

  internal var style: IconStyle? = null
    set(value) {
      field = value
      value?.apply(this)
    }

  internal var iconColor: Color = Color.Unset
  internal var iconPadding: Padding = Padding.Unspecified
  internal var iconWidth: SizeUnit = SizeUnit.Unspecified
  internal var iconHeight: SizeUnit = SizeUnit.Unspecified

  var icon: Drawable? = null
    set(value) {
      field = value
      super.setBackgroundDrawable(value?.let {
        iconColor.useOrNull()?.argb?.also(it::setTint)
        LayerDrawableBuilder().apply {
          realBackground?.also(::add)
          add(
            drawable = it,
            gravity = if (iconWidth.isSpecified || iconHeight.isSpecified) Gravity.CENTER else Gravity.NO_GRAVITY,
            width = iconWidth.toIntPxOrNull() ?: LayerDrawableBuilder.DIMEN_UNDEFINED,
            height = iconHeight.toIntPxOrNull() ?: LayerDrawableBuilder.DIMEN_UNDEFINED,
            insetStart = iconPadding.start.toIntPxOrNull() ?: LayerDrawableBuilder.DIMEN_UNDEFINED,
            insetTop = iconPadding.top.toIntPxOrNull() ?: LayerDrawableBuilder.DIMEN_UNDEFINED,
            insetEnd = iconPadding.end.toIntPxOrNull() ?: LayerDrawableBuilder.DIMEN_UNDEFINED,
            insetBottom = iconPadding.bottom.toIntPxOrNull()
              ?: LayerDrawableBuilder.DIMEN_UNDEFINED,
          )
        }.build()
      } ?: realBackground)
    }

  /** 更新 [Text] 的一些参数 */
  fun update(
    asset: Drawable? = null,
    modifier: Modifier = this.modifier,
    color: Color = Color.Unset,
    padding: Padding = Padding.Unspecified,
    size: SizeUnit = SizeUnit.Unspecified,
    height: SizeUnit = size,
    width: SizeUnit = size,
    style: IconStyle = this.style!!,
  ) = also {
    it.style = style.merge(
      IconStyle(
        color = color,
        padding = padding,
        size = size,
        width = width,
        height = height
      )
    )
    it.modifier = modifier
    asset?.apply { it.icon = this }
  }

  override fun setBackgroundDrawable(background: Drawable?) {
    realBackground = background
    super.setBackgroundDrawable(background)
  }

  override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {}

  override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {}

  override fun updateUiKitTheme() {
    style?.resolveIcon(this)?.also { style = it }
    super.updateUiKitTheme()
  }
}

/**
 * 创建一个 Icon 视图
 * @param asset 设置 [Drawable] 类型的图标
 * @param modifier 对于 View 的其他额外调整
 * @param color [IconStyle.color]
 * @param padding 图标内边距
 * @param size [IconStyle.size]
 * @param style 图标样式 [IconStyle]
 * @see Image
 */
fun Ui.Icon(
  asset: Drawable,
  modifier: Modifier = Modifier,
  color: Color = Color.Unset,
  padding: Padding = Padding.Unspecified,
  size: SizeUnit = SizeUnit.Unspecified,
  style: IconStyle = currentIcons.medium,
): Icon = With(::Icon) {
  it.update(
    asset = asset,
    modifier = modifier,
    padding = padding,
    color = color,
    size = size,
    style = style
  )
}

/**
 * 创建一个 Icon 视图
 * @param asset 设置 [Bitmap] 类型的图标
 * @param modifier 对于 View 的其他额外调整
 * @param color [IconStyle.color]
 * @param padding 图标内边距
 * @param size [IconStyle.size]
 * @param style 图标样式 [IconStyle]
 * @see Image
 */
fun Ui.Icon(
  asset: Bitmap,
  modifier: Modifier = Modifier,
  color: Color = Color.Unset,
  padding: Padding = Padding.Unspecified,
  size: SizeUnit = SizeUnit.Unspecified,
  style: IconStyle = currentIcons.medium,
) = Icon(
  asset = BitmapDrawable(this.asLayout.resources, asset),
  modifier = modifier,
  color = color,
  padding = padding,
  size = size,
  style = style
)

/**
 * 创建一个 Icon 视图
 * @param asset 用 resId [Resources.getDrawable] 设置图标
 * @param modifier 对于 View 的其他额外调整
 * @param color [IconStyle.color]
 * @param padding 图标内边距
 * @param size [IconStyle.size]
 * @param style 图标样式 [IconStyle]
 * @see Image
 */
fun Ui.Icon(
  asset: Int,
  modifier: Modifier = Modifier,
  color: Color = Color.Unset,
  padding: Padding = Padding.Unspecified,
  size: SizeUnit = SizeUnit.Unspecified,
  style: IconStyle = currentIcons.medium,
) = Icon(
  asset = ContextCompat.getDrawable(this.asLayout.context, asset)!!,
  modifier = modifier,
  color = color,
  padding = padding,
  size = size,
  style = style
)