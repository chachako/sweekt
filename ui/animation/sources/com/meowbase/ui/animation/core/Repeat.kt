package com.meowbase.ui.animation.core

import com.meowbase.ui.animation.core.leanback.ValueAnimator


/**
 * 重复播放动画
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/8 - 14:46
 * @property count 重复次数，值为 -1 时则代表无限重复播放动画
 * @property mode 重复模式，代表每一次播放结束后，再次播放时的模式 [Mode]
 */
data class Repeat(
  val count: Int = -1,
  val mode: Mode = Mode.Restart
) {
  enum class Mode(val flag: Int) {
    Restart(ValueAnimator.RESTART),
    Reverse(ValueAnimator.REVERSE),
  }
}