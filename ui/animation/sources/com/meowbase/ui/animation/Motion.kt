@file:OptIn(ExperimentalTime::class)

package com.meowbase.ui.animation

import android.animation.TimeInterpolator
import android.view.View
import androidx.core.animation.doOnEnd
import com.meowbase.toolkit.view.arranged
import com.meowbase.ui.animation.core.AnimationDefinition
import com.meowbase.ui.animation.core.AnimatorWrapper
import com.meowbase.ui.animation.core.Repeat
import com.meowbase.ui.animation.core.leanback.Animator
import com.meowbase.ui.animation.core.leanback.AnimatorSet
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * 用于定义预期的运动行为
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/5 - 17:59
 */
class Motion() : AnimatorWrapper<AnimatorSet>(AnimatorSet()) {
  private val animators: MutableList<Animator> = mutableListOf()

  @PublishedApi internal var sequentially: Boolean = false
  @PublishedApi internal var repeat: Repeat? = null
  @PublishedApi internal var delay: Duration? = null
    set(value) {
      field = value
      value?.toLongMilliseconds()?.apply(::setStartDelay)
    }
  @PublishedApi internal var duration: Duration? = null
    set(value) {
      field = value
      value?.toLongMilliseconds()?.apply(::setDuration)
    }
  @PublishedApi internal var easing: TimeInterpolator = Easing.Linear
    set(value) {
      field = value
      interpolator = value
    }

  constructor(
    duration: Duration? = null,
    delay: Duration? = null,
    repeat: Repeat? = null,
    sequentially: Boolean = false,
    easing: TimeInterpolator = Easing.Linear,
    onCompletion: (animator: Animator) -> Unit = {},
    define: Motion.() -> Unit
  ) : this() {
    this.delay = delay
    this.repeat = repeat
    this.duration = duration
    this.sequentially = sequentially
    this.interpolator = easing
    define()
    doOnEnd { onCompletion(this) }
  }

  /**
   * 为 [View] 添加新的动画
   * @param animDefine 定义需要播放的动画
   */
  operator fun View?.plusAssign(animDefine: AnimationDefinition) {
    if (this == null) return
    fun addAnimator() = animDefine.getAnimator(this).also { animators += it }
    if (animDefine.needArrange) {
      arranged { addAnimator() }
    } else {
      addAnimator()
    }
  }

  override fun start() {
    if (sequentially) {
      base.playSequentially(animators)
    } else {
      base.playTogether(animators)
    }
    super.start()
  }
}

fun startMotion(
  duration: Duration? = null,
  delay: Duration? = null,
  repeat: Repeat? = null,
  sequentially: Boolean = false,
  easing: TimeInterpolator = Easing.Linear,
  onCompletion: (animator: Animator) -> Unit = {},
  define: Motion.() -> Unit
): Motion = Motion(duration, delay, repeat, sequentially, easing, onCompletion, define)
  .apply { start() }