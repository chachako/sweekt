@file:SuppressLint("Recycle")

package com.mars.ui.animation.definition


/**
 * 放大动画
 * 放大的定义是将视图从某个比例放大回 1f
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/10/6 - 17:53
 */
import com.mars.ui.animation.core.leanback.*
import android.annotation.SuppressLint
import android.view.View
import com.mars.toolkit.float
import com.mars.ui.animation.core.AnimationDefinition
import com.mars.ui.animation.Motion
import com.mars.ui.core.graphics.geometry.Scale
import kotlin.time.Duration
import com.mars.ui.animation.core.Repeat
import android.animation.TimeInterpolator
import com.mars.ui.animation.util.applyConfigurations


/**
 * 定义一个放大动画
 *
 * @param initialScale 这是一个会保证视图绘制完后执行的 lambda
 * 它接收一个动画开始的 xy 比例值，参考 [zoomInX] [zoomInY]
 * 默认将从 [View.getScaleX] [View.getScaleY] 放大回 1f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomIn(
  initialScale: (view: View) -> Scale = { Scale(it.scaleX, it.scaleY) },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val scale = initialScale(view)
    playTogether(
      ObjectAnimator.ofFloat(view, View.SCALE_X, scale.x, 1f),
      ObjectAnimator.ofFloat(view, View.SCALE_Y, scale.y, 1f),
    )
    applyConfigurations(delay, duration, repeat, easing)
  }
}

/**
 * 定义一个放大动画
 *
 * @param initialScale 动画开始的 xy 值
 * 参考 [zoomInX] [zoomInY]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomIn(
  initialScale: Scale,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromX = initialScale.x,
  toX = 1f,
  fromY = initialScale.y,
  toY = 1f,
  delay = delay,
  duration = duration,
  easing = easing
)

/**
 * 定义一个放大动画
 *
 * @param initialScale 动画开始的 xy 值
 * 参考 [zoomInX] [zoomInY]
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomIn(
  initialScale: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromX = initialScale,
  toX = 1f,
  fromY = initialScale,
  toY = 1f,
  delay = delay,
  repeat = repeat,
  duration = duration,
  easing = easing
)

/**
 * 定义一个横向放大动画
 *
 * @param initialScale 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画开始值
 * 将会从返回值开始放大 x 轴到 1f，默认以 [View.getScaleX] 作为起始偏移值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomInX(
  initialScale: (view: View) -> Float = { it.scaleX },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
//  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_X, initialScale(view).float, 1f)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个横向放大动画
 *
 * @param initialScale 动画开始值，将会从此值开始放大 x 轴到 1f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomInX(
  initialScale: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromX = initialScale,
  toX = 1f,
  delay = delay,
  repeat = repeat,
  duration = duration,
  easing = easing
)

/**
 * 定义一个纵向放大动画
 *
 * @param initialScale 这是一个会保证视图绘制完后执行的 lambda, 它接收一个动画开始值
 * 将会从返回值开始放大 y 轴到 1f，默认以 [View.getScaleY] 作为起始偏移值
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomInY(
  initialScale: (view: View) -> Float = { it.scaleY },
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = object : AnimationDefinition {
  override val needArrange: Boolean = true
  override fun getAnimator(view: View): Animator = ObjectAnimator
    .ofFloat(view, View.SCALE_Y, initialScale(view).float, 1f)
    .applyConfigurations(delay, duration, repeat, easing)
}

/**
 * 定义一个纵向放大动画
 *
 * @param initialScale 动画开始值，将会从此值开始放大 y 轴到 1f
 *
 * @param delay 可单独定义此动画的初始播放延迟时长
 * @param duration 可单独定义此动画的播放持续时长
 * @param repeat 可单独定义此动画的重复播放次数与模式
 * @param easing 可单独定义此动画的插值器
 */
fun Motion.zoomInY(
  initialScale: Float,
  delay: Duration? = this.delay,
  duration: Duration? = this.duration,
  repeat: Repeat? = this.repeat,
  easing: TimeInterpolator = this.easing,
): AnimationDefinition = scale(
  fromY = initialScale,
  toY = 1f,
  delay = delay,
  duration = duration,
  repeat = repeat,
  easing = easing
)