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

@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.meowbase.ui.widget

import com.meowbase.ui.Ui
import com.meowbase.ui.asLayout
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.unit.Px
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.widget.implement.*
import com.meowbase.ui.widget.modifier.height
import com.meowbase.ui.widget.modifier.size
import com.meowbase.ui.widget.modifier.width

/**
 * 创建一个任意大小的空白视图
 * NOTE: [Spacer.draw] 不会进行任何绘制
 * @param width
 * @param height
 */
fun Ui.Spacer(
  width: SizeUnit? = null,
  height: SizeUnit? = null,
): Spacer = With(::Spacer) {
  when {
    width != null && height != null -> Modifier.size(width, height)
    width != null && height == null -> Modifier.width(width)
    width == null && height != null -> Modifier.height(height)
    else -> null
  }?.apply { it.realize(this@Spacer.asLayout) }
}

/**
 * 创建一个正方形的空白视图
 * NOTE: [Spacer.draw] 不会进行任何绘制
 * @param size 高宽大小
 */
fun Ui.Spacer(
  size: SizeUnit,
): Spacer = With(::Spacer) {
  Modifier.size(size).apply { it.realize(this@Spacer.asLayout) }
}

/**
 * 创建一个拥有完整功能的空间视图
 * NOTE: [Spacer.draw] 会进行绘制，并且取决于 [modifier]
 * @param modifier 高宽大小
 */
fun Ui.Spacer(
  modifier: Modifier
): Spacer = With(::Spacer) {
  it.modifier = modifier
  it.enabledDraw = true
  modifier.apply { it.realize(this@Spacer.asLayout) }
}

/**
 * 以剩余的空间创建一个最大宽度的空白视图以划分两个区域
 * RESULT: ————————————————————————————————
 *        | View1 View2 ---Spacer--- View3 |
 *         ————————————————————————————————
 */
fun Row.Spacer(): Spacer = With(::Spacer) {
  Modifier.width(Px.Zero).weight(1).apply { it.realize(this@Spacer) }
}

/**
 * 以剩余的空间创建一个最大高度的空白视图以划分两个区域
 * RESULT: ———————
 *        | View1 |
 *        | View2 |
 *        |   |   |
 *        |   |   |
 *        | Spacer|
 *        |   |   |
 *        |   |   |
 *        | View3 |
 *         ———————
 */
fun Column.Spacer(): Spacer = With(::Spacer) {
  Modifier.height(Px.Zero).weight(1).apply { it.realize(this@Spacer) }
}