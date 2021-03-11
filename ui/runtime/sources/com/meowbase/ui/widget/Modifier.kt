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

@file:Suppress("FunctionName")

package com.meowbase.ui.widget

import android.view.ViewGroup
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.decoupling.ViewCatcher

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