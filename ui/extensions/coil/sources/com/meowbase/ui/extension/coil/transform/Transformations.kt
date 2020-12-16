package com.meowbase.ui.extension.coil.transform

import coil.transform.Transformation
import com.meowbase.ui.core.graphics.shape.RoundedCornerShape
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.core.graphics.shape.SmoothCornerShape

/** 将 [Shape] 转换为 [Transformation] */
fun Shape.toTransformation(): Transformation = ShapeTransformation(this)