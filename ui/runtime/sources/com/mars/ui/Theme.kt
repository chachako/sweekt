@file:Suppress("FunctionName", "ClassName", "unused")

package com.mars.ui

import android.view.ViewGroup
import com.mars.toolkit.view.forEach
import com.mars.ui.UiKit.Companion.currentContext
import com.mars.ui.theme.*


/**
 * 定义一个主题范围，范围内的所有控件风格都可以从此处设置或覆盖
 *
 * @author 凛
 * @date 2020/8/8 3:11 AM
 * @github https://github.com/oh-Rin
 * @param data 主题具体数据
 * @param content 具体 UI 内容
 */
fun ThemeScope(
  data: Theme,
  content: UiKit.() -> Unit,
) = Theme.replaceInternal(data).apply(content)


/**
 * 主题数据持有者
 *
 * @property colors 参考了 Material Design 的颜色系统
 * @property typography 通用的文字排版
 * @property shapes 一些通用的控件形状
 */
data class Theme(
  internal val colors: Colors = LightColors(),
  internal val typography: Typography = Typography(),
  internal val materials: Materials = Materials(),
  internal val shapes: Shapes = Shapes(),
  internal val styles: Styles = Styles(),
  internal val icons: Icons = Icons(),
  internal val buttons: Buttons = Buttons(),
) {
  companion object {
    /** 得到当前界面的主题范围上的唯一 [Colors] 对象 */
    val colors: Colors get() = currentColors

    /** 得到当前界面的主题范围上的唯一 [Typography] 对象 */
    val typography: Typography get() = currentTypography

    /** 得到当前界面的主题范围上的唯一 [Materials] 对象 */
    val materials: Materials get() = currentMaterials

    /** 得到当前界面的主题范围上的唯一 [Shapes] 对象 */
    val shapes: Shapes get() = currentShapes

    /** 得到当前界面的主题范围上的唯一 [Styles] 对象 */
    val styles: Styles get() = currentStyles

    /** 得到当前界面的主题范围上的唯一 [Icons] 对象 */
    val icons: Icons get() = currentIcons

    /** 得到当前界面的主题范围上的唯一 [Buttons] 对象 */
    val buttons: Buttons get() = currentButtons

    /** 复制一个副本以覆盖一些主题数据 */
    fun copy(
      colors: Colors = currentColors,
      typography: Typography = currentTypography,
      materials: Materials = currentMaterials,
      shapes: Shapes = currentShapes,
      styles: Styles = currentStyles,
      icons: Icons = currentIcons,
      buttons: Buttons = currentButtons,
    ) = Theme(colors, typography, materials, shapes, styles, icons, buttons)

    /** 将新的主题数据 [newTheme] 覆盖到当前主题范围上 */
    fun replaceWith(newTheme: Theme) =
      (replaceInternal(newTheme) as ViewGroup).forEach(recursively = true) {
        // 主题被替换，更新全部控件以应用新的主题
        (it as? User)?.updateUiKitTheme()
      }

    internal fun replaceInternal(newTheme: Theme) = currentContext.currentUiKit.apply {
      /** 不直接替换颜色库是因为其中有一个 [Colors.isLight] */
      colors.merge(newTheme.colors)
      typography = newTheme.typography
      materials = newTheme.materials
      shapes = newTheme.shapes
      styles = newTheme.styles
      icons = newTheme.icons
      buttons = newTheme.buttons
    }
  }

  interface User {
    /**
     * 更新主题
     *
     * NOTE: 如果之前使用了主题，主题更新时我们也要更新对应的值
     * 为了避免逻辑错误，后续手动修改了某个样式时将不会被主题覆盖
     * 除非使用的是主题中的值，如 [Colors.background] [Typography.body1] [Shapes.large] 等
     */
    fun updateUiKitTheme()
  }
}