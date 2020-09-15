@file:Suppress("FunctionName", "ObjectPropertyName")

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
import com.mars.ui.UiKit.Companion._currentContext
import com.mars.ui.UiPreview.Companion.currentIdePreview
import com.mars.ui.core.Modifier
import com.mars.ui.theme.*

/** 限制 DSL 的子控件层级访问 */
@DslMarker annotation class UiKitMarker

interface UiKit {
  interface Container: UiKit {
    var colors: Colors
    var typography: Typography
    var materials: Materials
    var shapes: Shapes
    var styles: Styles
    var icons: Icons
    var buttons: Buttons
  }

  companion object {
    /** 这应该当前界面上的唯一一个 [UiKit] 的标识 */
    internal const val Tag = "UIKIT_CONTAINER_TAG"

    /** 这应该当前界面上的唯一一个 [UiKit] 的 ID */
    internal val Id = View.generateViewId()

    internal var _currentContext: Context? = null

    /** 当前屏幕显示的 [Activity] 实例 */
    val currentContext: Context
      get() = _currentContext
        ?: error("发生意外，无法获得当前屏幕上的 Context 实例。出错原因可能是你在 setUiContent 前调用了 Theme.* 或者 dp, sp 等需要上下文的扩展成员，你必须要在初始化后才能这么做！")
  }

  /**
   * 为 Ui 提供 IDE 预览功能
   * @see UiPreview
   */
  interface Preview {
    /** Provide root of current ui */
    val ui: UiScope
  }
}

/**
 * 设置 UI 内容到 [Activity]
 *
 * @param lifecycleOwner
 * @param modifier 对于 UI 整体的一些修饰调整
 * @param content 具体 UI 内容
 * @see UiKit
 */
fun Activity.setUiContent(
  lifecycleOwner: LifecycleOwner,
  modifier: Modifier = Modifier,
  content: UiScope,
) {
  _currentContext = this
  val uikit: Ui = windowView.findViewById(UiKit.Id)
    ?: window.decorView.findViewWithTag(UiKit.Tag)
    ?: Ui(this.wrapped()).also {
      it.lifecycleOwner = lifecycleOwner
      setContentView(it)
    }
  modifier.realize(uikit, uikit.parent as? ViewGroup)
  uikit.apply(content)
}

/**
 * 设置 UI 内容到 [ComponentActivity]
 *
 * @param modifier 对于 UI 整体的一些修饰调整
 * @param content 具体 UI 内容
 * @see UiKit
 */
fun ComponentActivity.setUiContent(
  modifier: Modifier = Modifier,
  content: UiScope,
) = setUiContent(this, modifier, content)

/**
 * 为其他任意布局添加 UiKit 能力
 *
 * @param lifecycleOwner
 * @param modifier 对于 UI 整体的一些修饰调整
 * @param content 具体 UI 内容
 * @see setUiContent
 */
fun ViewGroup.UiKit(
  lifecycleOwner: LifecycleOwner,
  modifier: Modifier = Modifier,
  content: UiScope,
) {
  _currentContext = context
  val uikit: Ui = (parent as? ViewGroup)?.findViewById(UiKit.Id)
    ?: (parent as? ViewGroup)?.findViewWithTag(UiKit.Tag)
    ?: Ui(context.wrapped()).also {
      it.lifecycleOwner = lifecycleOwner
      addView(it)
    }
  modifier.realize(uikit, uikit.parent as? ViewGroup)
  uikit.apply(content)
}

/** 用 Material 主题包装上下文 */
private fun Context.wrapped() =
  ContextThemeWrapper(this, R.style.Theme_MaterialComponents_Light_NoActionBar)

/** 返回当前 [Activity] 的 UiKit */
@PublishedApi internal val Activity.currentUiKit: UiKit.Container
  get() = currentIdePreview
    ?: windowView as? UiKit.Container
    ?: windowView.getChildAt(0) as? UiKit.Container
    ?: windowView.findViewById(UiKit.Id) as? UiKit.Container
    ?: windowView.findViewWithTag(UiKit.Tag) as? UiKit.Container
    ?: error("Context 不能转换为 Activity, 你可能无法在此上下文中获取 UiKit")

/**
 * 返回当前上下文中的 UiKit
 * @receiver [Context] 必须是 [Activity]
 */
@PublishedApi internal val Context.currentUiKit: UiKit.Container
  get() = currentIdePreview
    ?: (this as? Activity)?.currentUiKit
    ?: error("Context 不能转换为 Activity, 你可能无法在此上下文中获取 UiKit")

/** 返回当前的 UiKit */
@PublishedApi internal val View.currentUiKit: UiKit.Container
  get() = currentIdePreview
    ?: this as? UiKit.Container
    ?: parent as? UiKit.Container
    ?: context.asActivityOrNull?.currentUiKit
    ?: error("没有找到界面上的 UiKit, 请使用 *.setUiContent(...) 或 *.UiKit(...) 将 UI 内容添加到当前界面上")

val UiKit.asLayout get() = this as? ViewGroup ?: error("意外情况！UiKit 不是一个 ViewGroup")