package com.meowbase.ui.animation.definition

/**
 * 透明动画
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/6 - 22:50
 */
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.FloatRange
import com.meowbase.ui.animation.core.AnimationDefinition
import com.meowbase.ui.animation.Motion
import com.meowbase.ui.animation.core.Repeat
import com.meowbase.ui.animation.util.applyConfigurations
import kotlin.time.Duration


/**
 * 定义一个透明动画
 *
 * @param from 动画开始的透明度，如果为 null 则以 [View.getAlpha] 来作为开始值
 * @param to 动画结束时的透明度
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 *
 * @see fadeIn
 * @see fadeOut
 */
fun Motion.alpha(
  @FloatRange(from = .0, to = 1.0) from: Float? = null,
  @FloatRange(from = .0, to = 1.0) to: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  @SuppressLint("Recycle")
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.ALPHA, from ?: view.alpha, to)
    .applyConfigurations(delay, duration, repeat, easing)
}


/**
 * 定义一个透明动画
 *
 * ```
 * alpha(
 *   // 从正常透明度开始，然后淡出直到消失，最后再淡入到半透明
 *   transition = floatArrayOf(1f, 0f, 0.5f),
 * )
 * ```
 *
 * @param transition 多个透明过渡值，将会为每个不同时间段自动估算透明值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 *
 * @see fadeIn
 * @see fadeOut
 */
fun Motion.alpha(
  transition: FloatArray,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  @SuppressLint("Recycle")
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_X, *transition)
    .applyConfigurations(delay, duration, repeat, easing)
}
