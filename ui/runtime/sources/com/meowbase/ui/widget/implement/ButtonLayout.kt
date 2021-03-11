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

@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate",
  "PropertyName", "NestedLambdaShadowedImplicitParameter"
)

package com.meowbase.ui.widget.implement

import android.content.Context
import android.util.AttributeSet
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.core.Border
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Padding
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.shape.Shape
import com.meowbase.ui.widget.style.ButtonStyle
import com.meowbase.ui.theme.Styles.Companion.resolveStyle

/*
 * author: 凛
 * date: 2020/8/9 11:54 PM
 * github: https://github.com/RinOrz
 * description: 按钮的扩展
 */
@UiKitMarker class ButtonLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : Linear(context, attrs, defStyleAttr, defStyleRes), ButtonUi {
  /** 记录最后设置的值，以便主题系统来判断是否要更新对应的值 */
  private var style: ButtonStyle? = null
    set(value) {
      field = value
      value?.apply(this)
    }

  /** 更新 [ButtonLayout] 的一些参数 */
  fun update(
    /** [ButtonStyle.color] */
    color: Color = Color.Unspecified,
    /** [ButtonStyle.colorRipple] */
    colorRipple: Color = Color.Unspecified,
    /** [ButtonStyle.colorHighlight] */
    colorHighlight: Color = Color.Unspecified,
    /** [ButtonStyle.colorDisabled] */
    colorDisabled: Color = Color.Unspecified,
    /** [ButtonStyle.border] */
    border: Border? = null,
    /** [ButtonStyle.padding] */
    padding: Padding = Padding.Unspecified,
    /** [ButtonStyle.shape] */
    shape: Shape? = null,
    /** 对于按钮的其他额外调整 */
    modifier: Modifier = Modifier,
    /** 按钮的样式 */
    style: ButtonStyle = this.style!!,
  ) = also {
    it.modifier = modifier
    it.style = style.merge(
      ButtonStyle(
        color,
        colorRipple,
        colorHighlight,
        colorDisabled,
        border,
        padding,
        shape
      )
    )
  }

  override fun updateUiKitTheme() {
    super.updateUiKitTheme()
    style?.resolveStyle(this)?.also { style = it }
  }
}