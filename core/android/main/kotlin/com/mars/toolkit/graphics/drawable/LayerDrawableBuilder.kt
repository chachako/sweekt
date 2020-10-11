@file:Suppress("MemberVisibilityCanBePrivate")

package com.mars.toolkit.graphics.drawable

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity.NO_GRAVITY
import androidx.annotation.RequiresApi
import java.util.*

inline fun buildLayerDrawable(block: LayerDrawableBuilder.() -> Unit) =
  LayerDrawableBuilder().apply(block).build()

class LayerDrawableBuilder {
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private var paddingMode = LayerDrawableCompat.PADDING_MODE_NEST
  private var paddingLeft = 0
  private var paddingTop = 0
  private var paddingRight = 0
  private var paddingBottom = 0
  private var paddingStart = 0
  private var paddingEnd = 0
  private val layers = ArrayList<Layer>()

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  fun paddingMode(mode: Int) = apply { paddingMode = mode }

  fun paddingLeft(padding: Int) = apply { paddingLeft = padding }
  fun paddingTop(padding: Int) = apply { paddingTop = padding }
  fun paddingRight(padding: Int) = apply { paddingRight = padding }
  fun paddingBottom(padding: Int) = apply { paddingBottom = padding }

  fun paddingStart(padding: Int) = apply { paddingStart = padding }

  fun paddingEnd(padding: Int) = apply { paddingEnd = padding }

  fun padding(padding: Int) = apply {
    paddingLeft(padding).paddingTop(padding).paddingRight(padding).paddingBottom(padding)
  }

  fun paddingRelative(padding: Int) = apply {
    paddingStart(padding).paddingTop(padding).paddingEnd(padding).paddingBottom(padding)
  }

  fun add(
    drawable: Drawable,
    gravity: Int = NO_GRAVITY,
    width: Int = DIMEN_UNDEFINED,
    height: Int = DIMEN_UNDEFINED,
    insetLeft: Int = DIMEN_UNDEFINED,
    insetTop: Int = DIMEN_UNDEFINED,
    insetRight: Int = DIMEN_UNDEFINED,
    insetBottom: Int = DIMEN_UNDEFINED,
    insetStart: Int = DIMEN_UNDEFINED,
    insetEnd: Int = DIMEN_UNDEFINED
  ) = apply {
    layers.add(Layer(drawable).also { layer ->
      if (width != DIMEN_UNDEFINED) layer.width = width
      if (height != DIMEN_UNDEFINED) layer.height = height
      if (insetLeft != DIMEN_UNDEFINED) layer.insetLeft = insetLeft
      if (insetTop != DIMEN_UNDEFINED) layer.insetTop = insetTop
      if (insetRight != DIMEN_UNDEFINED) layer.insetRight = insetRight
      if (insetBottom != DIMEN_UNDEFINED) layer.insetBottom = insetBottom
      if (insetStart != DIMEN_UNDEFINED) layer.insetStart = insetStart
      if (insetEnd != DIMEN_UNDEFINED) layer.insetEnd = insetEnd
      if (insetEnd != NO_GRAVITY) layer.gravity = gravity
    })
  }

  fun width(width: Int) = apply { layers.last().width = width }
  fun height(height: Int) = apply { layers.last().height = height }

  fun insetLeft(inset: Int) = apply { layers.last().insetLeft = inset }
  fun insetTop(inset: Int) = apply { layers.last().insetTop = inset }
  fun insetRight(inset: Int) = apply { layers.last().insetRight = inset }
  fun insetBottom(inset: Int) = apply { layers.last().insetBottom = inset }

  fun insetStart(inset: Int) = apply { layers.last().insetStart = inset }

  fun insetEnd(inset: Int) = apply { layers.last().insetEnd = inset }

  fun inset(inset: Int) = apply {
    insetLeft(inset).insetTop(inset).insetRight(inset).insetBottom(
      inset
    )
  }

