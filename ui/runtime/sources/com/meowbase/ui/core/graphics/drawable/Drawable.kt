/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.ui.core.graphics.drawable

import android.content.res.ColorStateList
import android.graphics.*
import androidx.core.graphics.toRectF
import com.meowbase.ui.core.Border
import com.meowbase.ui.core.graphics.*
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.Outline
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.unit.toPx


typealias NativeDrawable = android.graphics.drawable.Drawable
typealias NativeConstantState = android.graphics.drawable.Drawable.ConstantState


/**
 * 一个具有丰富属性的可绘制对象
 *
 * @author 凛
 * @date 2020/9/30 下午7:45
 * @github https://github.com/RinOrz
 */
class Drawable(attributes: Attributes? = null) : NativeDrawable() {
  var attributes: Attributes = Attributes()
    private set
  private var fillPaint: Paint? = null
  private var borderPaint: Paint? = null
  private var outline: Outline? = null
  private var colorStateList: ColorStateList?= null

  private var _bounds: RectF? = null
  private val bounds: RectF get() = _bounds!!

  init {
    attributes?.also { this.attributes = it }
  }

  override fun mutate(): NativeDrawable = apply {
    attributes = Attributes(attributes)
  }

  override fun setAlpha(alpha: Int) {
    attributes.alpha = alpha
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
    attributes.colorFilter = colorFilter
  }

  override fun getOutline(nativeOutline: NativeOutline) {
    outline?.toNativeOutline()?.also(nativeOutline::set)
      ?: super.getOutline(nativeOutline).apply { nativeOutline.alpha = 1f }
  }

  override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

  override fun getConstantState(): NativeConstantState? = attributes

  override fun isStateful(): Boolean = super.isStateful()
    || attributes.border?.colorStateList?.isStateful == true
    || colorStateList?.isStateful == true

  override fun onBoundsChange(bounds: Rect?) {
    super.onBoundsChange(bounds)
    if (bounds == null) return
    _bounds = bounds.toRectF()
    if (borderPaint != null) {
      val inset = borderPaint!!.strokeWidth / 2
      _bounds!!.inset(inset, inset)
    }
    attributes.shape?.getOutline(_bounds!!)?.also { outline = it }
  }

  override fun onStateChange(state: IntArray): Boolean =
    updateColorsForState(state).also { if (it) invalidateSelf() }

  override fun draw(canvas: Canvas) {
    val useLayer = canvas.drawEffect()
    canvas.drawOutline()
    if (useLayer) canvas.restore()
  }

  /** 更新颜色状态 */
  private fun updateColorsForState(state: IntArray): Boolean {
    var invalidateSelf = false
    if (attributes.colorStates != null && fillPaint != null) {
      val old = fillPaint!!.color
      val new = colorStateList!!.getColorForState(state, old)
      if (old != new) {
        fillPaint!!.color = new
        invalidateSelf = true
      }
    }
    if (attributes.border?.colorStates != null && borderPaint != null) {
      val old = borderPaint!!.color
      val new = attributes.border!!.colorStateList!!.getColorForState(state, old)
      if (old != new) {
        borderPaint!!.color = new
        invalidateSelf = true
      }
    }
    return invalidateSelf
  }

  private fun Canvas.drawEffect(): Boolean {
    val haveEffect = attributes.colorFilter != null || attributes.alpha != null
    val useLayer = borderPaint != null && fillPaint != null
    if (haveEffect) {
      // 需要将效果应用到多个图层中，以避免显示错误
      if (useLayer) {
        val resolve = borderPaint?.strokeWidth ?: 0f
        val paint = Paint().apply {
          if (attributes.alpha != null) {
            alpha = attributes.alpha!!
          }
          if (attributes.colorFilter != null) {
            colorFilter = attributes.colorFilter!!
          }
        }
        saveLayer(
          bounds.left - resolve,
          bounds.top - resolve,
          bounds.right + resolve,
          bounds.bottom + resolve,
          paint
        )
        return true
      }

      borderPaint?.colorFilter = attributes.colorFilter
      fillPaint?.colorFilter = attributes.colorFilter
      attributes.alpha?.also { borderPaint?.alpha = it }
      attributes.colorFilter?.also { fillPaint?.colorFilter = it }
    }
    return false
  }

