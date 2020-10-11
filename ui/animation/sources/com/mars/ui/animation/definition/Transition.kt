package com.mars.ui.animation.definition

/**
 *
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/10/8 - 15:46
 */


///**
// * 定义一个值过渡动画
// *
// * @param targetOffset 这是一个会保证视图绘制完后执行的 lambda
// * 它接收一个动画结束的 xy 值，参考 [slideOutHorizontally] [slideOutVertically]
// *
// * @param delay 可单独定义此动画的初始播放延迟时长
// * @param duration 可单独定义此动画的播放持续时长
// * @param repeat 可单独定义此动画的重复播放次数与模式
// * @param easing 可单独定义此动画的插值器
// */
//fun <T: Any> MotionDefinition.transition(
//  from: T,
//  to: T,
//  delay: Duration? = this.delay,
//  duration: Duration? = this.duration,
//  repeat: Repeat? = this.repeat,
//  easing: TimeInterpolator = this.easing,
//  onUpdate: View.(value: T) -> Unit
//): AnimationDefinition = object : AnimationDefinition {
//  override fun getAnimator(view: View): Animator {
//    ValueAnimator.ofObject()
//  }
//
//}