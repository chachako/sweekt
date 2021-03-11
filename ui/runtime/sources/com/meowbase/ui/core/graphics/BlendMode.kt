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

@file:Suppress("SpellCheckingInspection")

package com.meowbase.ui.core.graphics

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff


/**
 * 实际上是
 * https://developer.android.com/reference/android/graphics/PorterDuff.Mode
 *
 *
 * Algorithms to use when painting on the canvas.
 *
 * When drawing a shape or image onto a canvas, different algorithms can be
 * used to blend the pixels. The different values of [BlendMode] specify
 * different such algorithms.
 *
 * Each algorithm has two inputs, the _source_, which is the image being drawn,
 * and the _destination_, which is the image into which the source image is
 * being composited. The destination is often thought of as the _background_.
 * The source and destination both have four color channels, the red, green,
 * blue, and alpha channels. These are typically represented as numbers in the
 * range 0.0 to 1.0. The output of the algorithm also has these same four
 * channels, with values computed from the source and destination.
 *
 * The horizontal and vertical bars in these images show the red, green, and
 * blue channels with varying opacity levels, then all three color channels
 * together with those same varying opacity levels, then all three color
 * channels set to zero with those varying opacity levels, then two bars showing
 * a red/green/blue repeating gradient, the first with full opacity and the
 * second with partial opacity, and finally a bar with the three color channels
 * set to zero but the opacity varying in a repeating gradient.
 *
 * ## Application to the [Canvas] API
 *
 * When using [Canvas.saveLayer] and [Canvas.restore], the blend mode of the
 * [Paint] given to the [Canvas.saveLayer] will be applied when
 * [Canvas.restore] is called. Each call to [Canvas.saveLayer] introduces a new
 * layer onto which shapes and images are painted; when [Canvas.restore] is
 * called, that layer is then composited onto the parent layer, with the source
 * being the most-recently-drawn shapes and images, and the destination being
 * the parent layer. (For the first [Canvas.saveLayer] call, the parent layer
 * is the canvas itself.)
 *
 * See also:
 *
 *  * [Paint.getBlendMode], which uses [BlendMode] to define the compositing
 *    strategy.
 */
enum class BlendMode {

  /**
   * Drop both the source and destination images, leaving nothing.
   */
  Clear,

  /**
   * Drop the destination image, only paint the source image.
   *
   * Conceptually, the destination is first cleared, then the source image is
   * painted.
   */
  Src,

  /**
   * Drop the source image, only paint the destination image.
   *
   * Conceptually, the source image is discarded, leaving the destination
   * untouched.
   */
  Dst,

  /**
   * Composite the source image over the destination image.
   *
   * This is the default value. It represents the most intuitive case, where
   * shapes are painted on top of what is below, with transparent areas showing
   * the destination layer.
   */
  SrcOver,

  /**
   * Composite the source image under the destination image.
   *
   * This is the opposite of [SrcOver].
   *
   * This is useful when the source image should have been painted before the
   * destination image, but could not be.
   */
  DstOver,

  /**
   * Show the source image, but only where the two images overlap. The
   * destination image is not rendered, it is treated merely as a mask. The
   * color channels of the destination are ignored, only the opacity has an
   * effect.
   *
   * To show the destination image instead, consider [DstIn].
   *
   * To reverse the semantic of the mask (only showing the source where the
   * destination is absent, rather than where it is present), consider
   * [SrcOut].
   */
  SrcIn,

  /**
   * Show the destination image, but only where the two images overlap. The
   * source image is not rendered, it is treated merely as a mask. The color
   * channels of the source are ignored, only the opacity has an effect.
   *
   * To show the source image instead, consider [SrcIn].
   *
   * To reverse the semantic of the mask (only showing the source where the
   * destination is present, rather than where it is absent), consider [DstOut].
   */
  DstIn,

  /**
   * Show the source image, but only where the two images do not overlap. The
   * destination image is not rendered, it is treated merely as a mask. The color
   * channels of the destination are ignored, only the opacity has an effect.
   *
   * To show the destination image instead, consider [DstOut].
   *
   * To reverse the semantic of the mask (only showing the source where the
   * destination is present, rather than where it is absent), consider [SrcIn].
   *
   * This corresponds to the "Source out Destination" Porter-Duff operator.
   */
  SrcOut,

  /**
   * Show the destination image, but only where the two images do not overlap. The
   * source image is not rendered, it is treated merely as a mask. The color
   * channels of the source are ignored, only the opacity has an effect.
   *
   * To show the source image instead, consider [SrcOut].
   *
   * To reverse the semantic of the mask (only showing the destination where the
   * source is present, rather than where it is absent), consider [DstIn].
   *
   * This corresponds to the "Destination out Source" Porter-Duff operator.
   */
  DstOut,

  /**
   * Composite the source image over the destination image, but only where it
   * overlaps the destination.
   *
   * This is essentially the [SrcOver] operator, but with the output's opacity
   * channel being set to that of the destination image instead of being a
   * combination of both image's opacity channels.
   *
   * For a variant with the destination on top instead of the source, see
   * [DstAtop].
   */
  SrcAtop,

