@file:Suppress("FunctionName", "MemberVisibilityCanBePrivate", "unused")

package com.mars.ui.extension.tab.impl

import android.graphics.Paint
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.mars.ui.core.LayoutSize
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.gradient.LinearGradient
import com.mars.ui.core.graphics.useOrElse
import com.mars.ui.core.unit.SizeUnit
import com.mars.ui.core.unit.dp
import com.mars.ui.theme.currentColors

/*
 * author: 凛
 * date: 2020/8/30 12:19 PM
 * github: https://github.com/oh-Rin
 * description: Tab 的指示器的相关类
 */
data class TabIndicator internal constructor(
  /** 指示器的颜色 */
  val color: Color = Color.Unset,
  /**
   * 指示器的宽度
   * 当为 [LayoutSize.Wrap] 时，包装 Tab 内容视图并取出减去 [View.getPaddingStart], [View.getPaddingEnd] 的宽度
   * 当为 [LayoutSize.Match] 时，将忽略 Tab 视图的 [View.getPaddingStart], [View.getPaddingEnd], 并填满整个标签大小
   */
  val width: SizeUnit = LayoutSize.Match,
  /** 指示器的高度/厚度 */
  val height: SizeUnit = 4.dp,
  /** 自定义渲染画笔参数 [TabBar.indicatorPaint] */
  val updatePaint: (Paint.(start: Float, end: Float, top: Float, bottom: Float, offset: Float) -> Unit)? = null,
  /**
   * 渲染器，用于渲染并显示指示器
   * @see TabIndicatorRenderer.Rectangle
   * @see TabIndicatorRenderer.Rounded
   */
  val renderer: TabIndicatorRenderer = TabIndicatorRenderer.Rounded,
  /** unused */
  private val use: Boolean,
) {

  /** Tab 指示器滑动偏移模式 */
  enum class OffsetMode {
    /**
     * 无论如何切换页面
     * 让页面的 Tab 永远在显示在屏幕中心（包括初始化）
     * [查看源效果](https://github.com/ogaclejapan/SmartTabLayout/blob/master/demo/src/main/res/layout/demo_basic_title_offset_auto_center.xml)
     */
    AlwaysInCenter,

    /**
     * 与 [AlwaysInCenter] 不同的是
     * 初始化时指示器会在开头显示
     * [查看效果预览](https://raw.githubusercontent.com/ogaclejapan/SmartTabLayout/master/art/demo3.gif)
     */
    AutoInCenter,

    /**
     * 保持第一次滑动后的偏移
     * [查看效果预览](https://raw.githubusercontent.com/ogaclejapan/SmartTabLayout/master/art/demo3.gif)
     * @see [TabBarContainer.indicatorOffset]
     */
    KeepFirst,
  }


  /** Tab 指示器的动画插值器 */
  interface Interpolator {
    /** 滑动时指示器的左边位置 */
    fun getLeftEdge(offset: Float): Float = offset

    /** 滑动时指示器的右边位置 */
    fun getRightEdge(offset: Float): Float = offset

    /** 滑动时的指示器厚度 */
    fun getThickness(offset: Float): Float = 1f // 滑动时不会改变厚度
  }

  /** 线性插值器，最朴素的效果 */
  object LinearInterpolator : Interpolator

  /**
   * 智能插值器
   * 滑动时会根据 factor 与当前指示器开头和下一个指示器的结尾来决定厚度
   * @param factor
   */
  class SmartInterpolator(factor: Float = 3.0f) : Interpolator {
    private val leftEdgeInterpolator = AccelerateInterpolator(factor)
    private val rightEdgeInterpolator = DecelerateInterpolator(factor)

    override fun getLeftEdge(offset: Float): Float {
      return leftEdgeInterpolator.getInterpolation(offset)
    }

    override fun getRightEdge(offset: Float): Float {
      return rightEdgeInterpolator.getInterpolation(offset)
    }

    override fun getThickness(offset: Float): Float {
      return 1f / (1.0f - getLeftEdge(offset) + getRightEdge(offset))
    }

    companion object {
      val Default by lazy { SmartInterpolator() }
    }
  }

  /**
   * 以 Function 形式创建插值器
   * @see Interpolator
   */
  fun Interpolator(
    leftEdge: ((Float) -> Float)? = null,
    rightEdge: ((Float) -> Float)? = null,
    thickness: ((Float) -> Float)? = null,
  ) = object : Interpolator {
    /** 滑动时指示器的左边位置 */
    override fun getLeftEdge(offset: Float): Float = leftEdge?.invoke(offset) ?: offset

    /** 滑动时指示器的右边位置 */
    override fun getRightEdge(offset: Float): Float = rightEdge?.invoke(offset) ?: offset

    /** 滑动时的指示器厚度 */
    override fun getThickness(offset: Float): Float = thickness?.invoke(offset) ?: 1f // 滑动时不会改变厚度
  }
}


/**
 * 定义一个指示器
 * NOTE: 使用 [TabBar.Scope] 限制调用
 * @see TabIndicator
 */
fun TabBar.Scope.TabIndicator(
  /** 指示器的颜色 */
  color: Color = Color.Unset,
  /**
   * 指示器的渐变效果
   * ```
   * 例子:
   * LinearGradient(arrayOf(currentColors.primary, currentColors.secondary))
   * ```
   * TODO Support other gradient effects
   */
  gradient: LinearGradient? = null,
  /**
   * 指示器的宽度
   * 当为 [LayoutSize.Wrap] 时，包装 Tab 内容视图并取出减去 [View.getPaddingStart], [View.getPaddingEnd] 的宽度
   * 当为 [LayoutSize.Match] 时，将忽略 Tab 视图的 [View.getPaddingStart], [View.getPaddingEnd], 并填满整个标签大小
   */
  width: SizeUnit = LayoutSize.Match,
  /** 指示器的高度/厚度 */
  height: SizeUnit = 4.dp,
  /** 自定义渲染画笔参数 [TabBar.indicatorPaint] */
  updatePaint: (Paint.(start: Float, end: Float, top: Float, bottom: Float, offset: Float) -> Unit)? = null,
  /**
   * 渲染器，用于渲染并显示指示器
   * @see TabIndicatorRenderer.Rectangle
   * @see TabIndicatorRenderer.Rounded
   */
  renderer: TabIndicatorRenderer = TabIndicatorRenderer.Rounded,
) = gradient?.let {
  TabIndicator(
    color = Color.Unset,
    width = width,
    height = height,
    updatePaint = { start, end, top, bottom, offset ->
      val (colors, angle, positions, tile) = it
      updatePaint?.invoke(this, start, end, top, bottom, offset)
      shader = LinearGradient(
        left = start,
        top = top,
        right = end,
        bottom = bottom,
        colors = colors,
        positions = positions,
        angle = angle,
        tile = tile
      )
    },
    renderer = renderer,
    use = true
  )
} ?: TabIndicator(
  color = color.useOrElse { ui.currentColors.background },
  width = width,
  height = height,
  updatePaint = updatePaint,
  renderer = renderer,
  use = true
)