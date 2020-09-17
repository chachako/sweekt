@file:Suppress("FunctionName", "NestedLambdaShadowedImplicitParameter")

package com.mars.ui.foundation

import android.content.Context
import android.view.View
import com.mars.ui.UiKit
import com.mars.ui.asLayout
import com.mars.ui.core.Modifier

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
 * @param block 提供此视图的所有可使用 api
 */
inline fun <V : View> UiKit.With(
  creator: (Context) -> V,
  modifier: Modifier = Modifier,
  block: (V) -> Unit = {},
) = creator(this.asLayout.context).also {
  this.asLayout.addView(it)
  (it as? ModifierProvider)?.also {
    it.modifier = modifier
  } ?: modifier.apply { it.realize(this@With.asLayout) }
  block(it)
}