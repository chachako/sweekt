@file:SuppressLint("Recycle")

package com.mars.ui.animation.definition


/**
 * 滑出动画
 * 滑出的定义是将视图从原位置移出到某个位置
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/10/6 - 22:09
 */
import com.mars.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import com.mars.toolkit.float
import com.mars.ui.animation.core.AnimationDefinition
import com.mars.ui.animation.Motion
import com.mars.ui.core.graphics.geometry.Offset
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.toPx
import kotlin.time.Duration
import com.mars.ui.animation.core.Repeat
import android.animation.TimeInterpolator
import com.mars.ui.animation.util.applyConfigurations


/**
 * 定义一个滑出动画
 *
 * @param targetOffset 这是一个会保证视图绘制完后执行的 lambda
 * 它接收一个动画结束的 xy 值，参考 [slideOutX] [slideOutY]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideOut(
  targetOffset: (view: View) -> Offset,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val offset = targetOffset(view)
    playTogether(
      ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.translationX, offset.x),
      ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.translationY, offset.y),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个滑出动画
 *
 * @param targetOffset 动画结束的 xy 值
 * 参考 [slideOutX] [slideOutY]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideOut(
  targetOffset: Offset,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    playTogether(
      ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.translationX, targetOffset.x),
      ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.translationY, targetOffset.y),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个横向滑出动画
 *
 * ```
 * // 让全屏视图从屏幕中向右移到屏幕中心
 * slideOutX(targetOffset = { it.width / 2 })
 * ```
 *
 * @param targetOffset 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画结束值
 * 如果返回的值是负数，那么将会从 [View.getTranslationX] 开始向左移动到返回值
 * 反之，则从 [View.getTranslationX] 开始向右移动到返回值
 * 默认将以视图的总宽度作为目标值，来进行向右移动动画
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideOutX(
  targetOffset: (view: View) -> Number = { it.width },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.TRANSLATION_X, view.translationX, targetOffset(view).float)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个横向滑出动画
 *
 * ```
 * // 让视图向左滑动 24 (dp)
 * slideOutX(-24.dp)
 * ```
 *
 * @param targetOffset 动画结束值
 * 如果值是负数，那么将会从 [View.getTranslationX] 开始向左移动到此值
 * 反之，则从 [View.getTranslationX] 开始向右移动到此值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideOutX(
  targetOffset: SizeUnit,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.TRANSLATION_X, view.translationX, targetOffset.toPx())
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个纵向滑出动画
 *
 * ```
 * // 让全屏视图从屏幕上侧外向下滑到屏幕中心
 * slideOutY(targetOffset = { it.height / 2 })
 * ```
 *
 * @param targetOffset 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画结束值
 * 如果返回的值是负数，那么将会从 [View.getTranslationY] 开始向上移动到返回值
 * 反之，则从 [View.getTranslationY] 开始向下移动到返回值
 * 默认将以视图的总高度作为目标值，来进行向下移动动画
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideOutY(
  targetOffset: (view: View) -> Number = { it.height },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.TRANSLATION_Y, view.translationY, targetOffset(view).float)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个纵向滑出动画
 *
 * ```
 * // 让视图向下滑动 24 (dp)
 * slideOutY(targetOffset = 24.dp)
 * ```
 *
 * @param targetOffset 动画结束值
 * 如果值是负数，那么将会从 [View.getTranslationY] 开始向上移动到此值
 * 反之，则从 [View.getTranslationY] 开始向下移动到此值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.slideOutY(
  targetOffset: SizeUnit,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.TRANSLATION_Y, view.translationY, targetOffset.toPx())
    .applyConfigurations(delay, duration, repeat, easing)
}