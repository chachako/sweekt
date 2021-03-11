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

package com.meowbase.toolkit

/**
 * 实现与 java 差不多的判断行为
 * kotlin 并不存在 java 的三元运算符
 * 但现在你可以在 kotlin 上做与 java 类似的判断行为
 * ```
 * var data: T? = null
 * val string = data == null that "no data." ?: "data exists, is: $data"
 * ```
 */
inline infix fun <T> Boolean?.that(default: T?): T? = if (this == true) default else null