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

package com.meowbase.ui.core.text

/*
 * author: 凛
 * date: 2020/8/8 7:11 PM
 * github: https://github.com/RinOrz
 * description: 文本滚动字幕效果
 */
data class TextMarquee(
  /** 开启轮播效果 */
  val enabled: Boolean = false,
  /** 播放次数，-1 为无限 */
  val repeat: Int = -1
)