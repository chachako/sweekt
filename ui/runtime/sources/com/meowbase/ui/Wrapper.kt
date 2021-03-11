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

package com.meowbase.ui

import android.app.Activity
import android.view.ViewGroup
import com.meowbase.ui.Ui.Companion.wrapMaterialTheme
import com.meowbase.ui.core.Modifier


/**
 * 为其他任意布局添加 UiKit 能力
 *
 * @param modifier 对于 UI 整体的一些修饰调整
 * @param theme UI 范围内的默认主题
 * @param content 具体 UI 内容
 * @see setUiContent
 */
fun ViewGroup.setUiContent(
  modifier: Modifier = Modifier,
  theme: Theme = Theme(),
  content: UIBody,
): Ui {
  val uikit = UiKit(context.wrapMaterialTheme())
  uikit.theme = theme.also { it.uikit = uikit }
  // 添加到 ViewGroup
  addView(uikit)
  // 实现修饰
  modifier.apply { uikit.realize(uikit.parent as? ViewGroup) }
  return uikit.apply(content)
}

/**
 * 设置 UI 内容到 [Activity]
 *
 * @param modifier 对于 UI 整体的一些修饰调整
 * @param theme UI 范围内的默认主题
 * @param content 具体 UI 内容
 * @see Ui
 */
fun Activity.setUiContent(
  modifier: Modifier = Modifier,
  theme: Theme = Theme(),
  content: UIBody,
): Ui {
  val uikit = UiKit(this.wrapMaterialTheme())
  uikit.theme = theme.also { it.uikit = uikit }
  // 附加到 Activity
  setContentView(uikit)
  // 实现修饰
  modifier.apply { uikit.realize(uikit.parent as? ViewGroup) }
  return uikit.apply(content)
}