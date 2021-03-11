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
 * 淡出淡入动画
 * 淡出的定义是将视图渐隐，即过渡原透明度到给定的目标透明度
 * 淡入的定义是将视图渐显，即过渡给定的透明度到 1f
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/7 - 17:02
 */
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.FloatRange
import com.meowbase.ui.animation.core.AnimationDefinition
import com.meowbase.ui.animation.Motion
import kotlin.time.Duration
import com.meowbase.ui.animation.core.Repeat
import com.meowbase.ui.animation.util.applyConfigurations


/**
 * 定义一个淡出动画
 *
 * @param targetAlpha 动画目标透明度，视图将从 [View.getAlpha] 淡化到此目标值
 * 默认将淡化直到消失，既 0f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.fadeOut(
  @FloatRange(from = .0, to = 1.0) targetAlpha: Float = 0f,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ALPHA, view.alpha, targetAlpha)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个淡入动画
 *
 * @param initialAlpha 动画开始透明度，视图将从此值开始淡入直到显示，既 1f
 * 如果为 null 则使用 [View.getAlpha] 作为动画起始透明值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.fadeIn(
  @FloatRange(from = .0, to = 1.0) initialAlpha: Float? = null,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ALPHA, initialAlpha ?: view.alpha, 1f)
    .applyConfigurations(delay, duration, repeat, easing)
}
