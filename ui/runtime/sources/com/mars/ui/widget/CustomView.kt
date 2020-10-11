@file:Suppress("FunctionName", "NestedLambdaShadowedImplicitParameter")

package com.mars.ui.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.mars.ui.Ui
import com.mars.ui.asLayout
import com.mars.ui.core.Modifier
import com.mars.ui.core.decoupling.ModifierProvider

/**
 * 创建并添加 [V] 类型的视图
 * 示例:
 * ```
 * With(::CustomView, Modifier.size(50.dp)) { view ->
 *   view.alpha = 0f
 *   ...
 * }
 * ```
 *
 * @param creator 提供一个 [V] 视图实例
 * @param modifier 此视图在布局上或其他的一些调整
 * @param options 提供此视图的所有可使用 api
 */
inline fun <V : View> Ui.With(
  creator: (Context) -> V,
  modifier: Modifier = Modifier,
  options: (V) -> Unit = {},
) = creator(this.asLayout.context).also {
  this.asLayout.addView(it)
  (it as? ModifierProvider)?.also {
    it.modifier = modifier
  } ?: modifier.apply { it.realize(this@With.asLayout) }
  options(it)
}

/**
 * 创建并添加 [V] 类型的 [ViewGroup] 视图
 * 示例:
 * ```
 * WithLayout(
 *   ::CustomView,
 *   modifier = Modifier.size(50.dp),
 *   options = { view ->
 *     view.alpha = 0f
 *     ...
 *   }
 * ) {
 *   // add some views...
 *   Text()
 *   Image()
 * }
 * ```
 *
 * @param creator 提供一个 [V] 视图实例
 * @param modifier 此视图在布局上或其他的一些调整
 * @param options 提供此视图的所有可使用 api
 * @param children 子视图块，块内定义的所有视图都会被添加到 [V]
 */
inline fun <V : ViewGroup> Ui.WithLayout(
  creator: (Context) -> V,
  modifier: Modifier = Modifier,
  options: (V) -> Unit = {},
  children: Ui.() -> Unit = {},
) = creator(this.asLayout.context).also {
  this.asLayout.addView(it)
  (it as? ModifierProvider)?.also {
    it.modifier = modifier
  } ?: modifier.apply { it.realize(this@WithLayout.asLayout) }
  options(it)
  (it as? Ui)?.apply(children)
}