@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.ui.widget.implement

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import com.meowbase.toolkit.float
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.core.Orientation
import com.meowbase.ui.core.graphics.Color
import com.meowbase.ui.core.graphics.useOrElse
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.dp
import com.meowbase.ui.core.unit.toPxOrNull
import com.meowbase.ui.core.unit.useOrElse
import com.meowbase.ui.widget.style.DividerStyle
import com.meowbase.ui.theme.Colors
import com.meowbase.ui.theme.Colors.Companion.resolveColor
import com.meowbase.ui.theme.Styles.Companion.resolveStyle
import com.meowbase.ui.theme.Typography
import com.meowbase.ui.theme.currentColors
import com.meowbase.ui.theme.currentStyles
import kotlin.math.roundToInt

/*
 * author: 凛
 * date: 2020/8/8 9:59 PM
 * github: https://github.com/RinOrz
 * description: 分频器/分割线
 */
@UiKitMarker class Divider @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
  private val paint = Paint()

  private var style: DividerStyle? = null
    set(value) {
      field = value
      value?.apply(this)
    }

  /** 分割线的方向 */
  var orientation: Orientation = Orientation.Horizontal

  /** [DividerStyle.color] */
  var color: Color? = null
    set(value) {
      field = value
      field?.useOrElse {
        currentColors.onBackground.useOrElse { Color.Black }.copy(alpha = DividerAlpha)
      }?.argb?.also { paint.color = it }
    }

  /** [DividerStyle.thickness] */
  var thickness: SizeUnit = SizeUnit.Unspecified
    set(value) {
      field = value.useOrElse { currentStyles.divider.thickness.useOrElse { 0.5.dp } }
      field.toPxOrNull()?.also {
        layoutParams = layoutParams?.apply {
          if (orientation == Orientation.Horizontal) {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = it.roundToInt()
          } else {
            width = it.roundToInt()
            height = ViewGroup.LayoutParams.MATCH_PARENT
          }
        } ?: when (orientation) {
          Orientation.Horizontal -> ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            it.roundToInt()
          )
          Orientation.Vertical -> ViewGroup.LayoutParams(
            it.roundToInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
          )
        }
      }
    }

  /** [DividerStyle.startIndent] */
  var startIndent: Float = 0f

  /** [DividerStyle.endIndent] */
  var endIndent: Float = 0f

  @SuppressLint("MissingSuperCall")
  override fun draw(canvas: Canvas) {
    when (orientation) {
      Orientation.Horizontal -> canvas.drawRect(
        startIndent,
        0f,
        width.float - endIndent,
        height.float,
        paint
      )
      Orientation.Vertical -> canvas.drawRect(
        0f,
        startIndent,
        width.float,
        height.float - endIndent,
        paint
      )
    }
  }

  fun update(
    /** [DividerStyle.color] */
    color: Color = Color.Unspecified,
    /** [DividerStyle.thickness] */
    thickness: SizeUnit = SizeUnit.Unspecified,
    /** [DividerStyle.startIndent] */
    startIndent: SizeUnit = SizeUnit.Unspecified,
    /** [DividerStyle.endIndent] */
    endIndent: SizeUnit = SizeUnit.Unspecified,
    /** [DividerStyle.indent] */
    indent: SizeUnit? = null,
    /** 分割线的样式 */
    style: DividerStyle = this.style!!,
    /**
     * 分割线的方向
     * NOTE: 这会影响 [thickness] 是高度还是宽度
     * [startIndent] 是上还是左
     * [endIndent] 是右还是下
     */
    orientation: Orientation = Orientation.Horizontal,
  ) = also {
    it.orientation = orientation
    it.style = style.merge(DividerStyle(color, thickness, startIndent, endIndent, indent))
  }


  /**
   * 更新主题
   *
   * NOTE: 如果之前使用了主题，主题更新时我们也要更新对应的值
   * 为了避免逻辑错误，后续手动修改了某个样式时将不会被主题覆盖
   * 除非使用的是主题中的值，如 [Colors.background] [Typography.body1] 等
   */
  override fun updateUiKitTheme() {
    /** 修改样式前先备份一下颜色，避免被 [DividerStyle.apply] 覆盖 */
    val backupColor = color
    style?.resolveStyle(this)?.also { style = it }
    backupColor?.resolveColor(this)?.also { color = it }
  }

  companion object {
    private const val DividerAlpha = 0.1f
  }
}