  /**
   * Composite the destination image over the source image, but only where it
   * overlaps the source.
   *
   * This is essentially the [DstOver] operator, but with the output's opacity
   * channel being set to that of the source image instead of being a
   * combination of both image's opacity channels.
   *
   * For a variant with the source on top instead of the destination, see
   * [SrcAtop].
   */
  DstAtop,

  /**
   * Apply a bitwise `xor` operator to the source and destination images. This
   * leaves transparency where they would overlap.
   */
  Xor,

  /**
   * Sum the components of the source and destination images.
   *
   * Transparency in a pixel of one of the images reduces the contribution of
   * that image to the corresponding output pixel, as if the color of that
   * pixel in that image was darker.
   *
   */
  Plus,

  /**
   * Multiply the color components of the source and destination images.
   *
   * This can only result in the same or darker colors (multiplying by white,
   * 1.0, results in no change; multiplying by black, 0.0, results in black).
   *
   * When compositing two opaque images, this has similar effect to overlapping
   * two transparencies on a projector.
   *
   * For a variant that also multiplies the alpha channel, consider [Multiply].
   *
   * See also:
   *
   *  * [Screen], which does a similar computation but inverted.
   *  * [Overlay], which combines [Modulate] and [Screen] to favor the
   *    destination image.
   */
  Modulate,

  /**
   * Multiply the inverse of the components of the source and destination
   * images, and inverse the result.
   *
   * Inverting the components means that a fully saturated channel (opaque
   * white) is treated as the value 0.0, and values normally treated as 0.0
   * (black, transparent) are treated as 1.0.
   *
   * This is essentially the same as [Modulate] blend mode, but with the values
   * of the colors inverted before the multiplication and the result being
   * inverted back before rendering.
   *
   * This can only result in the same or lighter colors (multiplying by black,
   * 1.0, results in no change; multiplying by white, 0.0, results in white).
   * Similarly, in the alpha channel, it can only result in more opaque colors.
   *
   * This has similar effect to two projectors displaying their images on the
   * same screen simultaneously.
   *
   * See also:
   *
   *  * [Modulate], which does a similar computation but without inverting the
   *    values.
   *  * [Overlay], which combines [Modulate] and [Screen] to favor the
   *    destination image.
   */
  Screen, // The last coeff mode.

  /**
   * Multiply the components of the source and destination images after
   * adjusting them to favor the destination.
   *
   * Specifically, if the destination value is smaller, this multiplies it with
   * the source value, whereas is the source value is smaller, it multiplies
   * the inverse of the source value with the inverse of the destination value,
   * then inverts the result.
   *
   * Inverting the components means that a fully saturated channel (opaque
   * white) is treated as the value 0.0, and values normally treated as 0.0
   * (black, transparent) are treated as 1.0.
   *
   * See also:
   *
   *  * [Modulate], which always multiplies the values.
   *  * [Screen], which always multiplies the inverses of the values.
   */
  Overlay,

  /**
   * Composite the source and destination image by choosing the lowest value
   * from each color channel.
   *
   * The opacity of the output image is computed in the same way as for
   * [SrcOver].
   */
  Darken,

  /**
   * Composite the source and destination image by choosing the highest value
   * from each color channel.
   *
   * The opacity of the output image is computed in the same way as for
   * [SrcOver].
   */
  Lighten,

  /** 不设置任何的混合模式，一切使用系统默认的 */
  Unset,
}

/**
 * Convert the [BlendMode] to the corresponding [PorterDuff.Mode] if it exists,
 * falling back on the default of [PorterDuff.Mode.SRC_OVER] for unsupported types
 */
internal fun BlendMode.toPorterDuffMode(): PorterDuff.Mode? = when (this) {
  BlendMode.Clear -> PorterDuff.Mode.CLEAR
  BlendMode.Src -> PorterDuff.Mode.SRC
  BlendMode.Dst -> PorterDuff.Mode.DST
  BlendMode.SrcOver -> PorterDuff.Mode.SRC_OVER
  BlendMode.DstOver -> PorterDuff.Mode.DST_OVER
  BlendMode.SrcIn -> PorterDuff.Mode.SRC_IN
  BlendMode.DstIn -> PorterDuff.Mode.DST_IN
  BlendMode.SrcOut -> PorterDuff.Mode.SRC_OUT
  BlendMode.DstOut -> PorterDuff.Mode.DST_OUT
  BlendMode.SrcAtop -> PorterDuff.Mode.SRC_ATOP
  BlendMode.DstAtop -> PorterDuff.Mode.DST_ATOP
  BlendMode.Xor -> PorterDuff.Mode.XOR
  BlendMode.Plus -> PorterDuff.Mode.ADD
  BlendMode.Screen -> PorterDuff.Mode.SCREEN
  BlendMode.Overlay -> PorterDuff.Mode.OVERLAY
  BlendMode.Darken -> PorterDuff.Mode.DARKEN
  BlendMode.Lighten -> PorterDuff.Mode.LIGHTEN
  BlendMode.Modulate -> {
    // b/73224934 Android PorterDuff Multiply maps to Skia Modulate
    PorterDuff.Mode.MULTIPLY
  }
  else -> null
}