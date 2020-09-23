@file:Suppress("FunctionName", "ObjectPropertyName", "unused")

package com.mars.ui

import android.app.Activity
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.R
import com.mars.toolkit.content.asActivityOrNull
import com.mars.toolkit.content.windowView
import com.mars.toolkit.koin
import com.mars.ui.Ui.Companion._currentContext
import com.mars.ui.Ui.Companion.wrapMaterialTheme
import com.mars.ui.UiPreview.Companion.currentIdePreview
import com.mars.ui.core.Modifier
import com.mars.ui.theme.*

/** 限制 DSL 的子控件层级访问 */
@DslMarker annotation class UiKitMarker

interface Ui {
  interface Container : Ui {
    var colors: Colors
    var typography: Typography
    var materials: Materials
    var shapes: Shapes
    var styles: Styles
    var icons: Icons
    var buttons: Buttons
  }

  companion object {
    /** 这应该当前界面上的唯一一个 [Ui] 的标识 */
    internal const val Tag = "UIKIT_CONTAINER_TAG"

    /** 这应该当前界面上的唯一一个 [Ui] 的 ID */
    internal val Id = View.generateViewId()

    var _currentContext: Context? = koin.getOrNull()

    /** 当前屏幕显示的 [Activity] 实例 */
    val currentContext: Context
      get() = _currentContext
        ?: error("发生意外，无法获得当前屏幕上的 Context 实例。出错原因可能是你在 setUiContent 前调用了 Theme.* 或者 dp, sp 等需要上下文的扩展成员，你必须要在初始化后才能这么做！")

    /** 用 Material 主题包装上下文 */
    fun Context.wrapMaterialTheme() =
      ContextThemeWrapper(this, R.style.Theme_MaterialComponents_Light_NoActionBar)
  }

  /**
   * 为 Ui 提供 IDE 预览功能
   * @see UiPreview
   */
  interface Preview {
    /** Provide root of current ui */
    val uiBody: UiBody
  }
}

typealias UiBody = Ui.() -> Unit

/**
 * 设置 UI 内容到 [Activity]
 *
 * @param lifecycleOwner
 * @param modifier 对于 UI 整体的一些修饰调整
 * @param content 具体 UI 内容
 * @see Ui
 */
fun Activity.setUiContent(
  lifecycleOwner: LifecycleOwner,
  modifier: Modifier = Modifier,
  content: UiBody,
) {
  _currentContext = this
  val uikit: UiContainer = windowView.findViewById(Ui.Id)
    ?: window.decorView.findViewWithTag(Ui.Tag)
    ?: UiContainer(this.wrapMaterialTheme()).also {
      it.lifecycleOwner = lifecycleOwner
      setContentView(it)
    }
  modifier.apply { uikit.realize(uikit.parent as? ViewGroup) }
  uikit.apply(content)
}

/**
 * 设置 UI 内容到 [ComponentActivity]
 *
 * @param modifier 对于 UI 整体的一些修饰调整
 * @param content 具体 UI 内容
 * @see Ui
 */
fun ComponentActivity.setUiContent(
  modifier: Modifier = Modifier,
  content: UiBody,
) = setUiContent(this, modifier, content)

/**
 * 为其他任意布局添加 UiKit 能力
 *
 * @param lifecycleOwner
 * @param modifier 对于 UI 整体的一些修饰调整
 * @param content 具体 UI 内容
 * @see setUiContent
 */
fun ViewGroup.setUiContent(
  lifecycleOwner: LifecycleOwner,
  modifier: Modifier = Modifier,
  content: UiBody,
) {
  _currentContext = context
  val uikit: UiContainer = (parent as? ViewGroup)?.findViewById(Ui.Id)
    ?: (parent as? ViewGroup)?.findViewWithTag(Ui.Tag)
    ?: UiContainer(context.wrapMaterialTheme()).also {
      it.lifecycleOwner = lifecycleOwner
      addView(it)
    }
  modifier.apply { uikit.realize(uikit.parent as? ViewGroup) }
  uikit.apply(content)
}

val Ui.asLayout: ViewGroup get() = this as? ViewGroup ?: error("意外情况！UiKit 不是一个 ViewGroup")

/** 返回当前 [Activity] 的 UiKit */
@PublishedApi internal val Activity.currentUi: Ui.Container
  get() = currentIdePreview
    ?: windowView as? Ui.Container
    ?: windowView.getChildAt(0) as? Ui.Container
    ?: windowView.findViewById(Ui.Id) as? Ui.Container
    ?: windowView.findViewWithTag(Ui.Tag) as? Ui.Container
    ?: error("Context 不能转换为 Activity, 你可能无法在此上下文中获取 UiKit")

/**
 * 返回当前上下文中的 UiKit
 * @receiver [Context] 必须是 [Activity]
 */
@PublishedApi internal val Context.currentUi: Ui.Container
  get() = currentIdePreview
    ?: this.asActivityOrNull?.currentUi
    ?: error("Context 不能转换为 Activity, 你可能无法在此上下文中获取 UiKit")

/** 返回当前的 UiKit */
@PublishedApi internal val View.currentUi: Ui.Container
  get() = currentIdePreview
    ?: this as? Ui.Container
    ?: parent as? Ui.Container
    ?: context.asActivityOrNull?.currentUi
    ?: error("没有找到界面上的 UiKit, 请使用 *.setUiContent(...) 或 *.UiKit(...) 将 UI 内容添加到当前界面上")