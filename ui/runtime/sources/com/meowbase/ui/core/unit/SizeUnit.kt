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

@file:Suppress("NOTHING_TO_INLINE")

package com.meowbase.ui.core.unit

import com.meowbase.ui.core.unit.SizeUnit.Companion.Unspecified
import kotlin.math.max
import kotlin.math.min

/*
 * author: 凛
 * date: 2020/8/11 2:46 PM
 * github: https://github.com/RinOrz
 * description: px || sp || dp
 */
interface SizeUnit {
  companion object {
    /**
     * Constant that means unspecified any size unit
     */
    val Unspecified = object : SizeUnit {}
  }
}

/**
 * 判断是否是一个未指定的单位
 */
inline val SizeUnit.isUnspecified
  get() = this == Unspecified
    || (this as? TextUnit)?.isInherit == true

/**
 * 判断是否是一个指定过的单位
 */
inline val SizeUnit.isSpecified get() = !isUnspecified

/**
 * 如果单位未指定则使用 [block] 提供的单位
 * 否则直接使用当前单位
 */
inline fun SizeUnit.useOrElse(block: () -> SizeUnit): SizeUnit =
  if (isUnspecified) block() else this

/**
 * 如果单位未指定则返回 null
 * 否则直接使用当前单位
 */
fun SizeUnit.useOrNull(): SizeUnit? = if (isUnspecified) null else this

inline fun min(a: SizeUnit, b: SizeUnit): Float = min(a.toPx(), b.toPx())
inline fun max(a: SizeUnit, b: SizeUnit): Float = max(a.toPx(), b.toPx())