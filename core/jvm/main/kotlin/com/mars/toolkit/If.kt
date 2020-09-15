@file:Suppress("NOTHING_TO_INLINE")

package com.mars.toolkit

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