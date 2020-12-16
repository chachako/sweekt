@file:Suppress(
  "FunctionName", "PropertyName", "RestrictedApi",
  "SuspiciousVarProperty", "MemberVisibilityCanBePrivate",
  "NAME_SHADOWING", "DEPRECATION"
)

package com.meowbase.ui.widget.implement

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import com.meowbase.toolkit.graphics.drawable.LayerDrawableBuilder
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.useOrNull
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.isSpecified
import com.meowbase.ui.core.unit.toIntPxOrNull
import com.meowbase.ui.widget.style.IconStyle
import com.meowbase.ui.theme.Icons.Companion.resolveIcon

/*
 * author: 凛
 * date: 2020/8/9 11:54 PM
 * github: https://github.com/RinOrz
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

  internal var iconColor: Color = Color.Unspecified
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
    color: Color = Color.Unspecified,
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