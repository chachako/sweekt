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

package com.meowbase.ui.core

import android.widget.LinearLayout

/*
 * author: 凛
 * date: 2020/8/8 10:27 PM
 * github: https://github.com/RinOrz
 * description: 定义方向
 */
enum class Orientation {
  /** 水平 */
  Horizontal,

  /** 垂直 */
  Vertical,
}

/** [LinearLayout.HORIZONTAL] [LinearLayout.VERTICAL] */
val Orientation.native
  get() = when (this) {
    Orientation.Horizontal -> 0
    Orientation.Vertical -> 1
  }