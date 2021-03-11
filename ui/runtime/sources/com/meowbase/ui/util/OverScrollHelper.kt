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

package com.meowbase.ui.util

import kotlin.math.abs

/* Reference: https://github.com/LawnchairLauncher/Lawnchair/blob/alpha/src/com/android/launcher3/touch/OverScroll.java */

/**
 * This curve determines how the effect of scrolling over the limits of the page diminishes
 * as the user pulls further and further from the bounds
 *
 * @param percent The percentage of how much the user has over-scrolled.
 * @return A transformed percentage based on the influence curve.
 */
private fun overScrollInfluenceCurve(percent: Float): Float {
  var f = percent
  f -= 1.0f
  return f * f * f + 1.0f
}

/**
 * 计算具有阻尼效果的滚动值
 * @param distance The original amount over-scrolled.
 * @param total The total amount that the View can over-scroll.
 * @param last The last scroll value that the View can over-scroll.
 * @return The dampened over-scroll amount.
 */
internal fun dampedScroll(distance: Float, total: Int, last: Float): Float {
  // 不同方向的乘以值
  val multiply = if (distance < 0) -1 else 1
  // 值越小，滑动阻力越大
  var dampedFactor = 0.4f

  // 增加阻尼效果后的值
  fun dampedScroll(): Float {
    if (distance.compareTo(0f) == 0) return 0f
    var f = distance / total
    f = f / abs(f) * overScrollInfluenceCurve(abs(f))

    // Clamp this factor, f, to -1 < f < 1
    if (abs(f) >= 1) {
      f /= abs(f)
    }
    return (last + (dampedFactor * f * total)) * multiply
  }

  // 达到总大小的 20% 之后需要慢慢加大阻力
  val range = total * 0.20f

  // 第一次求值
  var damped = dampedScroll()

  // 第二次求值（当超出范围后）
  if (damped > range) {
    // 得到超出范围后的每多少 px（最少 1px）
    val f = (total * 0.02f).coerceAtLeast(1f)
    val times = (damped / f) - (range / f)
    // 超出范围后每多 (f)px 就需要增加亿点点阻力
    dampedFactor -= (times * 0.0108f)
    dampedFactor = dampedFactor.coerceAtLeast(0.0025f)
    // 重新计算增加阻尼效果后的值
    damped = dampedScroll()
  }

  return damped * multiply
}