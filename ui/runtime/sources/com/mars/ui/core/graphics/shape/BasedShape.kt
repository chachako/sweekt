package com.mars.ui.core.graphics.shape

import android.graphics.*
import com.mars.ui.core.graphics.Outline
import com.mars.ui.core.graphics.geometry.minSize
import com.mars.ui.theme.Shapes
import com.mars.ui.theme.Shapes.Companion.resolveShape
import kotlin.math.min

/*
 * author: 凛
 * date: 2020/8/9 12:51 PM
 * github: https://github.com/oh-Rin
 * description: 形状
 */
interface Shape {
  /** 用于主题系统分辨此形状是否是主题中的形状，并决定是否可更新 */
  var id: Int

  /** 创建一个形状副本并传入给定的 Id 值 */
  fun new(id: Int): Shape

  /**
   * 创建形状轮廓
   * @param bounds 形状边界
   * @return 一个轮廓实例 [Outline]，可用于视图裁剪等行为
   */
  fun getOutline(bounds: RectF): Outline
}

/*
 * author: 凛
 * date: 2020/8/8 2:41 PM
 * github: https://github.com/oh-Rin
 * description: 定义基于角的形状
 */
abstract class CornerBasedShape(
  private val topLeft: CornerSize,
  private val topRight: CornerSize,
  private val bottomRight: CornerSize,
  private val bottomLeft: CornerSize,
) : Shape {
  /** [Shapes.resolveShape] */
  override var id: Int = -1

  /** 创建一个副本并传入给定的 Id 值 */
  override fun new(id: Int) = copy().also { it.id = id }

  final override fun getOutline(bounds: RectF): Outline {
    val minSize = bounds.minSize
    val topLeft = min(topLeft.toPx(bounds), minSize)
    val topRight = min(topRight.toPx(bounds), minSize)
    val bottomRight = min(bottomRight.toPx(bounds), minSize - topRight)
    val bottomLeft = min(bottomLeft.toPx(bounds), minSize - topLeft)
    require(topLeft >= 0.0f && topRight >= 0.0f && bottomRight >= 0.0f && bottomLeft >= 0.0f) {
      "Corner size in Px can't be negative(topLeft = $topLeft, topRight = $topRight, " +
        "bottomRight = $bottomRight, bottomLeft = $bottomLeft)!"
    }
    return getOutline(bounds, topLeft, topRight, bottomRight, bottomLeft)
  }

  /**
   * 创建基于角的形状的轮廓
   *
   * @param bounds 形状边界
   * @param topLeft 解析后的左上角大小
   * @param topRight 解析后的右上角大小
   * @param bottomRight 解析后的右下角大小
   * @param bottomLeft 解析后的左下角大小
   * @return 一个轮廓实例 [Outline]，可用于视图裁剪等行为
   */
  abstract fun getOutline(
    bounds: RectF,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float
  ): Outline

  /** 创建一个 [CornerBasedShape] 副本以选择覆盖一些角的大小 */
  abstract fun copy(
    topLeft: CornerSize = this.topLeft,
    topRight: CornerSize = this.topRight,
    bottomRight: CornerSize = this.bottomRight,
    bottomLeft: CornerSize = this.bottomLeft
  ): CornerBasedShape

  /** 创建一个 [CornerBasedShape] 副本以所有角的大小 */
  fun copy(all: CornerSize): CornerBasedShape = copy(all, all, all, all)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (this.javaClass != other?.javaClass) return false

    other as CornerBasedShape

    if (topLeft != other.topLeft) return false
    if (topRight != other.topRight) return false
    if (bottomRight != other.bottomRight) return false
    if (bottomLeft != other.bottomLeft) return false

    return true
  }

  override fun hashCode(): Int {
    var result = topLeft.hashCode()
    result = 31 * result + topRight.hashCode()
    result = 31 * result + bottomRight.hashCode()
    result = 31 * result + bottomLeft.hashCode()
    return result
  }

  override fun toString(): String =
    "${javaClass.simpleName}(topLeft=$topLeft, topRight=$topRight, bottomRight=$bottomRight, bottomLeft=$bottomLeft, id=$id)"
}