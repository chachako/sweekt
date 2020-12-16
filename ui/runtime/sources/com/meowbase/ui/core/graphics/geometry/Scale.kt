package com.meowbase.ui.core.graphics.geometry

import com.meowbase.toolkit.float
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toPx
import com.meowbase.toolkit.packFloats

/**
 * 持有 xy 两个轴的 [Float] 值的缩放单位
 */
typealias Scale = Offset

/**
 * Convert [SizeUnit] to [Scale]
 */
fun SizeUnit.toScale() = Scale(this.toPx(), this.toPx())

/**
 * Convert [Number] to [Scale]
 */
fun Number.toScale() = Scale(this.float, this.float)

/**
 * Constructs an Scale from the given relative x and y Scales
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Scale(x: SizeUnit, y: SizeUnit) = Scale(packFloats(x.toPx(), y.toPx()))

/**
 * Constructs an Scale from the given relative x and y Scales
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Scale(x: Number, y: Number) = Scale(packFloats(x.float, y.float))