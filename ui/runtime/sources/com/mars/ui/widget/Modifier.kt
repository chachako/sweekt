@file:Suppress("FunctionName")

package com.mars.ui.widget

import android.view.ViewGroup
import com.mars.ui.Ui
import com.mars.ui.core.Modifier
import com.mars.ui.core.decoupling.ViewCatcher

/**
 * 修改范围内的所有子项
 * @param modifier 定义修改器
 * @param children 需要修改的子项 block，在 block 内定义的 Ui 都会执行修改
 */
inline fun Ui.ModifyScope(
  modifier: Modifier,
  children: Ui.() -> Unit,
): Ui = apply {
  val scope = this as ViewCatcher
  scope.startCapture()
  children(this)
  scope.captured?.forEach {
    modifier.apply { it.realize(this@ModifyScope as? ViewGroup) }
  }
  scope.endCapture()
}