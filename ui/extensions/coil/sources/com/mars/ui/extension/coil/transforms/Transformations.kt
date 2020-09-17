package com.mars.ui.extension.coil.transforms

import coil.transform.Transformation
import com.mars.ui.core.graphics.shape.RoundedCornerShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.shape.SmoothCornerShape

/** 将 [Shape] 转换为 [Transformation] */
val Shape.asTransformation
  get(): Transformation = when (this) {
    is RoundedCornerShape -> RoundedCornersTransformation(
      topLeft,
      topRight,
      bottomLeft,
      bottomRight,
    )
    is SmoothCornerShape -> SmoothCornersTransformation(
      topLeft,
      topRight,
      bottomLeft,
      bottomRight,
    )
    else -> error("当前仅支持将 SmoothCornerShape/RoundedCornerShape 转换为 Coil 可用的 Transformation.")
  }
