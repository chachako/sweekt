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

@file:Suppress("FunctionName", "ObjectPropertyName", "unused")

package com.meowbase.ui

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import com.meowbase.ui.UiPreview.Companion.currentIdePreview
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.widget.implement.FakeLayoutScope

interface Ui {
  companion object {
    /** 定义一个空 Ui */
    val Unspecified: UIBody = {}

    /** 用 Material 主题包装上下文 */
    fun Context.wrapMaterialTheme() =
      ContextThemeWrapper(this, R.style.Theme_MaterialComponents_Light_NoActionBar)
  }

  interface Container : Ui {
    var theme: Theme
  }

  /**
   * 为 Ui 提供 IDE 预览功能
   * @see UiPreview
   */
  interface Preview {
    /** Provide root of current ui */
    val uiBody: UIBody

    /** Optional modifier for [uiBody] */
    val modifier: Modifier get() = Modifier

    /** Theme data for [uiBody] */
    val theme: Theme get() = Theme()
  }
}

/** 将 [Ui] 作为布局 [View] 返回 */
inline val Ui.asView: View
  get() = this as? View ?: error("意外情况！Ui 实例不能作为一个 View")

/** 将 [Ui] 作为布局 [ViewGroup] 返回 */
inline val Ui.asLayout: ViewGroup
  get() = this as? ViewGroup ?: error("意外情况！Ui 实例不能作为一个 ViewGroup")

/** 从视图中返回当前屏幕上的唯一的 [Ui.Container] 实例 */
val Ui.container: Ui.Container
  get() = currentIdePreview
    ?: this as? Ui.Container
    ?: this.asView.uiParent?.findContainer()
    ?: error("没有找到当前的 Ui 容器, 请确保使用 *.setUiContent(...) 将 UI 内容添加到任意界面上")

/** 一个适用于在 UiKit 中使用的 [View] 真实父布局获取方法 */
val View.uiParent: ViewGroup? get() = when(this) {
  is FakeLayoutScope -> realParent
  else -> getTag(R.id.real_parent_view) as? ViewGroup ?: parent as? ViewGroup
}

/** 遍历 View 祖先找到 [Ui.Container] */
private fun View.findContainer(): Ui.Container? = when (this) {
  is Ui.Container -> container
  else -> uiParent?.findContainer()
}
