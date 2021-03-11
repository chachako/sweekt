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

package com.meowbase.plugin.toolkit.booster.transform.asm

import org.objectweb.asm.tree.AnnotationNode

@Suppress("UNCHECKED_CAST")
fun <T> AnnotationNode.getValue(name: String = "value"): T? = values?.withIndex()?.iterator()?.let {
  while (it.hasNext()) {
    val i = it.next()
    if (i.index % 2 == 0 && i.value == name) {
      return@let it.next().value as T
    }
  }
  null
}
