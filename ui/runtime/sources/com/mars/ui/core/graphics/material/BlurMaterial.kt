package com.mars.ui.core.graphics.material

import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.theme.Materials
import com.mars.ui.theme.Materials.Companion.resolveMaterial

/*
 * author: 凛
 * date: 2020/8/16 8:49 PM
 * github: https://github.com/oh-Rin
 * description: 高斯模糊材质
 */
data class BlurMaterial(
  /** 模糊半径 */
  internal val radius: Number? = null,

  /** 覆盖层颜色, 默认不叠加任何颜色 */
  internal val overlayColor: Color = Color.Unset,
) : Material {
  /** [Materials.resolveMaterial] */
  override var id: Int = -1

  /** 将当前 [BlurMaterial] 与 [other] 进行合并后得到一个新的模糊材质 */
  override fun merge(other: Material): BlurMaterial {
    if (other !is BlurMaterial) return this
    return BlurMaterial(
      radius = other.radius ?: this.radius,
      overlayColor = other.overlayColor.useOrElse { this.overlayColor },
    )
  }

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }
}