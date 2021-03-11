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

@file:Suppress("FunctionName")

package com.meowbase.ui.core

import com.meowbase.ui.core.Padding.Companion.Unspecified
import com.meowbase.ui.core.unit.SizeUnit

/*
 * author: 凛
 * date: 2020/8/17 11:28 PM
 * github: https://github.com/RinOrz
 * description: 定义边距
 */
data class Padding(
  val start: SizeUnit = SizeUnit.Unspecified,
  val top: SizeUnit = SizeUnit.Unspecified,
  val end: SizeUnit = SizeUnit.Unspecified,
  val bottom: SizeUnit = SizeUnit.Unspecified,
) {
  companion object {
    val Unspecified = Padding()
  }
}

/**
 * 定义横纵的边距
 * @param horizontal 横向边距
 * @param vertical 纵向边距
 */
fun Padding(
  horizontal: SizeUnit = SizeUnit.Unspecified,
  vertical: SizeUnit = SizeUnit.Unspecified,
) = Padding(horizontal, vertical, horizontal, vertical)

/** 定义四个边的边距 */
fun Padding(all: SizeUnit) = Padding(all, all)

val Padding.isUnspecified get() = this == Unspecified

val Padding.isSpecified get() = !isUnspecified

fun Padding.useOrNull() = if (this == Unspecified) null else this

fun Padding.useOrElse(block: () -> Padding) = if (this == Unspecified) block() else this