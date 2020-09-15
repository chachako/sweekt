@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.mars.ui.theme

import androidx.annotation.RestrictTo
import com.mars.ui.UiKit.Companion.currentContext
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.material.BlurMaterial
import com.mars.ui.core.graphics.material.Material
import com.mars.ui.currentUiKit

/*
 * author: 凛
 * date: 2020/8/8 3:31 AM
 * github: https://github.com/oh-Rin
 * description: 定义一些通用的材质
 * specification: https://material.io/ or https://developer.apple.com/design/human-interface-guidelines/ios/visual-design/materials/
 */
class Materials(
  /** 较厚的材质 */
  thick: Material = BlurMaterial(
    radius = 28,
    overlayColor = Color.White.copy(alpha = 0.74f)
  ),
  /** 正常的材质 */
  regular: Material = BlurMaterial(
    radius = 20,
    overlayColor = Color.White.copy()
  ),
  /** 较薄的材质 */
  thin: Material = BlurMaterial(
    radius = 14,
    overlayColor = Color.White.copy(alpha = 0.40f)
  ),
  /** 超薄的材质 */
  ultraThin: Material = BlurMaterial(
    radius = 8,
    overlayColor = Color.White.copy(alpha = 0.20f)
  ),
) {
  /**
   * 需要拷贝一份新的材质副本并修改 [Color.id]
   * 以主题系统分辨其他地方的某个控件使用的材质是否为这里的
   */

  val thick: BlurMaterial = thick.new(0) as BlurMaterial
  val regular: BlurMaterial = regular.new(1) as BlurMaterial
  val thin: BlurMaterial = thin.new(2) as BlurMaterial
  val ultraThin: BlurMaterial = ultraThin.new(3) as BlurMaterial

  /**
   * 创建一份材质库的副本，以覆盖一些值
   */
  fun copy(
    thick: Material = this.thick,
    regular: Material = this.regular,
    thin: Material = this.thin,
    ultraThin: Material = this.ultraThin,
  ): Materials = Materials(thick, regular, thin, ultraThin)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Materials

    if (thick != other.thick) return false
    if (regular != other.regular) return false
    if (thin != other.thin) return false
    if (ultraThin != other.ultraThin) return false

    return true
  }

  override fun hashCode(): Int {
    var result = thick.hashCode()
    result = 31 * result + regular.hashCode()
    result = 31 * result + thin.hashCode()
    result = 31 * result + ultraThin.hashCode()
    return result
  }

  override fun toString(): String {
    return "Materials(thick=$thick, regular=$regular, thin=$thin, ultraThin=$ultraThin)"
  }

  companion object {
    /**
     * 当应用材质时都会将其备份起来
     * 后续主题更新时，在更新回调中先判断材质备份是否存在
     * 如果存在，根据材质备份的 [Material.id] 判断材质是否为主题排版库中的材质
     * @return 最后返回主题更新后的排版库的实际材质
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun Material.resolveMaterial(): Material = when (id) {
      /** 重新获取一遍即可达到更新效果，因为 [currentMaterials] 值其实已经变化了 */
      0 -> currentMaterials.thick
      1 -> currentMaterials.regular
      2 -> currentMaterials.thin
      3 -> currentMaterials.ultraThin
      else -> this // 并非为主题库中的材质，不需要更新
    }
  }
}

/** 当前主题范围中的材质库 */
@PublishedApi internal val currentMaterials get() = currentContext.currentUiKit.materials