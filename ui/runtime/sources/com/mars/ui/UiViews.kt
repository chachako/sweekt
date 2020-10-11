@file:Suppress("LeakingThis", "ViewConstructor")

package com.mars.ui

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mars.ui.widget.implement.Box
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

/**
 * UiKit - 核心容器（运行时）
 *
 * @author 凛
 * @date 2020/8/10 12:13 AM
 * @github https://github.com/oh-Rin
 */
@UiKitMarker class UiKit @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
) : Box(context, attrs), Ui.Container {
  override var theme = Theme().also { it.uikit = this }
}


/**
 * 为 [Ui] 提供 IDE 预览功能（IDE 编辑）
 *
 * @author 凛
 * @date 2020/9/15 16:28 PM
 * @github https://github.com/oh-Rin
 * @see UiKit
 */
@Suppress("NAME_SHADOWING")
open class UiPreview(
  context: Context,
  attrs: AttributeSet?,
  preview: () -> Ui.Preview,
) : FrameLayout(context, attrs) {
  init {
    GlobalContext.getOrNull() ?: startKoin { androidContext(context) }
    preview().apply {
      if (this is ContextWrapper) {
        ContextWrapper::class.java.getDeclaredMethod(
          "attachBaseContext",
          Context::class.java
        ).apply { isAccessible = true }.invoke(this, context)
      }
      currentIdePreview = setUiContent(
        modifier = modifier,
        theme = theme,
        content = uiBody
      ) as Ui.Container
    }
  }

  companion object {
    const val Deprecated = "仅用于 IDE 编辑预览"
    internal var currentIdePreview: Ui.Container? = null
  }
}