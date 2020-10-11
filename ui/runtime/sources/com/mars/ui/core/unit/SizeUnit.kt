@file:Suppress("NOTHING_TO_INLINE")

package com.mars.ui.core.unit

import com.mars.ui.core.unit.SizeUnit.Companion.Unspecified
import kotlin.math.max
import kotlin.math.min

/*
 * author: 凛
 * date: 2020/8/11 2:46 PM
 * github: https://github.com/oh-Rin
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