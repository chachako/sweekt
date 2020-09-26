@file:Suppress("OverridingDeprecatedMember", "Deprecation", "RestrictedApi")

package com.mars.ui.foundation.modifies

import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.ShapeAppearancePathProvider
import com.mars.toolkit.view.arrange
import com.mars.toolkit.view.clipOutline
import com.mars.ui.core.Modifier
import com.mars.ui.core.graphics.shape.CircleShape
import com.mars.ui.core.graphics.shape.Shape

/** 将视图裁剪为指定形状 */
fun Modifier.clip(
  shape: Shape,
) = +ClipModifier(shape)

/**
 * 将视图裁剪为圆形
 * @see [CircleShape]
 */
fun Modifier.clipOval() = clip(CircleShape)

/** 根据参数裁剪 View 形状的具体实现 */
private data class ClipModifier(val shape: Shape) : Modifier {
  val shapeAppearanceModel = shape.toModelBuilder().build()
  val shapeAppearancePathProvider = ShapeAppearancePathProvider()
  override fun View.realize(parent: ViewGroup?) {
    arrange {
      clipOutline {
        val path = Path()
        val rect = Rect(left, top, right, bottom)
        val rectF = RectF().apply { set(rect) }
        shapeAppearancePathProvider.calculatePath(shapeAppearanceModel, 1f, rectF, path)

        if (shapeAppearanceModel.isRoundRect(rectF)) {
          val radius = shapeAppearanceModel.topLeftCornerSize.getCornerSize(rectF)
          it.setRoundRect(rect, radius)
          return@clipOutline
        }

        try {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            it.setPath(path)
          } else if (path.isConvex || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            it.setConvexPath(path)
          }
        } catch (ignored: IllegalArgumentException) {
          // The change to support concave paths was done late in the release cycle. People
          // using pre-releases of Q would experience a crash here.
        }
      }
    }
  }
}