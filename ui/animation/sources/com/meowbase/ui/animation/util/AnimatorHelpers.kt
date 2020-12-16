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