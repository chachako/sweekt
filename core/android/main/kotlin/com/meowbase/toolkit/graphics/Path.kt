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

@file:Suppress("NOTHING_TO_INLINE", "EXTENSION_SHADOWED_BY_MEMBER")

package com.meowbase.toolkit.graphics

import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import com.meowbase.toolkit.float

inline fun Path.relativeCubicTo(
  dx1: Float,
  dy1: Float,
  dx2: Float,
  dy2: Float,
  dx3: Float,
  dy3: Float
) = rCubicTo(
  dx1, dy1,
  dx2, dy2,
  dx3, dy3
)

inline fun Path.relativeMoveTo(dx: Float, dy: Float) = rMoveTo(dx, dy)

inline fun Path.relativeLineTo(dx: Float, dy: Float) = rLineTo(dx, dy)

inline fun Path.quadraticBezierTo(x1: Float, y1: Float, x2: Float, y2: Float) =
  quadTo(x1, y1, x2, y2)

inline fun Path.relativeQuadraticBezierTo(dx1: Float, dy1: Float, dx2: Float, dy2: Float) =
  rQuadTo(dx1, dy1, dx2, dy2)

inline fun Path.addRect(rect: Rect) =
  addRect(RectF().apply { set(rect) }, Path.Direction.CCW)

inline fun Path.addRect(rectF: RectF) =
  addRect(rectF, Path.Direction.CCW)

inline fun Path.addOval(oval: Rect) =
  addOval(RectF().apply { set(oval) }, Path.Direction.CCW)

inline fun Path.addOval(oval: RectF) =
  addOval(oval, Path.Direction.CCW)

inline fun Path.addRoundRect(
  rectF: RectF,
  topLeftRadius: Number,
  topRightRadius: Number,
  bottomRightRadius: Number,
  bottomLeftRadius: Number,
  dir: Path.Direction = Path.Direction.CCW
) {
  val radii = FloatArray(8)
  radii[0] = topLeftRadius.float
  radii[1] = topLeftRadius.float

  radii[2] = topRightRadius.float
  radii[3] = topRightRadius.float

  radii[4] = bottomRightRadius.float
  radii[5] = bottomRightRadius.float

  radii[6] = bottomLeftRadius.float
  radii[7] = bottomLeftRadius.float
  addRoundRect(rectF, radii, dir)
}

inline fun Path.addRoundRect(
  rect: Rect,
  rx: Number,
  ry: Number,
  dir: Path.Direction = Path.Direction.CCW
) {
  addRoundRect(
    rect.left.float,
    rect.top.float,
    rect.right.float,
    rect.bottom.float,
    rx.float,
    ry.float,
    dir
  )
}

inline fun Path.addRoundRect(
  rect: Rect,
  radius: Number,
  dir: Path.Direction = Path.Direction.CCW
) = addRoundRect(rect, radius, radius, dir)


inline fun Path.addRoundRect(
  rectF: RectF,
  rx: Number,
  ry: Number,
  dir: Path.Direction = Path.Direction.CCW
) {
  addRoundRect(
    rectF.left.float,
    rectF.top.float,
    rectF.right.float,
    rectF.bottom.float,
    rx.float,
    ry.float,
    dir
  )
}

inline fun Path.addRoundRect(
  rectF: RectF,
  radius: Number,
  dir: Path.Direction = Path.Direction.CCW
) = addRoundRect(rectF, radius, radius, dir)