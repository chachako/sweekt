@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.mars.ui.animation


import android.animation.TimeInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

object Easing {
  /**
   * 均速
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_linear.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1314-L1321)
   */
  val Linear: TimeInterpolator get() = LinearInterpolator()

  /**
   * 快速加速并逐渐放慢
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_fast_out_slow_in.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1654-L1665)
   */
  val FastOutSlowIn: TimeInterpolator get() = FastOutSlowInInterpolator()

  /**
   * 匀减速
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_decelerate.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1323-L1329)
   */
  val Decelerate: TimeInterpolator get() = LinearOutSlowInInterpolator()

  /**
   * 加速
   * [Preview](https://material.io/design/motion/speed.html#easing)
   */
  val Accelerate: TimeInterpolator get() = FastOutLinearInInterpolator()

  /**
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_fast_linear_to_slow_ease_in.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1332-L1339)
   */
  val FastLinearToSlowEaseIn: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.18F, 1.0F, 0.04F, 1.0F)

  /**
   * 快速加速然后慢慢结束
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1341-L1344)
   */
  val Ease: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.25F, 0.1F, 0.25F, 1.0F)

  /**
   * 慢慢加速然后快速结束
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease_in.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1346-L1349)
   */
  val EaseIn: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.42F, 0.0F, 1.0F, 1.0F)

  /**
   * 类似 [EaseIn], 但曲线的开头和结尾更低一些
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease_in_sine.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1358-L1366)
   */
  val EaseInSine: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.47F, 0.0F, 0.745F, 0.715F)

  /**
   * 类似 [EaseInSine], 但曲线更陡峭
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease_in_quad.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1368-L1377)
   */
  val EaseInQuad: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.55F, 0.085F, 0.68F, 0.53F)

  /**
   * 类似 [EaseInQuad], 但曲线更陡峭
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease_in_cubic.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1379-L1388)
   */
  val EaseInCubic: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.55F, 0.055F, 0.675F, 0.19F)

  /**
   * 快速开始然后慢慢结束
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease_out.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1447-L1450)
   */
  val EaseOut: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.0F, 0.0F, 0.58F, 1.0F)

  /**
   * 类似 [EaseOut], 但曲线的开头和结尾更高一些
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease_out_sine.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1459-L1467)
   */
  val EaseOutSine: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.39F, 0.575F, 0.565F, 1.0F)

  /**
   * 类似 [EaseOutSine], 但曲线更突出
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease_out_quad.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1469-L1478)
   */
  val EaseOutQuad: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.25F, 0.46F, 0.45F, 0.94F)

  /**
   * 类似 [EaseInQuad], 但曲线更突出
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_ease_out_cubic.mp4)
   * [More details](https://github.com/flutter/flutter/blob/60494b4c1efcea29002491e8ae1a5ed05a6f1536/packages/flutter/lib/src/animation/curves.dart#L1480-L1491)
   */
  val EaseOutCubic: TimeInterpolator
    get() = PathInterpolatorCompat.create(0.215F, 0.61F, 0.355F, 1.0F)

  /**
   * 幅度越来越大的振荡曲线变化
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_bounce_in.mp4)
   * [More details](https://easings.net/cn#easeInBounce)
   */
  val BounceIn: TimeInterpolator get() = TimeInterpolator { 1 - BounceOut.getInterpolation(1 - it) }

  /**
   * 先增大然后减小幅度的振荡曲线变化
   * [Preview](https://flutter.github.io/assets-for-api-docs/assets/animation/curve_bounce_out.mp4)
   * [More details](https://easings.net/cn#easeOutBounce)
   */
  val BounceOut: TimeInterpolator
    get() = TimeInterpolator {
      var x = it
      val n1 = 7.5625f
      val d1 = 2.75f
      when {
        x < 1 / d1 -> n1 * x * x
        x < 2 / d1 -> {
          x -= 1.5f / d1
          n1 * x * x + 0.75f
        }
        x < 2.5 / d1 -> {
          x -= 2.25f / d1
          n1 * x * x + 0.9375f
        }
        else -> {
          x -= 2.625f / d1
          n1 * x * x + 0.984375f
        }
      }
    }
}