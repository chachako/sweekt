@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.ui.skeleton.animation

import android.view.View
import com.meowbase.ui.animation.core.MotionDirection
import com.meowbase.ui.animation.core.reverse
import com.meowbase.ui.skeleton.Skeleton


/**
 * 代表 [Skeleton] 的默认过渡动画
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/4 - 21:43
 */
sealed class SkeletonTransition(
  val id: Int,
  val direction: MotionDirection = MotionDirection.StartToEnd,
  val sharedElement: Boolean = false,
) {
  object Push : SkeletonTransition(0)
  object Slide : SkeletonTransition(1)
  object ZoomSlide : SkeletonTransition(2)
  object Cover : SkeletonTransition(3)
  object Page : SkeletonTransition(4)
  object Fade : SkeletonTransition(5)
  object Zoom : SkeletonTransition(6)
  object SharedElement : SkeletonTransition(7)
  object None : SkeletonTransition(8)
  private class New(id: Int, direction: MotionDirection, sharedElement: Boolean) :
    SkeletonTransition(id, direction, sharedElement)

  /** 添加关于运动方向的定义 */
  operator fun plus(direction: MotionDirection) =
    copy(direction = direction)

  /**
   * 为当前过渡添加共享元素动画
   * @see View.getTransitionName 转换所有设置了此属性的 View
   */
  operator fun plus(sharedElement: SharedElement) =
    copy(sharedElement = sharedElement.sharedElement)

  /**
   * 拷贝部分元素到新的实例中
   * @return 新的拷贝实例
   */
  fun copy(
    direction: MotionDirection = this.direction,
    sharedElement: Boolean = this.sharedElement,
  ): SkeletonTransition = New(id, direction, sharedElement)

  /**
   * 反转过渡动画的方向
   */
  fun reverse() = copy(direction = direction.reverse())

  override fun equals(other: Any?): Boolean = this.id == (other as? SkeletonTransition)?.id

  override fun hashCode(): Int = id

  override fun toString(): String {
    return "SkeletonTransition(id=$id, direction=${direction.name}, sharedElement=$sharedElement)"
  }

}