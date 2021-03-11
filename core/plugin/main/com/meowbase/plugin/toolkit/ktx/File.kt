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

package com.meowbase.plugin.toolkit.ktx

import com.meowbase.toolkit.subtree
import java.io.File

val Set<File>.allFiles
  get() = mutableListOf<File>().also { list ->
    forEach { list.addAll(it.subtree()) }
  }

/** 遍历所有文件 */
inline fun Set<File>.forEachRecursive(
  filter: File.() -> Boolean = { true },
  action: (File) -> Unit
) {
  val files = mutableListOf<File>()
  forEach { files.addAll(it.subtree()) }
  files.forEach {
    if (it.filter() && filter.invoke(it)) action(it)
  }
}