@file:Suppress("FunctionName")

package com.mars.ui.core.graphics.shape

import android.graphics.RectF
import com.mars.ui.core.graphics.Outline
import com.mars.ui.core.graphics.createOutline

class RectangleCornerShape : RoundedCornerShape() {
  override fun getOutline(
    bounds: RectF,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float
  ): Outline = createOutline { setRect(bounds) }
}

/**
 * 四边为直角的形状
 * @see RoundedCornerShape.getOutline
 */
val RectangleShape by lazy { RectangleCornerShape() }