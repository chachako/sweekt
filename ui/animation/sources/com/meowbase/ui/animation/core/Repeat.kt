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