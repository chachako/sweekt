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

package com.meowbase.ui.animation.definition

/**
 * 缩放动画
 * 缩放的定义是将视图缩放到指定比例
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/6 - 22:50
 */
import com.meowbase.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import com.meowbase.ui.animation.core.AnimationDefinition
import com.meowbase.ui.animation.Motion
import com.meowbase.ui.animation.core.Repeat
import kotlin.time.Duration
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.util.applyConfigurations


/**
 * 定义一个缩放动画
 *
 * @param fromX 动画 x 轴缩放开始值（如果此值没有定义但定义了 [toX] 则将使用 [View.getScaleX]）
 * @param toX 动画 x 轴缩放结束值（如果此值没有定义则直接忽略 [fromX]）
 *
 * @param fromY 动画 y 轴缩放开始值（如果此值没有定义但定义了 [toY] 则将使用 [View.getScaleY]）
 * @param toY 动画 y 轴缩放结束值（如果此值没有定义则直接忽略 [fromY]）
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 *
 * @see zoomIn
 * @see zoomOut
 */
fun Motion.scale(
  fromX: Float? = null,
  toX: Float? = null,
  fromY: Float? = null,
  toY: Float? = null,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  @SuppressLint("Recycle")
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val animators = mutableListOf<Animator>()
    // 如果定义了目标 X 轴比例
    if (toX != null) {
      animators += ObjectAnimator.ofFloat(
        view,
        View.SCALE_X,
        fromX ?: view.scaleX,
        toX
      )
    }
    // 如果定义了目标 Y 轴比例
    if (toY != null) {
      animators += ObjectAnimator.ofFloat(
        view,
        View.SCALE_Y,
        fromY ?: view.scaleY,
        toY
      )
    }
    playTogether(*animators.toTypedArray())
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个缩放动画
 *
 * @param fromXy 动画同时开始缩放 xy 轴的值
 * 如果此值没有定义但定义了 [toXy] 则将使用 [View.getScaleX]
 * @param toXy  动画同时结束缩放 xy 轴的值（如果此值没有定义则直接忽略 [fromXy]）
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 *
 * @see zoomIn
 * @see zoomOut
 */
fun Motion.scale(
  fromXy: Float? = null,
  toXy: Float? = null,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromX = fromXy,
  toX = toXy,
  fromY = fromXy,
  toY = toXy,
  delay = delay,
  duration = duration,
  repeat = repeat,
  easing = easing
)

/**
 * 定义一个缩放动画
 *
 * ```
 * scale(
 *   // 从正常大小开始，然后缩小直到消失，最后再放大回视图大小的一半
 *   transitionX = floatArrayOf(1f, 0f, 0.5f),
 * )
 * ```
 *
 * @param transitionX 多个 x 轴过渡值，将会为每个不同时间段自动估算缩放值
 * @param transitionY 多个 y 轴过渡值，将会为每个不同时间段自动估算缩放值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 *
 * @see zoomIn
 * @see zoomOut
 */
fun Motion.scale(
  transitionX: FloatArray? = null,
  transitionY: FloatArray? = null,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  @SuppressLint("Recycle")
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val animators = mutableListOf<Animator>()
    if (transitionX != null) {
      animators.add(
        ObjectAnimator.ofFloat(
          view, View.SCALE_X,
          *transitionX
        )
      )
    }
    if (transitionY != null) {
      animators.add(
        ObjectAnimator.ofFloat(
          view, View.SCALE_Y,
          *transitionY
        )
      )
    }
    playTogether(*animators.toTypedArray())
    applyConfigurations(delay, duration, repeat, easing)
  }
}
