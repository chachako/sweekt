@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.foundation.modifies

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import com.google.android.material.shape.MaterialShapeDrawable
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
  override fun realize(myself: View, parent: ViewGroup?) {
    val color = when (val bg = myself.background) {
      is MaterialShapeDrawable -> bg.fillColor
      is ColorDrawable -> ColorStateList.valueOf(bg.color)
      else -> ColorStateList.valueOf(0x00000000)
    }
    myself.clipToOutline = true
    myself.background = MaterialShapeDrawable(
      shape.toModelBuilder().build()
    ).apply { fillColor = color }
  }
}