package com.mars.ui.extension.coil.transform

import coil.transform.Transformation
import com.mars.ui.core.graphics.shape.RoundedCornerShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.shape.SmoothCornerShape

/** 将 [Shape] 转换为 [Transformation] */
fun Shape.toTransformation(): Transformation = ShapeTransformation(this)