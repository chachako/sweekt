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

package com.meowbase.preference

import com.meowbase.preference.core.SharedPrefModel
import com.meowbase.preference.kotpref.impl.enum.enumValue

enum class Enum {
  ONE, TWO, THREE, DEFAULT
}

data class JsonModel(val name: String)

object SharedExample : SharedPrefModel("example") {
  var str by string("default")
  var enum by enumValue(Enum.DEFAULT)
}

fun main() {
  // example: set, put
  SharedExample.str = "changed"
  println(SharedExample.str)
}