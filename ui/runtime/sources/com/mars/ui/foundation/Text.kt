@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate", "PropertyName",
  "OverridingDeprecatedMember"
)

package com.mars.ui.foundation

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.core.widget.TextViewCompat
import com.google.android.material.textview.MaterialTextView
import com.mars.toolkit.removeFlags
import com.mars.ui.Theme
import com.mars.ui.UiKit
import com.mars.ui.UiKitMarker
import com.mars.ui.core.*
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.IconData
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.graphics.useOrNull
import com.mars.ui.core.text.*
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.TextUnit
import com.mars.ui.core.unit.toIntPxOrNull
import com.mars.ui.core.unit.useOrElse
import com.mars.ui.foundation.modifies.BlurEffect
import com.mars.ui.foundation.styles.IconStyle
import com.mars.ui.foundation.styles.TextStyle
import com.mars.ui.theme.*
import com.mars.ui.theme.Colors.Companion.resolveColor
import com.mars.ui.theme.Styles.Companion.resolveStyle
import com.mars.ui.theme.Typography.Companion.resolveTypography
import com.mars.ui.util.BlurHelper

/*
 * author: 凛
 * date: 2020/8/8 6:24 PM
 * github: https://github.com/oh-Rin
 * description: 文本控件的扩展
 */
