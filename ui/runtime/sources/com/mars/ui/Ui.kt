@file:Suppress("FunctionName", "ObjectPropertyName", "unused")

package com.mars.ui

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import com.google.android.material.R
import com.mars.toolkit.data.ContextProvider
import com.mars.ui.UiPreview.Companion.currentIdePreview
import com.mars.ui.core.Modifier

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
@PublishedApi internal val Ui.container: Ui.Container
  get() = currentIdePreview
    ?: this as? Ui.Container
    ?: this.asView.parent.findContainer()
    ?: error("没有找到当前的 Ui 容器, 请确保使用 *.setUiContent(...) 将 UI 内容添加到任意界面上")

/** 遍历 View 祖先找到 [Ui.Container] */
private fun ViewParent.findContainer(): Ui.Container? = when (this) {
  is Ui.Container -> container
  else -> parent?.findContainer()
}