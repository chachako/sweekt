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

@file:Suppress("FunctionName", "NestedLambdaShadowedImplicitParameter")

package com.meowbase.ui.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.meowbase.ui.Ui
import com.meowbase.ui.asLayout
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.decoupling.ModifierProvider

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