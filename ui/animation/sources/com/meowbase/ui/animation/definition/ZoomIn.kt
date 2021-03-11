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

@file:SuppressLint("Recycle")

package com.meowbase.ui.animation.definition


/**
 * 放大动画
 * 放大的定义是将视图从某个比例放大回 1f
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/6 - 17:53
 */
import com.meowbase.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import com.meowbase.toolkit.float
import com.meowbase.ui.animation.core.AnimationDefinition
import com.meowbase.ui.animation.Motion
import com.meowbase.ui.core.graphics.geometry.Scale
import kotlin.time.Duration
import com.meowbase.ui.animation.core.Repeat
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.util.applyConfigurations


/**
 * 定义一个放大动画
 *
 * @param initialScale 这是一个会保证视图绘制完后执行的 lambda
 * 它接收一个动画开始的 xy 比例值，参考 [zoomInX] [zoomInY]
 * 默认将从 [View.getScaleX] [View.getScaleY] 放大回 1f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomIn(
  initialScale: (view: View) -> Scale = { Scale(it.scaleX, it.scaleY) },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val scale = initialScale(view)
    playTogether(
      ObjectAnimator.ofFloat(view, View.SCALE_X, scale.x, 1f),
      ObjectAnimator.ofFloat(view, View.SCALE_Y, scale.y, 1f),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个放大动画
 *
 * @param initialScale 动画开始的 xy 值
 * 参考 [zoomInX] [zoomInY]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomIn(
  initialScale: Scale,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromX = initialScale.x,
  toX = 1f,
  fromY = initialScale.y,
  toY = 1f,
  delay = delay,
  duration = duration,
  easing = easing
)

/**
 * 定义一个放大动画
 *
 * @param initialScale 动画开始的 xy 值
 * 参考 [zoomInX] [zoomInY]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomIn(
  initialScale: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromX = initialScale,
  toX = 1f,
  fromY = initialScale,
  toY = 1f,
  delay = delay,
  repeat = repeat,
  duration = duration,
  easing = easing
)

/**
 * 定义一个横向放大动画
 *
 * @param initialScale 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画开始值
 * 将会从返回值开始放大 x 轴到 1f，默认以 [View.getScaleX] 作为起始偏移值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomInX(
  initialScale: (view: View) -> Float = { it.scaleX },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
//  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_X, initialScale(view).float, 1f)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个横向放大动画
 *
 * @param initialScale 动画开始值，将会从此值开始放大 x 轴到 1f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomInX(
  initialScale: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromX = initialScale,
  toX = 1f,
  delay = delay,
  repeat = repeat,
  duration = duration,
  easing = easing
)

/**
 * 定义一个纵向放大动画
 *
 * @param initialScale 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画开始值
 * 将会从返回值开始放大 y 轴到 1f，默认以 [View.getScaleY] 作为起始偏移值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomInY(
  initialScale: (view: View) -> Float = { it.scaleY },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_Y, initialScale(view).float, 1f)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个纵向放大动画
 *
 * @param initialScale 动画开始值，将会从此值开始放大 y 轴到 1f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomInY(
  initialScale: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromY = initialScale,
  toY = 1f,
  delay = delay,
  duration = duration,
  repeat = repeat,
  easing = easing
)