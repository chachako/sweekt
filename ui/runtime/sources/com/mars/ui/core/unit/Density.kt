/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mars.ui.core.unit

import android.graphics.Rect
import com.mars.ui.UiKit
import kotlin.math.roundToInt

/**
 * The logical density of the display. This is a scaling factor for the [Dp] unit.
 */
internal val density: Float get() = UiKit.currentContext.resources.displayMetrics.density

/**
 * Current user preference for the scaling factor for fonts.
 */
internal val fontScale: Float get() = UiKit.currentContext.resources.configuration.fontScale

/**
 * Convert [Dp] to Sp. Sp is used for font size, etc.
 */
fun Dp.toSp(): TextUnit = TextUnit.Sp(value / fontScale)

fun SizeUnit.toPxOrNull(baseSize: Float? = null): Float? = when (this) {
  is TextUnit -> when (type) {
    TextUnitType.Sp -> value * fontScale * density
    TextUnitType.Em -> baseSize.apply { if (this == null) error("如果是 em 单位则必须指定一个基础大小值") }!! * value
    TextUnitType.Inherit -> null // Do nothing
  }
  is Px -> value
  is Dp -> value * density
  SizeUnit.Unspecified -> null
  else -> error("Unknown size unit: ${this.javaClass.name}")
}

fun SizeUnit.toIntPxOrNull(baseSize: Float? = null): Int? = toPxOrNull(baseSize)?.roundToInt()

/**
 * Convert SizeUnit to pixels. Pixels are used to paint to Canvas.
 * @throws IllegalStateException Unknown unit or if TextUnit other than SP unit is specified.
 */
fun SizeUnit.toPx(): Float {
  if (this == SizeUnit.Unspecified) error("不可以将未指定的 size 转换为数值")
  if (this is Px) return value
  if (this is TextUnit) {
    check(type == TextUnitType.Sp) { "If text unit, Only Sp can convert to Px" }
    return value * fontScale * density
  }
  if (this is Dp) return value * density
  if (this is DpCubed) return value * density
  if (this is DpInverse) return value * density
  if (this is DpSquared) return value * density
  error("Unknown size unit: ${this.javaClass.name}")
}

/**
 * Convert SizeUnit to [Int] by rounding
 */
fun SizeUnit.toIntPx(): Int {
  val px = toPx()
  return if (px.isInfinite()) Int.MAX_VALUE else px.roundToInt()
}

/**
 * Convert SizeUnit to [Dp].
 * @throws IllegalStateException Unknown unit or if TextUnit other than SP unit is specified.
 */
fun SizeUnit.toDp(): Dp {
  if (this is Px) return value.toDp()
  if (this is TextUnit) {
    check(type == TextUnitType.Sp) { "If text unit, Only Sp can convert to Dp" }
    return Dp(value * fontScale)
  }
  if (this is Dp) return this
  error("Unknown size unit: ${this.javaClass.name}")
}

/**
 * Convert an [Int] pixel value to [Dp].
 */
fun Int.toDp(): Dp = (this / density).dp

/**
 * Convert an [Int] pixel value to Sp.
 */
fun Int.toSp(): TextUnit = (this / (fontScale * density)).sp

/** Convert a [Float] pixel value to a Dp */
fun Float.toDp(): Dp = (this / density).dp

/** Convert a [Float] pixel value to a Sp */
fun Float.toSp(): TextUnit = (this / (fontScale * density)).sp

/**
 * Convert a [Bounds] to a [Rect].
 */
fun Bounds.toRect(): Rect {
  return Rect(
    left.toIntPx(),
    top.toIntPx(),
    right.toIntPx(),
    bottom.toIntPx()
  )
}

