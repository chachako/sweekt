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
 * 滑入动画
 * 滑入的定义是将视图从某个位置移动回原位置
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
import com.meowbase.ui.core.graphics.geometry.Offset
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toPx
import kotlin.time.Duration
import com.meowbase.ui.animation.core.Repeat
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.util.applyConfigurations


/**
 * 定义一个滑入动画
 *
 * @param initialOffset 这是一个会保证视图绘制完后执行的 lambda
 * 它接收一个动画开始的 xy 值，参考 [slideInX] [slideInY]
 * 默认将从 [View.getTranslationY] [View.getTranslationX] 滑动回 0
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideIn(
  initialOffset: (view: View) -> Offset = { Offset(it.translationX, it.translationY) },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val offset = initialOffset(view)
    playTogether(
      ObjectAnimator.ofFloat(view, View.TRANSLATION_X, offset.x, 0f),
      ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, offset.y, 0f),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个滑入动画
 *
 * @param initialOffset 动画开始的 xy 值
 * 参考 [slideInX] [slideInY]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideIn(
  initialOffset: Offset,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    playTogether(
      ObjectAnimator.ofFloat(view, View.TRANSLATION_X, initialOffset.x, 0f),
      ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, initialOffset.y, 0f),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个横向滑入动画
 *
 * ```
 * // 让全屏视图从屏幕左侧外向右滑回屏幕
 * slideInX(initialOffset = { -it.width })
 * ```
 *
 * @param initialOffset 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画开始值
 * 如果返回的值是负数，那么将会从左向右开始偏移到 0, 反之则从右到左
 * 默认以 [View.getTranslationX] 作为起始偏移值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideInX(
  initialOffset: (view: View) -> Number = { it.translationX },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.TRANSLATION_X, initialOffset(view).float, 0f)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个横向滑入动画
 *
 * ```
 * // 让视图从往右 24 (dp) 的位置滑动回左边复位
 * slideInX(24.dp)
 * ```
 *
 * @param initialOffset 动画开始值
 * 如果值是负数，那么将会从左向右开始偏移到 0, 反之则从右到左
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideInX(
  initialOffset: SizeUnit,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.TRANSLATION_X, initialOffset.toPx(), 0f)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个纵向滑入动画
 *
 * ```
 * // 让全屏视图从屏幕上侧外向下滑回屏幕
 * slideInY(initialOffset = { it.height })
 * ```
 *
 * @param initialOffset 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画开始值
 * 如果返回的值是负数，那么将会从下往上开始偏移到 0, 反之则从上到下
 * 默认以 [View.getTranslationY] 作为起始偏移值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideInY(
  initialOffset: (view: View) -> Number = { it.translationY },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.TRANSLATION_Y, initialOffset(view).float, 0f)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个纵向滑入动画
 *
 * ```
 * // 让视图从往上 24 (dp) 的位置滑动回下边复位
 * slideInY(initialOffset = -24.dp)
 * ```
 *
 * @param initialOffset 动画开始值
 * 如果值是负数，那么将会从下往上开始偏移到 0, 反之则从上到下
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideInY(
  initialOffset: SizeUnit,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.TRANSLATION_Y, initialOffset.toPx(), 0f)
    .applyConfigurations(delay, duration, repeat, easing)
}