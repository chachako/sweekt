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