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

package com.meowbase.ui.animation.definition

/**
 *
 *
 * @author 凛
 * @github https://github.com/RinOrz
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