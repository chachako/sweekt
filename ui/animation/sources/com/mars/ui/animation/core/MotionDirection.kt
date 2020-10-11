package com.mars.ui.animation.core

/**
 * 定义动画的运动方向
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/10/9 - 17:33
 */
enum class MotionDirection {
  /** 从开头到结尾 */
  StartToEnd,

  /** 从结尾到开头 */
  EndToStart,

  /** 从上到下 */
  TopToDown,

  /** 从下到上 */
  BottomToUp,
}

/** 反转运动方向 */
fun MotionDirection.reverse() = when (this) {
  MotionDirection.StartToEnd -> MotionDirection.EndToStart
  MotionDirection.EndToStart -> MotionDirection.StartToEnd
  MotionDirection.TopToDown -> MotionDirection.BottomToUp
  MotionDirection.BottomToUp -> MotionDirection.TopToDown
}