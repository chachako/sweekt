@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.mars.ui.theme

import com.mars.ui.UiKit.Companion.currentContext
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.material.Material
import com.mars.ui.core.graphics.shape.RoundedCornerShape
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.core.graphics.shape.ZeroCornerSize
import com.mars.ui.core.unit.dp
import com.mars.ui.currentUiKit

/*
 * author: 凛
 * date: 2020/8/8 3:31 AM
 * github: https://github.com/oh-Rin
 * description: 定义一些通用的角形状
 * specification: https://material.io/design/shape/applying-shape-to-ui.html
 */
class Shapes(
  /** 小形状定义 */
  small: Shape = RoundedCornerShape(4.dp),
  /** 一般形状定义 */
  medium: Shape = RoundedCornerShape(4.dp),
  /** 用于大组件上的大形状定义*/
  large: Shape = RoundedCornerShape(ZeroCornerSize),
) {
  /**
   * 需要拷贝一份新的形状副本并修改 [Color.id]
   * 以主题系统分辨其他地方的某个控件使用的形状是否为这里的
   */

  val small: Shape = small.new(0)
  val medium: Shape = medium.new(1)
  val large: Shape = large.new(2)

  /**
   * 创建一份形状库的副本，以覆盖一些值
   */
  fun copy(
    small: Shape = this.small,
    medium: Shape = this.medium,
    large: Shape = this.large,
  ): Shapes = Shapes(small, medium, large)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Shapes

    if (small != other.small) return false
    if (medium != other.medium) return false
    if (large != other.large) return false

    return true
  }

  override fun hashCode(): Int {
    var result = small.hashCode()
    result = 31 * result + medium.hashCode()
    result = 31 * result + large.hashCode()
    return result
  }

  companion object {
    /**
     * 当应用形状时都会将其备份起来
     * 后续主题更新时，在更新回调中先判断形状备份是否存在
     * 如果存在，根据形状备份的 [Material.id] 判断形状是否为主题排版库中的形状
     * @return 最后返回主题更新后的排版库的实际形状
     */
    internal fun Shape.resolveShape(): Shape = when (id) {
      /** 重新获取一遍即可达到更新效果，因为 [currentShapes] 值其实已经变化了 */
      0 -> currentShapes.small
      1 -> currentShapes.medium
      2 -> currentShapes.large
      else -> this // 并非为主题库中的形状，不需要更新
    }
  }
}

/** 当前主题范围中的形状库 */
@PublishedApi internal val currentShapes get() = currentContext.currentUiKit.shapes