  private fun Canvas.drawOutline() {
    outline?.apply {
      if (usePath) {
        fillPaint?.also { drawPath(forcePath, it) }
        borderPaint?.also { drawPath(forcePath, it) }
      } else when {
        isOval -> {
          fillPaint?.also { drawOval(bounds, it) }
          borderPaint?.also { drawOval(bounds, it) }
        }
        rectRadius == 0F -> {
          fillPaint?.also { drawRect(bounds, it) }
          borderPaint?.also { drawRect(bounds, it) }
        }
        else -> {
          fillPaint?.also { drawRoundRect(bounds, rectRadius, rectRadius, it) }
          borderPaint?.also { drawRoundRect(bounds, rectRadius, rectRadius, it) }
        }
      }
    } ?: run {
      fillPaint?.also { drawRect(bounds, it) }
      borderPaint?.also { drawRect(bounds, it) }
    }
  }

  inner class Attributes(orig: Attributes? = null) : NativeConstantState() {
    /** 需要绘制的形状 */
    var shape: Shape? = null

    /** 绘制图层的透明度 */
    var alpha: Int? = null

    /** 背景色 */
    var color: Color = Color.Unspecified
      set(value) {
        field = value
        value.useOrNull()?.apply {
          fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.style = Paint.Style.FILL
            it.color = argb
          }
        }
      }

    /** 带有状态的背景色 */
    var colorStates: ColorStates? = null
      set(value) {
        field = value
        value?.apply {
          colorStateList = toColorStateList()
          color = colorStateList!!.defaultColor.toColor()
        }
      }

    /** 绘制图层的滤色镜 */
    var colorFilter: ColorFilter? = null

    /** 边框 */
    var border: Border? = null
      set(value) {
        field = value
        value?.apply {
          borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.style = Paint.Style.STROKE
            it.strokeWidth = size.toPx()
            it.color = color.argb
            if (ends != Border.Ends.None) it.strokeCap = ends.native
            if (joins != Border.Joins.Miter) it.strokeJoin = joins.native
          }
        }
      }

    init {
      orig?.also {
        color = it.color
        colorStates = it.colorStates
        alpha = it.alpha
        shape = it.shape
        border = it.border
        colorFilter = it.colorFilter
      }
    }

    override fun getChangingConfigurations(): Int = 0

    override fun newDrawable(): Drawable = Drawable().also { it.attributes = this }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as Attributes

      if (shape != other.shape) return false
      if (alpha != other.alpha) return false
      if (colorFilter != other.colorFilter) return false
      if (colorStates != other.colorStates) return false
      if (color != other.color) return false
      if (border != other.border) return false

      return true
    }

    override fun hashCode(): Int {
      var result = shape?.hashCode() ?: 0
      result = 31 * result + (alpha ?: 0)
      result = 31 * result + (colorFilter?.hashCode() ?: 0)
      result = 31 * result + (colorStates?.hashCode() ?: 0)
      result = 31 * result + color.hashCode()
      result = 31 * result + (border?.hashCode() ?: 0)
      return result
    }


  }
}


/** 创建并返回一个可绘制对象 */
inline fun drawWith(attributesBlock: Drawable.Attributes.() -> Unit): NativeDrawable =
  Drawable().apply { attributes.also(attributesBlock) }

/**
 * 创建并返回一个可绘制对象
 *
 * @param color 背景色
 * @param alpha 绘制图层的透明度
 * @param shape 需要绘制的形状
 * @param border 边框
 * @param colorStates 带有状态的背景色
 * @param colorFilter 绘制图层的滤色镜
 */
fun drawWith(
  color: Color = Color.Unspecified,
  alpha: Int? = null,
  shape: Shape? = null,
  border: Border? = null,
  colorStates: ColorStates? = null,
  colorFilter: ColorFilter? = null,
): NativeDrawable = drawWith {
  this.color = color
  this.shape = shape
  this.alpha = alpha
  this.border = border
  this.colorStates = colorStates
  this.colorFilter = colorFilter
}