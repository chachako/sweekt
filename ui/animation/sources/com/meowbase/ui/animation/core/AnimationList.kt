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

package com.meowbase.ui.animation.core

import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.core.leanback.*
import com.meowbase.ui.animation.Easing
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.milliseconds

/**
 * 持有多个动画器的动画列表
 * 并附有一些可同时设置所有动画的通用参数
 * 如果没有定义这些通用参数，那么子动画将使用它们自己的值
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/8 - 16:53
 */
internal class AnimationList : ArrayList<AnimatorSet>() {
  /**
   * 决定了当前动画列表中每个动画的播放前的延迟时间
   *
   * @see ValueAnimator.getDuration
   */
  var delay: Duration? = null

  /**
   * 决定了当前动画列表中所有动画的持续时间
   *
   * @see ValueAnimator.getDuration
   */
  var duration: Duration? = null

  /**
   * 决定动画是否需要重复播放
   * 每次重复时播放的模式
   *
   * @see ValueAnimator.getRepeatCount
   * @see ValueAnimator.getRepeatMode
   */
  var repeat: Repeat? = null

  /**
   * 动画缓和效果（动画随时间变化的速率）
   *
   * @see ValueAnimator.getInterpolator
   */
  var easing: TimeInterpolator = Easing.Linear

  /**
   * 决定了动画列表是否按顺序来播放
   *
   * @see AnimatorSet.playTogether
   * @see AnimatorSet.playSequentially
   */
  var sequentially: Boolean = false

  var progress: Float = 0f

  /**
   * 返回动画列表总时长
   * @see sequentially 当为有序播放时将获取所有子动画时间总和
   * 否则将返回子动画中最大的持续时间
   */
  val totalTime: Duration
    get() = if (sequentially) {
      sumOf { it.totalDuration }
    } else {
      maxOf { it.totalDuration }
    }.milliseconds
}