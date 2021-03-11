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

package com.meowbase.plugin.toolkit.data

/*
 * author: 凛
 * date: 2020/9/18 上午1:14
 * github: https://github.com/RinOrz
 * description: 一些内置的 Android 项目 tasks 名称
 */
enum class AndroidTasks(val taskName: String) {
  /** Android 项目编译时 */
  Assemble("assemble**"),
  /** Android 项目打包时 */
  Packaging("package**"),
  /** Dex 合并时 */
  DexMerge("mergeDex**"),
  /** 原生库合并时 */
  NativeLibsMerge("merge**NativeLibs")
}