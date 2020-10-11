package com.mars.ui.animation.definition

/**
 * 平移动画
 * 平移的定义是将视图从某个位置使用偏移值移动到另一个位置
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/10/6 - 22:50
 */
import com.mars.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import com.mars.ui.animation.core.AnimationDefinition
import com.mars.ui.animation.Motion
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.isSpecified
import com.mars.ui.core.unit.toPx
import com.mars.ui.core.unit.toPxOrNull
import kotlin.time.Duration
import com.mars.ui.animation.core.Repeat
import android.animation.TimeInterpolator
import com.mars.ui.animation.util.applyConfigurations


/**
 * 定义一个平移动画
 *
 * @param fromX 动画 x 轴开始值（如果此值没有定义但定义了 [toX] 则将使用 [View.getTranslationX]）
 * @param toX 动画 x 轴结束值（如果此值没有定义则直接忽略 [fromX]）
 *
 * @param fromY 动画 y 轴开始值（如果此值没有定义但定义了 [toY] 则将使用 [View.getTranslationY]）
 * @param toY 动画 y 轴结束值（如果此值没有定义则直接忽略 [fromY]）
 *
 * @param fromZ 动画 z 轴开始值（如果此值没有定义但定义了 [toZ] 则将使用 [View.getTranslationZ]）
 * @param toZ 动画 z 轴结束值（如果此值没有定义则直接忽略 [fromZ]）
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 *
 * @see slideIn
 * @see slideOut
 */
fun Motion.translate(
  fromX: SizeUnit = SizeUnit.Unspecified,
  toX: SizeUnit = SizeUnit.Unspecified,
  fromY: SizeUnit = SizeUnit.Unspecified,
  toY: SizeUnit = SizeUnit.Unspecified,
  fromZ: SizeUnit = SizeUnit.Unspecified,
  toZ: SizeUnit = SizeUnit.Unspecified,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  @SuppressLint("Recycle")
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val animators = mutableListOf<Animator>()
    // 如果定义了目标 X 轴
    if (toX.isSpecified) {
      animators += ObjectAnimator.ofFloat(
        view,
        View.TRANSLATION_X,
        fromX.toPxOrNull() ?: view.translationX,
        toX.toPx()
      )
    }
    // 如果定义了目标 Y 轴
    if (toY.isSpecified) {
      animators += ObjectAnimator.ofFloat(
        view,
        View.TRANSLATION_Y,
        fromY.toPxOrNull() ?: view.translationY,
        toY.toPx()
      )
    }
    // 如果定义了目标 Z 轴
    if (toZ.isSpecified) {
      animators += ObjectAnimator.ofFloat(
        view,
        View.TRANSLATION_Z,
        fromZ.toPxOrNull() ?: view.translationZ,
        toZ.toPx()
      )
    }
    playTogether(*animators.toTypedArray())
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个平移动画
 *
 * @param fromXy 动画 xy 轴同时开始的值
 * 如果此值没有定义但定义了 [toXy] 则将使用 [View.getTranslationX]
 * @param toXy 动画 xy 轴同时结束的值（如果此值没有定义则直接忽略 [fromXy]）
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 *
 * @see slideIn
 * @see slideOut
 */
fun Motion.translate(
  fromXy: SizeUnit = SizeUnit.Unspecified,
  toXy: SizeUnit = SizeUnit.Unspecified,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = translate(
  fromX = fromXy,
  toX = toXy,
  fromY = fromXy,
  toY = toXy,
  delay = delay,
  duration = duration,
  easing = easing
)

/**
 * 定义一个平移动画
 *
 * ```
 * translate(
 *  // 从右偏移 2dp 开始，然后向右移动 180dp，最后向左移动直到离原视图 -180dp
 *   transitionX = arrayOf(2.dp, 180.dp, -180.dp),
 * )
 * ```
 *
 * @param transitionX 多个 x 轴过渡值，将会为每个不同时间段自动估值
 * @param transitionY 多个 y 轴过渡值，将会为每个不同时间段自动估值
 * @param transitionZ 多个 z 轴过渡值，将会为每个不同时间段自动估值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 *
 * @see slideIn
 * @see slideOut
 */
fun Motion.translate(
  transitionX: Array<SizeUnit>? = null,
  transitionY: Array<SizeUnit>? = null,
  transitionZ: Array<SizeUnit>? = null,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  @SuppressLint("Recycle")
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val animators = mutableListOf<Animator>()
    if (transitionX != null) {
      animators.add(
        ObjectAnimator.ofFloat(
          view, View.TRANSLATION_X,
          *transitionX.map { it.toPx() }.toFloatArray()
        )
      )
    }
    if (transitionY != null) {
      animators.add(
        ObjectAnimator.ofFloat(
          view, View.TRANSLATION_Y,
          *transitionY.map { it.toPx() }.toFloatArray()
        )
      )
    }
    if (transitionZ != null) {
      animators.add(
        ObjectAnimator.ofFloat(
          view, View.TRANSLATION_Z,
          *transitionZ.map { it.toPx() }.toFloatArray()
        )
      )
    }
    playTogether(*animators.toTypedArray())
    applyConfigurations(delay, duration, repeat, easing)
  }
}
