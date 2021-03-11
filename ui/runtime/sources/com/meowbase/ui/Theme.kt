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

@file:Suppress("FunctionName", "ClassName", "unused")

package com.meowbase.ui

import com.meowbase.toolkit.util.generateViewId
import com.meowbase.toolkit.view.forEach
import com.meowbase.ui.theme.*

/**
 * 返回当前 [UiKit] 范围内的唯一主题
 *
 * @author 凛
 * @date 2020/8/8 3:11 AM
 * @github https://github.com/RinOrz
 */
val Ui.currentTheme: Theme get() = container.theme


/**
 * 主题数据持有者
 *
 * @property colors 参考了 Material Design 的颜色系统
 * @property typography 通用的文字排版
 * @property materials 一些通用的材质
 * @property shapes 一些通用的控件形状
 * @property icons 一些通用的图标样式
 * @property buttons 一些通用的按钮样式
 * @property styles 其他局部控件的一些通用样式
 */
data class Theme(
  var colors: Colors = LightColors(),
  var typography: Typography = Typography(),
  var materials: Materials = Materials(),
  var shapes: Shapes = Shapes(),
  var icons: Icons = Icons(),
  var buttons: Buttons = Buttons(),
  var styles: Styles = Styles(),
) {
  /** 代表持有当前主题对象的 [UiKit] 实例 */
  internal lateinit var uikit: UiKit

  /**
   * 将新的主题数据 [newTheme] 覆盖到当前主题上
   * @param notifyUi 通知所有 Ui 进行主题更新
   */
  fun replaceWith(newTheme: Theme, notifyUi: Boolean = true): Theme {
    /** 不直接替换颜色库是因为其中有一个 [Colors.isLight] */
    colors = colors.merge(newTheme.colors)
    typography = newTheme.typography
    materials = newTheme.materials
    shapes = newTheme.shapes
    styles = newTheme.styles
    icons = newTheme.icons
    buttons = newTheme.buttons

    // 通知 Ui 更新
    if (notifyUi) {
      uikit.forEach(recursively = true) {
        // 主题已经替换，更新全部支持主题的控件以应用新主题
        (it as? User)?.updateUiKitTheme()
      }
    }

    return this
  }

  companion object {
    val Id = generateViewId()
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