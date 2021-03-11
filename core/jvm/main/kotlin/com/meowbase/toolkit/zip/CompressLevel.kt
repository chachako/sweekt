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

package com.meowbase.toolkit.zip

/*
 * author: 凛
 * date: 2020/9/10 下午9:59
 * github: https://github.com/RinOrz
 * description: 定义压缩等级，等级越高且压缩越强但速度也会随之降低
 */
data class CompressLevel(val level: Int) {
  companion object {
    /** 正常压缩 */
    val Default = CompressLevel(-1)

    /** 不压缩 */
    val None = CompressLevel(0)

    /** 极速压缩 */
    val Fastest = CompressLevel(1)

    /** 最佳压缩 */
    val Best = CompressLevel(9)
  }
}