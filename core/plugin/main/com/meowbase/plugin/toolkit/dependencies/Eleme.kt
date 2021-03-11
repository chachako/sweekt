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

@file:Suppress("PackageDirectoryMismatch", "SpellCheckingInspection", "unused", "ClassName")

/*
 * author: 凛
 * date: 2020/9/3 7:05 下午
 * github: https://github.com/RinOrz
 * description: 饿了么团队公开的依赖管理, see https://github.com/eleme
 */
object Eleme {
  val dna = Dna

  object Dna {
    const val annotations = "me.ele:dna-annotations:_"
    const val compiler = "me.ele:dna-compiler:_"
  }
}