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

package com.meowbase.toolkit.other

import com.meowbase.toolkit.appClassLoader

/** 更安全的解析并返回 [Class] */
fun resolveClass(
  className: String,
  initializeClass: Boolean = false,
  classLoader: ClassLoader = appClassLoader
): Class<*> = when (className) {
  "java.lang.Void" -> Void.TYPE
  else -> try {
    Class.forName(className, initializeClass, classLoader)
  } catch (_: ClassNotFoundException) {
    // 试试另一种方式提高容错率，因为某些情况下可能会导致 inner class 无法找到
    try {
      val resolved = className.substringBeforeLast(".") + "\$" + className.substringAfterLast(".")
      Class.forName(resolved, initializeClass, classLoader)
    } catch (e: ClassNotFoundException) {
      e.printStackTrace()
      error(e.message ?: "Class $className 查找失败。")
    }
  }
}