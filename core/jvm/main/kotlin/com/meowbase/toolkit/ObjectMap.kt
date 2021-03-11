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

@file:Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
@file:OptIn(ExperimentalContracts::class)

package com.meowbase.toolkit

/**
 * 以函数式编程形式简化复杂步骤来进行对象转换
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2021/01/07 - 19:05
 */
import kotlin.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 强制转换对象类型
 * @return 无论对象类型是否匹配都返回 [R]
 */
inline fun <reified R> Any?.cast(): R {
  contract {
    returns() implies (this@cast is R)
  }
  return this as R
}

/**
 * 安全转换对象类型
 * @return 如果对象类型匹配则返回 [R], 否则返回 null
 */
inline fun <reified R> Any?.safeCast(): R? {
  contract {
    returnsNotNull() implies (this@safeCast is R)
    returns(null) implies (this@safeCast !is R)
  }
  return this as? R
}

/**
 * 如果给定的 [predicate] 满足且为 null,
 * 则返回本身 [T], 否则返回 null.
 *
 * @see takeIf
 */
inline fun <T> T.takeIfNull(predicate: (T) -> Any?): T? = takeIf { predicate(it) == null }

/**
 * 如果给定的 [predicate] 满足且不为 null,
 * 则返回本身 [T], 否则返回 null.
 *
 * @see takeIf
 */
inline fun <T> T.takeIfNotNull(predicate: (T) -> Any?): T? = takeIf { predicate(it) != null }


/**
 * 如果给定的 [T] 为 null, 则使用 [another]
 *
 * @return [T] 或者 [another]
 */
inline fun <T> T?.orElse(another: () -> T): T = this ?: another()
