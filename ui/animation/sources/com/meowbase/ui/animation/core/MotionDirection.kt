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

/**
 * 定义动画的运动方向
 *
 * @author 凛
 * @github https://github.com/RinOrz
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