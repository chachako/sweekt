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

@file:Suppress("unused")

package com.meowbase.ui.animation.definition

/**
 * 动画修饰符
 * 用于调整多个动画的基本参数
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/8 - 12:36
 */
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.core.leanback.*
import com.meowbase.ui.animation.core.AnimationModifier
import com.meowbase.ui.animation.Easing
import com.meowbase.ui.animation.Motion
import android.animation.Animator
import androidx.core.animation.addListener
import androidx.core.animation.addPauseListener
import kotlin.time.Duration


/**
 * 为所有已经定义的动画设置多个动画修饰符
 *
 * @param delay [Motion.delay]
 * @param duration [Motion.duration]
 * @param sequentially [Motion.playSequentially] & [Motion.playTogether]
 * @param easing [Motion.easing]
 */
fun Motion.modifier(
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  sequentially: Boolean = this.sequentially,
  easing: TimeInterpolator = this.easing,
): AnimationModifier = object : PlayMode(sequentially) {
  override fun attach(animator: Animator) {
    if (delay != null && delay != this@modifier.delay)
      animator.startDelay = delay.toLongMilliseconds()

    if (duration != null && duration != this@modifier.duration)
      animator.duration = duration.toLongMilliseconds()

    if (easing != this@modifier.easing)
      animator.interpolator = easing
  }
}

/**
 * 为所以已经定义的动画，即 [AnimatorSet] 设置播放监听器
 *
 * @param onStart 当动画开始后
 * @param onPause 当动画暂停后
 * @param onResume 当暂停的动画恢复播放后
 * @param onCancel 当动画退出后
 * @param onRepeat 当动画进行重复播放时
 * @param onEnd 当动画播放完成后
 */
inline fun Motion.listener(
  crossinline onStart: (animator: Animator) -> Unit = {},
  crossinline onPause: (animator: Animator) -> Unit = {},
  crossinline onResume: (animator: Animator) -> Unit = {},
  crossinline onCancel: (animator: Animator) -> Unit = {},
  crossinline onRepeat: (animator: Animator) -> Unit = {},
  crossinline onEnd: (animator: Animator) -> Unit = {},
): AnimationModifier = object : AnimationModifier {
  override fun attach(animator: Animator) {
    animator.addListener(
      onStart = onStart,
      onCancel = onCancel,
      onEnd = onEnd,
      onRepeat = onRepeat,
    )
    animator.addPauseListener(
      onPause = onPause,
      onResume = onResume
    )
  }
}

/**
 * 为所有已经定义的动画设置播放前延迟时长
 * @param time
 */
fun Motion.delay(time: Duration): AnimationModifier = object : AnimationModifier {
  override fun attach(animator: Animator) {
    animator.startDelay = time.toLongMilliseconds()
  }
}

/**
 * 为所有已经定义的动画设置持续播放时长
 * @param time
 */
fun Motion.duration(time: Duration): AnimationModifier = object : AnimationModifier {
  override fun attach(animator: Animator) {
    animator.duration = time.toLongMilliseconds()
  }
}

/**
 * 为所有已经定义的动画的设置插值器
 * @see Easing
 */
fun Motion.easing(interpolator: TimeInterpolator): AnimationModifier =
  object : AnimationModifier {
    override fun attach(animator: Animator) {
      animator.interpolator = interpolator
    }
  }

/**
 * 让所有已经定义的动画进行同时播放
 * @see AnimatorSet.playSequentially
 */
fun Motion.playSequentially(): AnimationModifier = PlayMode(sequentially = true)

/**
 * 让所有已经定义的动画进行同时播放
 * @see AnimatorSet.playTogether
 */
fun Motion.playTogether(): AnimationModifier = PlayMode(sequentially = false)


/** 设置有序的或者无序的播放动画 */
internal open class PlayMode(val sequentially: Boolean) : AnimationModifier {
  override fun attach(animator: Animator) {}
}