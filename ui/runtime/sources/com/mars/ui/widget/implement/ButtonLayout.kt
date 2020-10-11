@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate",
  "PropertyName", "NestedLambdaShadowedImplicitParameter"
)

package com.mars.ui.widget.implement

import android.content.Context
import android.util.AttributeSet
import com.mars.ui.UiKitMarker
import com.mars.ui.core.Border
import com.mars.ui.core.Modifier
import com.mars.ui.core.Padding
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.shape.Shape
import com.mars.ui.widget.style.ButtonStyle
import com.mars.ui.theme.Styles.Companion.resolveStyle

/*
 * author: 凛
 * date: 2020/8/9 11:54 PM
 * github: https://github.com/oh-Rin
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