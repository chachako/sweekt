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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.toolkit.data

import com.meowbase.toolkit.float
import com.meowbase.toolkit.packFloats
import com.meowbase.toolkit.unpackFloat1
import com.meowbase.toolkit.unpackFloat2

/**
 * 拥有两个 [Float] 数值的类
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/8 - 15:30
 * @see kotlin.Pair inlined it
 */
inline class Pair(val packedValue: Long) {
  val first: Float
    get() = unpackFloat1(packedValue)
  val second: Float
    get() = unpackFloat2(packedValue)

  operator fun component1(): Float = first
  operator fun component2(): Float = second

  fun copy(first: Float = this.first, second: Float = this.second) = Pair(first, second)

  operator fun unaryMinus(): Pair = Pair(-first, -second)

  operator fun minus(other: Pair): Pair = Pair(first - other.first, second - other.second)

  operator fun plus(other: Pair): Pair = Pair(first + other.first, second + other.second)

  operator fun times(operand: Float): Pair = Pair(first * operand, second * operand)

  operator fun div(operand: Float): Pair = Pair(first / operand, second / operand)

  operator fun rem(operand: Float) = Pair(first % operand, second % operand)

  override fun toString(): String = "Pair($first, $second)"
}

@Suppress("NOTHING_TO_INLINE")
inline fun Pair(first: Number, second: Number) = Pair(packFloats(first.float, second.float))