@UiKitMarker open class Text @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : MaterialTextView(context, attrs, defStyleAttr, defStyleRes),
  Theme.User,
  BlurEffect,
  Foreground,
  ModifierProvider,
  UiKit {
  override var foregroundSupport: Drawable? = null

  override var blurHelper: BlurHelper? = null

  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(parent as? ViewGroup) }
    }

  /** 记录最后设置的值，以便主题系统来判断是否要更新对应的值 */
  private var textColor: Color? = null
  internal var style: TextStyle? = null
    set(value) {
      field = value
      value?.apply(this)
    }
  internal var iconStyle: IconStyle? = null

  /** 跑马灯效果 */
  var marquee: TextMarquee? = null
    set(value) {
      field = value!!
      isSelected = true
      isSingleLine = value.enabled
      ellipsize = if (value.enabled) TextUtils.TruncateAt.MARQUEE else TextUtils.TruncateAt.END
      marqueeRepeatLimit = value.repeat
    }

  var maxLength: Int? = null
    set(value) {
      field = value!!
      filters += arrayOf(LengthFilter(value))
    }

  var overflow: TextOverflow? = null
    set(value) {
      field = value!!
      ellipsize = when (value) {
        TextOverflow.Start -> TextUtils.TruncateAt.START
        TextOverflow.Middle -> TextUtils.TruncateAt.MIDDLE
        TextOverflow.End -> TextUtils.TruncateAt.END
      }
    }

  var isAutoSize: Boolean = false
    @SuppressLint("RestrictedApi", "WrongConstant")
    set(value) {
      field = value
      setAutoSizeTextTypeWithDefaults(
        if (value) TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        else TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE
      )
    }

  /** 调整文本的着色器 */
  fun Modifier.shader(shader: Shader) = +ShaderModifier(shader)

  /** 调整文本的着色器 */
  fun Modifier.shader(shaderProvider: (View) -> Shader) =
    +ShaderModifier(shaderProvider = shaderProvider)

  /** 装饰文本 */
  fun decorate(textDecoration: TextDecoration) {
    // 抗锯齿
    if (textDecoration != TextDecoration.None && !paint.isAntiAlias) paint.isAntiAlias = true
    paintFlags = when (textDecoration) {
      TextDecoration.Underline -> paintFlags or Paint.UNDERLINE_TEXT_FLAG
      TextDecoration.LineThrough -> paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
      TextDecoration.FakeBold -> paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
      TextDecoration.None -> paintFlags
        .removeFlags(Paint.FAKE_BOLD_TEXT_FLAG)
        .removeFlags(Paint.STRIKE_THRU_TEXT_FLAG)
        .removeFlags(Paint.UNDERLINE_TEXT_FLAG)
    }
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

  /** 一旦手动设置了颜色则不需要记录节点了 */
  override fun setTextColor(color: Int) {
    textColor = null
    super.setTextColor(color)
  }

  override fun setTextColor(colors: ColorStateList?) {
    textColor = null
    super.setTextColor(colors)
  }

  /** 文本颜色，如果使用的颜色是 [Colors] 中的主题颜色则会跟随后续主题变化而更新 */
  fun setTextColor(color: Color) {
    textColor = color.useOrElse {
      /**
       * 如果没有设置颜色则使用默认色 [Colors.onSurface]
       * 如果默认色被设置成了 [Color.Unset] 则使用黑色
       */
      currentColors.onSurface.useOrElse { Color.Black }
    }
    super.setTextColor(textColor!!.argb)
  }

  /** 仅仅是重命名了原方法 */
  fun setFont(font: Font? = null, style: FontStyle) {
    super.setTypeface(font, style.real)
  }

  /** 设置文本边缘的图标 */
  fun setEdgeIcons(
    start: IconData? = null,
    end: IconData? = null,
    top: IconData? = null,
    bottom: IconData? = null,
  ) {
    fun IconData.resolveDrawable() = run {
      color.useOrElse { iconStyle?.color ?: Color.Unset }.useOrNull()
        ?.resolveColor()?.argb
        ?.apply(icon::setTint)

      icon.setBounds(
        0, 0,
        width.useOrElse { iconStyle?.width ?: SizeUnit.Unspecified }.toIntPxOrNull()
          ?: icon.intrinsicWidth,
        height.useOrElse { iconStyle?.height ?: SizeUnit.Unspecified }.toIntPxOrNull()
          ?: icon.intrinsicHeight
      )

      val padding = padding.useOrElse { iconStyle?.padding ?: Padding.Unspecified }
      if (padding.isSpecified) {
        InsetDrawable(
          icon,
          padding.start.toIntPxOrNull() ?: 0,
          padding.top.toIntPxOrNull() ?: 0,
          padding.end.toIntPxOrNull() ?: 0,
          padding.bottom.toIntPxOrNull() ?: 0,
        )
      } else icon
    }

    setCompoundDrawablesRelative(
      start?.resolveDrawable(),
      top?.resolveDrawable(),
      end?.resolveDrawable(),
      bottom?.resolveDrawable(),
    )
  }


  /** 更新 [Text] 的一些参数 */
  fun update(
    text: String? = null,
    modifier: Modifier = this.modifier,
    /** 自动调整文本大小以适应容器，默认关闭 */
    isAutoSize: Boolean? = null,
    /** 文本对齐 */
    align: Alignment? = null,
    /** 文本溢出时的处理 */
    overflow: TextOverflow? = null,
    /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
    marquee: TextMarquee? = null,
    /** 限制文本行数 */
    maxLines: Int? = null,
    /** 限制文本最长数量 */
    maxLength: Int? = null,
    /** 长按选择文本 */
    isSelectable: Boolean? = null,
    /** 文本旁边的图标，可设置上下左右四个方向 */
    icons: EdgeIcons? = null,
    /** 文本旁边所有的图标的样式 */
    iconsStyle: IconStyle? = null,
    /** 文本与图标 [icons] 之间的间隔 */
    spaceBetween: SizeUnit = SizeUnit.Unspecified,
    /** [TextStyle.color] */
    color: Color = Color.Unset,
    /** [TextStyle.lineHeight] */
    lineHeight: SizeUnit = SizeUnit.Unspecified,
    /** [TextStyle.decoration] */
    decoration: TextDecoration? = null,
    /** [TextStyle.font] */
    font: Font? = null,
    /** [TextStyle.fontStyle] */
    fontStyle: FontStyle = FontStyle.Normal,
    /** [TextStyle.fontSize] */
    fontSize: SizeUnit = TextUnit.Inherit,
    /** [TextStyle.fontName] */
    fontName: String? = PingFangFont.Regular,
    /** [TextStyle.letterSpacing] */
    letterSpacing: SizeUnit = SizeUnit.Unspecified,
    /** 文本样式（不是字体样式 [FontStyle]） */
    style: TextStyle = this.style ?: currentTypography.body1,
  ) = also {
    text?.apply(it::setText)
    it.style = style.merge(
      TextStyle(
        color = color,
        font = font,
        fontStyle = fontStyle,
        fontSize = fontSize,
        fontName = fontName,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        decoration = decoration,
      )
    )
    align?.gravity?.apply(it::setGravity)
    maxLines?.apply(it::setMaxLines)
    isSelectable?.apply(it::setTextIsSelectable)
    maxLength?.apply { it.maxLength = this }
    marquee?.apply { it.marquee = this }
    overflow?.apply { it.overflow = this }
    isAutoSize?.apply { it.isAutoSize = this }
    iconsStyle?.apply { it.iconStyle = this }
    icons?.apply { setEdgeIcons(start = start, end = end, top = top, bottom = bottom) }
    spaceBetween.toIntPxOrNull()?.apply {
      if (it.compoundDrawablePadding != this) it.compoundDrawablePadding = this
    }
    it.modifier = modifier
  }


  /**
   * 更新主题
   *
   * NOTE: 如果之前使用了主题，主题更新时我们也要更新对应的值
   * 为了避免逻辑错误，后续手动修改了某个样式时将不会被主题覆盖
   * 除非使用的是主题中的值，如 [Colors.background] [Typography.body1] 等
   */
  override fun updateUiKitTheme() {
    /** 修改样式前先备份一下文本色，避免被 [TextStyle.apply] 覆盖 */
    val backupTextColor = textColor
    style?.resolveTypography()?.also { style = it }
    iconStyle?.resolveStyle()?.also { iconStyle = it }
    backupTextColor?.resolveColor()?.apply(::setTextColor)

    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(parent as? ViewGroup) }
    }
  }
}


/** 着色调整的具体实现 */
internal data class ShaderModifier(
  val shader: Shader? = null,
  val shaderProvider: ((View) -> Shader)? = null,
) : Modifier {
  override fun View.realize(parent: ViewGroup?) {
    this as TextView
    if (shader != null) paint.shader = shader
    if (shaderProvider != null) doOnLayout {
      paint.shader = shaderProvider.invoke(this)
    }
  }
}


