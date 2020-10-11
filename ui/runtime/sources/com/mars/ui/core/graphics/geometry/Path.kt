@file:Suppress("NOTHING_TO_INLINE")

package com.mars.ui.core.graphics.geometry

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.RectF

inline fun Path.addRoundRect(
  rectF: RectF,
  topLeftRadius: Radius,
  topRightRadius: Radius,
  bottomRightRadius: Radius,
  bottomLeftRadius: Radius,
) {
  val radii = FloatArray(8)
  radii[0] = topLeftRadius.x
  radii[1] = topLeftRadius.y

  radii[2] = topRightRadius.x
  radii[3] = topRightRadius.y

  radii[4] = bottomRightRadius.x
  radii[5] = bottomRightRadius.y

  radii[6] = bottomLeftRadius.x
  radii[7] = bottomLeftRadius.y
  addRoundRect(rectF, radii, Path.Direction.CCW)
}

inline fun Path.addRoundRect(roundRect: RoundRect) {
  val radii = FloatArray(8)
  val rectF = RectF()
  rectF.set(roundRect.left, roundRect.top, roundRect.right, roundRect.bottom)
  radii[0] = roundRect.topLeftRadius.x
  radii[1] = roundRect.topLeftRadius.y

  radii[2] = roundRect.topRightRadius.x
  radii[3] = roundRect.topRightRadius.y

  radii[4] = roundRect.bottomRightRadius.x
  radii[5] = roundRect.bottomRightRadius.y

  radii[6] = roundRect.bottomLeftRadius.x
  radii[7] = roundRect.bottomLeftRadius.y
  addRoundRect(rectF, radii, Path.Direction.CCW)
}

inline fun Path.addPath(path: Path, offset: Offset) =
  addPath(path, offset.x, offset.y)

inline fun Path.translate(offset: Offset) = Matrix().let {
  it.reset()
  it.setTranslate(offset.x, offset.y)
  transform(it)
}
