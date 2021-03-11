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
 * 旋转动画
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/7 - 10:23
 */
import com.meowbase.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import com.meowbase.ui.animation.core.AnimationDefinition
import com.meowbase.ui.animation.Motion
import kotlin.time.Duration
import com.meowbase.ui.animation.core.Repeat
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.util.applyConfigurations


/**
 * 定义一个顺时针旋转动画 - 2D
 *
 * @param from 动画开始时的旋转值，如果为 null 则以 [View.getRotation] 来作为开始值
 * @param to 动画结束时的旋转值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.rotate(
  from: Float? = null,
  to: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ROTATION, from ?: view.rotation, to)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个顺时针旋转动画 - 2D
 *
 * @param transition 多个旋转过渡值，将会为每个不同时间段自动估算旋转值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.rotate(
  transition: FloatArray,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ROTATION, *transition)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个绕着水平中心旋转的动画 - 3D
 *
 * @param from 动画开始时的旋转值，如果为 null 则以 [View.getRotationX] 来作为开始值
 * @param to 动画结束时的旋转值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.rotateX(
  from: Float? = null,
  to: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ROTATION_X, from ?: view.rotationX, to)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个绕着水平中心旋转的动画 - 3D
 *
 * @param transition 多个 x 轴旋转过渡值，将会为每个不同时间段自动估算旋转值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.rotateX(
  transition: FloatArray,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ROTATION_X, *transition)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个绕着垂直中心旋转的动画
 *
 * @param fromY 动画开始时的旋转值，如果为 null 则以 [View.getRotationY] 来作为开始值
 * @param to 动画结束时的旋转值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.rotateY(
  fromY: Float? = null,
  to: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ROTATION_Y, fromY ?: view.rotationY, to)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个绕着垂直中心旋转的动画 - 3D
 *
 * @param transition 多个 y 轴旋转过渡值，将会为每个不同时间段自动估算旋转值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.rotateY(
  transition: FloatArray,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ROTATION_Y, *transition)
    .applyConfigurations(delay, duration, repeat, easing)
}