/** 创建一个 [Text] 视图 */
fun UiKit.Text(
  text: String? = null,
  modifier: Modifier = Modifier,
  /** 自动调整文本大小以适应容器，默认关闭 */
  isAutoSize: Boolean? = null,
  /** 文本对齐 */
  align: Alignment? = null,
  /** 文本溢出时的处理 */
  overflow: TextOverflow? = null,
  /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
  marquee: TextMarquee? = null,
  /** 限制文本行数 */
  maxLines: Int? = null,
  /** 限制文本最长数量 */
  maxLength: Int? = null,
  /** 长按选择文本 */
  isSelectable: Boolean? = null,
  /** 文本旁边的图标，可设置上下左右四个方向 */
  icons: EdgeIcons? = null,
  /** 文本旁边的图标的样式 */
  iconsStyle: IconStyle? = null,
  /** 文本与图标 [icons] 之间的间隔 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.color] */
  color: Color = Color.Unset,
  /** [TextStyle.lineHeight] */
  lineHeight: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.decoration] */
  decoration: TextDecoration? = null,
  /** [TextStyle.font] */
  font: Font? = null,
  /** [TextStyle.fontStyle] */
  fontStyle: FontStyle = FontStyle.Normal,
  /** [TextStyle.fontSize] */
  fontSize: SizeUnit = TextUnit.Inherit,
  /** [TextStyle.fontName] */
  fontName: String? = PingFangFont.Regular,
  /** [TextStyle.letterSpacing] */
  letterSpacing: SizeUnit = SizeUnit.Unspecified,
  /** 文本样式（不是字体样式 [FontStyle]） */
  style: TextStyle = currentTypography.body1,
): Text = With(::Text) {
  it.update(
    text = text,
    modifier = modifier,
    isAutoSize = isAutoSize,
    align = align,
    overflow = overflow,
    marquee = marquee,
    maxLines = maxLines,
    maxLength = maxLength,
    isSelectable = isSelectable,
    icons = icons,
    iconsStyle = iconsStyle,
    spaceBetween = spaceBetween,
    color = color,
    lineHeight = lineHeight,
    decoration = decoration,
    font = font,
    fontStyle = fontStyle,
    fontSize = fontSize,
    fontName = fontName,
    letterSpacing = letterSpacing,
    style = style,
  )
}


/**
 * 为按钮添加一个 [Text] 视图
 * NOTE: 对于按钮内的文本，我们应该默认使用 [Typography.button] 样式
 */
fun ButtonLayout.Text(
  text: String? = null,
  modifier: Modifier = Modifier,
  /** 自动调整文本大小以适应容器，默认关闭 */
  isAutoSize: Boolean? = null,
  /** 文本对齐 */
  align: Alignment? = null,
  /** 文本溢出时的处理 */
  overflow: TextOverflow? = null,
  /** 跑马灯效果，当开启时 [overflow] 的设置将失效，且 [maxLines] 只能为 1 */
  marquee: TextMarquee? = null,
  /** 限制文本行数 */
  maxLines: Int? = null,
  /** 限制文本最长数量 */
  maxLength: Int? = null,
  /** 长按选择文本 */
  isSelectable: Boolean? = null,
  /** 文本旁边的图标，可设置上下左右四个方向 */
  icons: EdgeIcons? = null,
  /** 文本旁边的图标的样式 */
  iconsStyle: IconStyle? = null,
  /** 文本与图标 [icons] 之间的间隔 */
  spaceBetween: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.color] */
  color: Color = Color.Unset,
  /** [TextStyle.lineHeight] */
  lineHeight: SizeUnit = SizeUnit.Unspecified,
  /** [TextStyle.decoration] */
  decoration: TextDecoration? = null,
  /** [TextStyle.font] */
  font: Font? = null,
  /** [TextStyle.fontStyle] */
  fontStyle: FontStyle = FontStyle.Normal,
  /** [TextStyle.fontSize] */
  fontSize: SizeUnit = TextUnit.Inherit,
  /** [TextStyle.fontName] */
  fontName: String? = PingFangFont.Regular,
  /** [TextStyle.letterSpacing] */
  letterSpacing: SizeUnit = SizeUnit.Unspecified,
  /** 文本样式（不是字体样式 [FontStyle]） */
  style: TextStyle = currentTypography.body1,
) = (this as UiKit).Text(
  text = text,
  modifier = modifier,
  isAutoSize = isAutoSize,
  align = align,
  overflow = overflow,
  marquee = marquee,
  maxLines = maxLines,
  maxLength = maxLength,
  isSelectable = isSelectable,
  icons = icons,
  iconsStyle = iconsStyle,
  spaceBetween = spaceBetween,
  color = color,
  lineHeight = lineHeight,
  decoration = decoration,
  font = font,
  fontStyle = fontStyle,
  fontSize = fontSize,
  fontName = fontName,
  letterSpacing = letterSpacing,
  style = style
)