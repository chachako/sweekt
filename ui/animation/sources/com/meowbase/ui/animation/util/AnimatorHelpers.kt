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

package com.meowbase.ui.animation.util

import android.animation.TimeInterpolator
import com.meowbase.toolkit.long
import com.meowbase.ui.animation.core.Repeat
import com.meowbase.ui.animation.core.leanback.Animator
import com.meowbase.ui.animation.core.leanback.AnimatorSet
import com.meowbase.ui.animation.core.leanback.ValueAnimator
import kotlin.time.Duration

/** 将参数中的配置应用到 [Animator] */
internal fun Animator.applyConfigurations(
  delay: Duration?,
  duration: Duration?,
  repeat: Repeat?,
  easing: TimeInterpolator,
): Animator = apply {
  when(this) {
    is ValueAnimator -> applyConfigurations(delay, duration, repeat, easing)
    is AnimatorSet -> childAnimations.forEach {
      it.applyConfigurations(delay, duration, repeat, easing)
    }
  }
}

/** 将参数中的配置应用到 [ValueAnimator] */
private fun ValueAnimator.applyConfigurations(
  delay: Duration?,
  duration: Duration?,
  repeat: Repeat?,
  easing: TimeInterpolator,
) {
  repeat?.apply {
    repeatCount = count
    repeatMode = mode.flag
  }
  delay?.inMilliseconds?.long?.let(::setStartDelay)
  duration?.inMilliseconds?.long?.let(::setDuration)
  easing.let(::setInterpolator)
}