/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

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