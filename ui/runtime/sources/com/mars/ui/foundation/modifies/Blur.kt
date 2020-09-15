@file:Suppress("OverridingDeprecatedMember")

package com.mars.ui.foundation.modifies

import android.view.View
import android.view.ViewGroup
import com.mars.toolkit.float
import com.mars.ui.core.Modifier
import com.mars.ui.core.UpdatableModifier
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.material.BlurMaterial
import com.mars.ui.core.graphics.material.Material
import com.mars.ui.core.graphics.useOrNull
import com.mars.ui.theme.Colors.Companion.resolveColor
import com.mars.ui.theme.Materials.Companion.resolveMaterial
import com.mars.ui.theme.currentMaterials
import com.mars.ui.util.BlurHelper

/*
 * author: 凛
 * date: 2020/8/14 3:40 PM
 * github: https://github.com/oh-Rin
 * description: 类似于 iOS 的 UIBlurEffect 或 Flutter 的 ImageFilter.blur
 * reference: https://developer.apple.com/documentation/uikit/uiblureffect
 */
interface BlurEffect {
  /** 模糊效果的具体实现 [BlurHelper] */
  var blurHelper: BlurHelper?
}

/**
 * 将模糊效果应用于视图背后的内容
 * @param radius [BlurMaterial.radius]
 * @param overlayColor [BlurMaterial.overlayColor]
 * @see material
 */
fun Modifier.blurEffect(
  radius: Number? = null,
  overlayColor: Color = Color.Unset,
) = +BlurMaterialModifier(radius = radius, overlayColor = overlayColor)

/**
 * 将模糊材质应用于视图背后的内容
 * @param blurMaterial 模糊材质
 * @see blurEffect
 */
fun Modifier.material(blurMaterial: BlurMaterial) = +BlurMaterialModifier(blurMaterial)


/** 为视图提供模糊材质的调整器 */
private data class BlurMaterialModifier(
  val material: Material = currentMaterials.regular.resolveMaterial(),
  val radius: Number? = null,
  val overlayColor: Color = Color.Unset,
) : Modifier, UpdatableModifier {
  val resolvedMaterial = material.resolveMaterial().merge(
    BlurMaterial(radius, overlayColor)
  ) as? BlurMaterial ?: error("fun BlurEffect(material..) 参数必须是一个 BlurMaterial")

  override fun realize(myself: View, parent: ViewGroup?) {
    (myself as BlurEffect).blurHelper = BlurHelper(myself).also { helper ->
      resolvedMaterial.radius?.float?.apply { helper.radius = this }
      resolvedMaterial.overlayColor.useOrNull()?.apply { helper.overlayColor = argb }
      helper.attach()
    }
  }

  override fun update(myself: View, parent: ViewGroup?) {
    (myself as? BlurEffect)?.blurHelper?.overlayColor = resolvedMaterial.overlayColor.useOrNull()
      ?.resolveColor()
      ?.argb
  }
}