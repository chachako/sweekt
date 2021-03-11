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

@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.meowbase.ui.extension.pager.implement

/** 用来标明使用了分页器 */
interface PagerUser {
  var pager: Pager?
}

/** 连接分页器与使用者 */
operator fun Pager.plus(user: PagerUser): Pager = also {
  user.pager = this
}

/** 连接使用者与分页器 */
operator fun PagerUser.plus(pager: Pager): Pager = pager.also {
  this.pager = pager
}

/** 连接分页器与使用者 */
fun connect(pager: Pager, user: PagerUser) {
  user.pager = pager
}

/** 连接多个分页器与使用者 */
fun connect(vararg pagerAndUser: Pair<Pager, PagerUser>) {
  pagerAndUser.forEach {
    it.second.pager = it.first
  }
}

/** 用一个分页器连接多个使用者 */
fun Pager.connect(vararg users: PagerUser) {
  users.forEach {
    it.pager = this
  }
}