  fun inset(insetLeft: Int, insetTop: Int, insetRight: Int, insetBottom: Int) = apply {
    insetLeft(insetLeft).insetTop(insetTop).insetRight(insetRight).insetBottom(insetBottom)
  }

  fun insetRelative(inset: Int) = apply {
    insetStart(inset).insetTop(inset).insetEnd(inset).insetBottom(
      inset
    )
  }

  fun insetRelative(
    insetStart: Int, insetTop: Int, insetEnd: Int, insetBottom: Int
  ) = apply {
    insetStart(insetStart).insetTop(insetTop).insetEnd(insetEnd).insetBottom(insetBottom)
  }

  @RequiresApi(Build.VERSION_CODES.M)
  fun gravity(gravity: Int) = apply { layers.last().gravity = gravity }

  fun modify(
    index: Int,
    drawable: Drawable,
    gravity: Int = NO_GRAVITY,
    width: Int = DIMEN_UNDEFINED,
    height: Int = DIMEN_UNDEFINED,
    insetLeft: Int = DIMEN_UNDEFINED,
    insetTop: Int = DIMEN_UNDEFINED,
    insetRight: Int = DIMEN_UNDEFINED,
    insetBottom: Int = DIMEN_UNDEFINED,
    insetStart: Int = DIMEN_UNDEFINED,
    insetEnd: Int = DIMEN_UNDEFINED
  ) = apply {
    val layer = layers[index]
    layer.drawable = drawable
    if (width != DIMEN_UNDEFINED) layer.width = width
    if (height != DIMEN_UNDEFINED) layer.height = height
    if (insetLeft != DIMEN_UNDEFINED) layer.insetLeft = insetLeft
    if (insetTop != DIMEN_UNDEFINED) layer.insetTop = insetTop
    if (insetRight != DIMEN_UNDEFINED) layer.insetRight = insetRight
    if (insetBottom != DIMEN_UNDEFINED) layer.insetBottom = insetBottom
    if (insetStart != DIMEN_UNDEFINED) layer.insetStart = insetStart
    if (insetEnd != DIMEN_UNDEFINED) layer.insetEnd = insetEnd
    if (insetEnd != NO_GRAVITY) layer.gravity = gravity
  }

  fun build(): LayerDrawableCompat {
    val layerDrawable = LayerDrawableCompat(layers.map { it.drawable }
      .toTypedArray())
    for (i in 0 until layers.size) {
      val layer = layers[i]
      layerDrawable.setLayerSize(i, layer.width, layer.height)
      layerDrawable.setLayerInset(
        i, layer.insetLeft, layer.insetTop, layer.insetRight, layer.insetBottom
      )
      if (layer.insetStart != DIMEN_UNDEFINED || layer.insetEnd != DIMEN_UNDEFINED) {
        layerDrawable.setLayerInsetRelative(
          i, layer.insetStart, layer.insetTop, layer.insetEnd, layer.insetBottom
        )
      }
      layerDrawable.setId(i, i)
      layerDrawable.setLayerGravity(i, layer.gravity)
      layerDrawable.setLayerInsetStart(i, layer.insetStart)
      layerDrawable.setLayerInsetEnd(i, layer.insetEnd)
    }
    layerDrawable.paddingMode = paddingMode
    layerDrawable.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    if (paddingStart != DIMEN_UNDEFINED || paddingEnd != DIMEN_UNDEFINED) {
      layerDrawable.setPaddingRelative(
        paddingStart, paddingTop, paddingEnd, paddingBottom
      )
    }

    return layerDrawable
  }

  internal class Layer(var drawable: Drawable) {
    var gravity: Int = NO_GRAVITY
    var width: Int = -1
    var height: Int = -1
    var insetLeft: Int = 0
    var insetTop: Int = 0
    var insetRight: Int = 0
    var insetBottom: Int = 0
    var insetStart: Int = DIMEN_UNDEFINED
    var insetEnd: Int = DIMEN_UNDEFINED
  }

  companion object {
    const val DIMEN_UNDEFINED = Int.MIN_VALUE
  }
}