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

package com.meowbase.ui.core

import android.view.Gravity

/*
 * author: 凛
 * date: 2020/8/9 1:57 AM
 * github: https://github.com/RinOrz
 * description: 所有的方向对齐
 */
enum class Alignment(val gravity: Int) {
  Top(Gravity.TOP),
  TopStart(Gravity.TOP or Gravity.START),
  TopCenter(Gravity.TOP or Gravity.CENTER),
  TopEnd(Gravity.TOP or Gravity.END),

  Center(Gravity.CENTER),
  CenterStart(Gravity.CENTER or Gravity.START),
  CenterEnd(Gravity.CENTER or Gravity.END),
  CenterVertically(Gravity.CENTER_VERTICAL),
  CenterHorizontally(Gravity.CENTER_HORIZONTAL),

  Bottom(Gravity.BOTTOM),
  BottomStart(Gravity.BOTTOM or Gravity.START),
  BottomCenter(Gravity.BOTTOM or Gravity.CENTER),
  BottomEnd(Gravity.BOTTOM or Gravity.END),

  Start(Gravity.START),
  End(Gravity.END),
}


/*
 * 用于指定子元素在主轴方向上的对齐方式
 * 拥有与 Flutter 等同的效果
 * reference: https://juejin.im/post/6844903778643099661
 */
enum class MainAxisAlignment {
  /** 将子内容放在主轴的开始位置 */
  Start,

  /** 将子内容放在主轴的中心位置 */
  Center,

  /** 将子内容放在主轴的结束位置 */
  End,

  /**
   * 均匀分布子内容到主轴，自动计算子内容的间距
   * 忽略主轴 [Start] [End] 两端的间距，完全填满
   * @see SpaceAround
   * @see SpaceEvenly
   */
  SpaceBetween,

  /**
   * 均匀分布子内容到主轴，自动计算间距（称其为 A）
   * [Start] [End] 两端边界的距离为子内容间距的一半，即 A/2
   * @see SpaceBetween
   * @see SpaceEvenly
   */
  SpaceAround,

  /**
   * 使子内容分布绝对均匀，所有的主轴的间距都会相等
   * @see SpaceBetween
   * @see SpaceAround
   */
  SpaceEvenly,
}

/*
 * 用于指定子元素在交叉轴方向上的对齐方式
 * 拥有与 Flutter 等同的效果
 * reference: https://juejin.im/post/6844903778643099661
 * todo: add Baseline property
 */
enum class CrossAxisAlignment {
  /** 将子内容放在交叉轴的开始位置 */
  Start,

  /** 将子内容放在交叉轴的中心位置 */
  Center,

  /** 将子内容放在交叉轴的结束位置 */
  End,

  /** 让子内容填满交叉轴 */
  Stretch,
}

/**
 * 转换为原生 [Gravity]
 * @param mainAxisHorizontal 决定交叉轴是否为垂直方向
 */
fun MainAxisAlignment.toGravity(mainAxisHorizontal: Boolean) = when (this) {
  MainAxisAlignment.Start -> if (mainAxisHorizontal) Gravity.START else Gravity.TOP
  MainAxisAlignment.Center -> if (mainAxisHorizontal) Gravity.CENTER_HORIZONTAL else Gravity.CENTER_VERTICAL
  MainAxisAlignment.End -> if (mainAxisHorizontal) Gravity.END else Gravity.BOTTOM
  MainAxisAlignment.SpaceBetween -> null
  MainAxisAlignment.SpaceAround -> null
  MainAxisAlignment.SpaceEvenly -> null
}

/**
 * 转换为原生 [Gravity]
 * @param mainAxisHorizontal 声明主轴方向是否为水平方向，用于决定交叉轴是否为垂直方向
 */
fun CrossAxisAlignment.toGravity(mainAxisHorizontal: Boolean) = when (this) {
  CrossAxisAlignment.Start -> if (!mainAxisHorizontal) Gravity.START else Gravity.TOP
  CrossAxisAlignment.Center -> if (!mainAxisHorizontal) Gravity.CENTER_HORIZONTAL else Gravity.CENTER_VERTICAL
  CrossAxisAlignment.End -> if (!mainAxisHorizontal) Gravity.END else Gravity.BOTTOM
  CrossAxisAlignment.Stretch -> null
}