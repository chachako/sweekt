package com.meowbase.ui.animation.core

import android.animation.TimeInterpolator
import com.meowbase.ui.animation.core.leanback.Animator

/**
 * 包装动画实例
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/10 - 15:05
 */
abstract class AnimatorWrapper<Base: Animator>() : Animator() {
  lateinit var base: Base

  constructor(base: Base): this() {
    this.base = base
  }

  override fun getStartDelay(): Long = base.startDelay

  override fun setStartDelay(startDelay: Long) {
    base.startDelay = startDelay
  }

  override fun setDuration(duration: Long): Animator = apply {
    base.duration = duration
  }

  override fun setInterpolator(value: TimeInterpolator?) {
    base.interpolator = value
  }

  override fun addListener(listener: android.animation.Animator.AnimatorListener) {
    base.addListener(listener)
  }

  override fun removeAllListeners() = base.removeAllListeners()

  override fun removeAllUpdateListeners() = base.removeAllUpdateListeners()

  override fun removeListener(listener: android.animation.Animator.AnimatorListener)
    = base.removeListener(listener)

  override fun removePauseListener(listener: AnimatorPauseListener) =
    base.removePauseListener(listener)

  override fun removeUpdateListener(listener: AnimatorUpdateListener) =
    base.removeUpdateListener(listener)

  override fun reverse() = base.reverse()

  override fun skipToEndValue(inReverse: Boolean) {
    base.skipToEndValue(inReverse)
  }

  override fun start() {
    base.start()
  }

  override fun pulseAnimationFrame(frameTime: Long): Boolean = base.pulseAnimationFrame(frameTime)

  override fun startWithoutPulsing(inReverse: Boolean) = base.startWithoutPulsing(inReverse)

  override fun isInitialized(): Boolean = base.isInitialized

  override fun animateBasedOnPlayTime(
    currentPlayTime: Long,
    lastPlayTime: Long,
    inReverse: Boolean
  ) = base.animateBasedOnPlayTime(currentPlayTime, lastPlayTime, inReverse)

  override fun canReverse(): Boolean = base.canReverse()

  override fun resume() = base.resume()

  override fun getDuration(): Long = base.duration

  override fun isRunning(): Boolean = base.isRunning

  override fun pause() = base.pause()

  override fun cancel() = base.cancel()

  override fun end() = base.end()

  override fun getInterpolator(): TimeInterpolator? = base.interpolator

  override fun getTotalDuration(): Long = base.totalDuration

  override fun setTarget(target: Any?) = base.setTarget(target)

  override fun getListeners() = base.listeners

  override fun setupEndValues() = base.setupEndValues()

  override fun setupStartValues() = base.setupStartValues()

  override fun addPauseListener(listener: AnimatorPauseListener) = base.addPauseListener(listener)

  override fun addUpdateListener(listener: AnimatorUpdateListener) = base.addUpdateListener(listener)

  override fun isPaused(): Boolean = base.isPaused

  override fun isStarted(): Boolean = base.isStarted
}