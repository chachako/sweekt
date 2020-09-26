package com.mars.ui

import android.app.Activity
import android.view.ViewGroup
import com.mars.ui.Ui.Companion.wrapMaterialTheme
import com.mars.ui.core.Modifier


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