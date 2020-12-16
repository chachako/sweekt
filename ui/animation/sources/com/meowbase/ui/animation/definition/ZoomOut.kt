@file:SuppressLint("Recycle")

package com.meowbase.ui.animation.definition


/**
 * 缩小动画
 * 缩小的定义是将视图从某个轴缩小回任意比例值
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/6 - 17:53
 */
import com.meowbase.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import com.meowbase.ui.animation.core.AnimationDefinition
import com.meowbase.ui.animation.Motion
import com.meowbase.ui.core.graphics.geometry.Scale
import kotlin.time.Duration
import com.meowbase.ui.animation.core.Repeat
import android.animation.TimeInterpolator
import com.meowbase.ui.animation.util.applyConfigurations


/**
 * 定义一个缩小动画
 *
 * @param targetScale 这是一个会保证视图绘制完后执行的 lambda
 * 它接收一个动画结束的 xy 比例值，参考 [zoomOutHorizontally] [zoomOutVertically]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomOut(
  targetScale: (view: View) -> Scale,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val scale = targetScale(view)
    playTogether(
      ObjectAnimator.ofFloat(view, View.SCALE_X, view.scaleX, scale.x),
      ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, scale.y),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个缩小动画
 *
 * @param targetScale 动画结束的 xy 值
 * 参考 [zoomOutHorizontally] [zoomOutVertically]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomOut(
  targetScale: Scale,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    playTogether(
      ObjectAnimator.ofFloat(view, View.SCALE_X, view.scaleX, targetScale.x),
      ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, targetScale.y),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个缩小动画
 *
 * @param targetScale 动画结束的 xy 比例值
 * 参考 [zoomOutHorizontally] [zoomOutVertically]
 * 默认将从 [View.getScaleX] [View.getScaleY] 缩小回 0f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomOut(
  targetScale: Float = 0f,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    playTogether(
      ObjectAnimator.ofFloat(view, View.SCALE_X, view.scaleX, targetScale),
      ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, targetScale),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个横向缩小动画
 *
 * @param targetScaleX 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画结束值
 * 将会从 [View.getScaleX] 开始缩小 x 轴到返回值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomOutHorizontally(
  targetScaleX: (view: View) -> Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_X, view.scaleX, targetScaleX(view))
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个横向缩小动画
 *
 * @param targetScaleX 动画结束值，将会从 [View.getScaleX] 开始缩小 x 轴到结束值
 * 默认将视图缩小直到消失，即 0f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomOutHorizontally(
  targetScaleX: Float = 0f,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_X, view.scaleX, targetScaleX)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个纵向缩小动画
 *
 * @param targetScaleY 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画结束值
 * 将会从 [View.getScaleY] 开始缩小 y 轴到返回值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomOutVertically(
  targetScaleY: (view: View) -> Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_Y, view.scaleY, targetScaleY(view))
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个纵向缩小动画
 *
 * @param targetScaleY 动画结束值，将会从 [View.getScaleY] 开始缩小 y 轴到结束值
 * 默认将视图缩小直到消失，即 0f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomOutVertically(
  targetScaleY: Float = 0f,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_Y, view.scaleY, targetScaleY)
    .applyConfigurations(delay, duration, repeat, easing)
}