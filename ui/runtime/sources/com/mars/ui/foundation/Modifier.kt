@file:Suppress("FunctionName")

package com.mars.ui.foundation

import android.view.ViewGroup
import com.mars.ui.core.Modifier

interface ModifierProvider {
  var modifier: Modifier
}

/**
 * 修改范围内的所有子项
 * @param modifier 定义修改器
 * @param children 需要修改的子项 block，在 block 内定义的 Ui 都会执行修改
 */
inline fun <T : ViewGroup> T.ModifyScope(
  modifier: Modifier,
  children: T.() -> Unit,
): T = apply {
  val scope = this as ViewCatcher
  scope.startCapture()
  children(this)
  scope.captured.forEach {
    modifier.apply { it.realize(this@ModifyScope) }
  }
  scope.endCapture()
}