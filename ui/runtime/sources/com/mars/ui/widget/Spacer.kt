@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate")

package com.mars.ui.widget

import com.mars.ui.Ui
import com.mars.ui.asLayout
import com.mars.ui.core.Modifier
import com.mars.ui.core.unit.Px
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.widget.implement.*
import com.mars.ui.widget.modifier.height
import com.mars.ui.widget.modifier.size
import com.mars.ui.widget.modifier.width

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