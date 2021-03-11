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

package com.meowbase.toolkit.kotlinpoet.utility


/**
 * KotlinPoet-ClassName 相关的 Api 优化
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/12/11 - 21:01
 */
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName


/** 简单的将字符串转换为 [MemberName] */
fun String.asMemberName() = MemberName(
  this.substringBeforeLast("."),
  this.substringAfterLast(".")
)


/** 简单的将字符串转换为 [ClassName] */
fun String.asClassName() = ClassName(
  this.substringBeforeLast("."),
  this.substringAfterLast(".")
)