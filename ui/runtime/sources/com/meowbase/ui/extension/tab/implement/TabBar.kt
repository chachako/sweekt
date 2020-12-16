@file:Suppress(
  "MemberVisibilityCanBePrivate", "ViewConstructor", "ObjectPropertyName",
  "NestedLambdaShadowedImplicitParameter"
)

package com.meowbase.ui.extension.tab.implement

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.meowbase.toolkit.float
import com.meowbase.toolkit.lerp
import com.meowbase.toolkit.view.forEachIndexed
import com.meowbase.toolkit.view.idOrNew
import com.meowbase.toolkit.widget.LinearLayoutParams
import com.meowbase.ui.Ui
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.LayoutSize
import com.meowbase.ui.core.graphics.lerp
import com.meowbase.ui.core.toGravity
import com.meowbase.ui.core.unit.toPx
import com.meowbase.ui.extension.pager.Pager
import com.meowbase.ui.extension.pager.implement.Pager
import com.meowbase.ui.extension.pager.implement.PagerUser
import com.meowbase.ui.widget.implement.FakeLayoutScope
import com.meowbase.ui.widget.implement.Linear
import kotlin.math.abs


/*
 * author: 凛
 * date: 2020/8/19 9:10 PM
 * github: https://github.com/RinOrz
 * reference: https://github.com/ogaclejapan/SmartTabLayout
 * description: 标签栏
 */
open class TabBar(context: Context) : Linear(context), PagerUser {
  @PublishedApi internal lateinit var dividerCreator: Scope.(index: Int) -> View?
  @PublishedApi internal lateinit var indicatorCreator: Scope.(index: Int) -> TabIndicator
  @PublishedApi internal lateinit var indicatorInterpolator: TabIndicator.Interpolator
  @PublishedApi internal lateinit var indicatorAlign: CrossAxisAlignment
  @PublishedApi internal lateinit var dividerAlign: CrossAxisAlignment

  private lateinit var indicators: List<TabIndicator>
  private val parentContainer get() = parent as? TabBarContainer
  private val currentIndicator get() = indicators[position]

  private var dividerIds: MutableList<Int> = mutableListOf()
  private val indicatorPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
  private var lastPosition = 0
  private var position = 0
  private var positionOffset = 0f

  /** 返回所有 tab 视图 */
  val tabs get() = children.filter { !dividerIds.contains(it.id) }.toList()

  /** 返回 tab 的总数量 */
  val tabCount get() = tabs.size

  /** 返回所有分割线视图 */
  val dividers get() = children.filter { dividerIds.contains(it.id) }.toList()

  /** 返回分割线的总数量 */
  val dividerCount get() = dividers.size

  /** 用于与 [Pager] 联动 */
  override var pager: Pager? = null
    set(value) {
      field = value
      // 重新加载标签栏
      value?.also {
        checkNotNull(it.adapter) { "加载标签栏前必须先指定 ViewPager2.Adapter" }

        val itemCount = it.adapter!!.itemCount
        check(itemCount == tabCount) { "Page 数量必须与 Tab 数量一致。页数: $itemCount, 标签数: $tabCount" }

        forEachIndexed { index, tab ->
          // 高亮当前页面的 Tab
          if (index == it.currentItem) {
            tab.isSelected = true
          }
        }

        it.registerOnPageChangeCallback(object : OnPageChangeCallback() {
          private var scrollState = 0

          override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
          ) {
            if (tabCount == 0 || position < 0 || position >= tabCount) return
            parentContainer?.scrollToTab(position, positionOffset)
            onPageChanged(position, positionOffset)
          }

          override fun onPageScrollStateChanged(state: Int) {
            scrollState = state
          }

          override fun onPageSelected(position: Int) {
            if (scrollState == ViewPager2.SCROLL_STATE_IDLE) {
              parentContainer?.scrollToTab(position, 0f)
              onPageChanged(position, 0f)
            }
            forEachIndexed { index, view ->
              view.isSelected = position == index
            }
          }
        })

        loadDividers()
        loadIndicators()
      }
    }

  fun onPageChanged(position: Int, positionOffset: Float) {
    this.position = position
    this.positionOffset = positionOffset
    if (positionOffset == 0f && lastPosition != position) {
      lastPosition = position
    }
    invalidate()
  }

  override fun dispatchDraw(canvas: Canvas) {
    super.dispatchDraw(canvas)
    drawDecoration(canvas)
  }

  private fun drawDecoration(canvas: Canvas) {
    if (tabCount <= 0) return
    // 当前 Tab 的位置信息
    val currentTabX = getTabX(position)
    var currentTabStart = currentTabX.first.float
    var currentTabEnd = currentTabX.second.float

    // 当前 Tab 的样式数据
    var currentIndicatorHeight = currentIndicator.height.toPx()
    var currentIndicatorColor = currentIndicator.color

    // 确保已经开始滑动，且存在下一个 Tab
    if (positionOffset > 0f && position < childCount - 1) {
      // 下一个 Tab 的位置信息
      val nextTabX = getTabX(position + 1)
      val nextTabStart = nextTabX.first
      val nextTabEnd = nextTabX.second

      // 下一个 Tab 的样式数据
      val nextIndicator = indicators[position + 1]
      val nextIndicatorHeight = nextIndicator.height.toPx()
      val nextIndicatorColor = nextIndicator.color

      // 当两个指示器的颜色不同时则需要过渡
      if (currentIndicatorColor != nextIndicatorColor) {
        currentIndicatorColor = lerp(
          start = currentIndicatorColor,
          stop = nextIndicatorColor,
          fraction = positionOffset
        )
      }

      // 当两个指示器的高度不同时则需要过渡
      if (currentIndicatorHeight != nextIndicatorHeight) {
        currentIndicatorHeight = lerp(
          start = currentIndicatorHeight,
          stop = nextIndicatorHeight,
          fraction = positionOffset
        )
      }

      // 计算动画插值器
      val startOffset = indicatorInterpolator.getLeftEdge(positionOffset)
      val endOffset = indicatorInterpolator.getRightEdge(positionOffset)
      val thicknessOffset = indicatorInterpolator.getThickness(positionOffset)

      // 改变当前的指示器数据（用于显示）
      currentTabStart = startOffset * nextTabStart + (1.0f - startOffset) * currentTabStart
      currentTabEnd = endOffset * nextTabEnd + (1.0f - endOffset) * currentTabEnd
      currentIndicatorHeight *= thicknessOffset
    }
    indicatorPaint.color = currentIndicatorColor.argb
    canvas.drawIndicator(
      start = currentTabStart,
      end = currentTabEnd,
      thickness = currentIndicatorHeight,
    )
  }

  private fun Canvas.drawIndicator(
    start: Float,
    end: Float,
    thickness: Float,
  ) {
    // 不显示的时候就没必要画了
    if (end - start <= 0 || thickness == 0f) return

    val indicatorThickness = currentIndicator.height.toPx()
    val top: Float
    val bottom: Float
    when (indicatorAlign) {
      CrossAxisAlignment.Start -> {
        val center = indicatorThickness / 2f
        top = center - thickness / 2f
        bottom = center + thickness / 2f
      }
      CrossAxisAlignment.End -> {
        val center = height - indicatorThickness / 2f
        top = center - thickness / 2f
        bottom = center + thickness / 2f
      }
      CrossAxisAlignment.Center -> {
        val center = height / 2f
        top = center - thickness / 2f
        bottom = center + thickness / 2f
      }
      CrossAxisAlignment.Stretch -> {
        top = paddingTop.float
        bottom = height - paddingBottom.float
      }
    }

    val render = currentIndicator.renderer.render
    when (currentIndicator.width) {
      LayoutSize.Wrap, LayoutSize.Match -> {
        currentIndicator.updatePaint?.invoke(
          indicatorPaint,
          start,
          end,
          top,
          bottom,
          positionOffset
        )
        render(indicatorPaint, start, end, top, bottom, positionOffset)
      }
      else -> {
        val padding = (abs(start - end) - currentIndicator.width.toPx()) / 2f
        currentIndicator.updatePaint?.invoke(
          indicatorPaint,
          start + padding,
          end - padding,
          top,
          bottom,
          positionOffset
        )
        render(indicatorPaint, start + padding, end - padding, top, bottom, positionOffset)
      }
    }
  }

  /** 加载所有分割线并保存对应视图的 id */
  private fun loadDividers() {
    if (!::dividerCreator.isInitialized) return
    var addedCount = 0
    (0 until tabCount - 1).forEach {
      scope.dividerCreator(it)?.also { divider ->
        dividerIds.add(divider.idOrNew)
        // 添加到每个视图的后方
        addView(divider, it + 1 + addedCount, divider.LinearLayoutParams {
          // 主轴为垂直方向，交叉轴为水平方向
          dividerAlign.toGravity(true)?.also { gravity = it }
          // 判断是否需要将水平方向的高度填满
          if (dividerAlign == CrossAxisAlignment.Stretch) height = MATCH_PARENT
        })
      }
      addedCount++
    }
  }

  /**
   * 加载所有标签指示器
   * 将 [tabCount] 与 [indicatorCreator] 转换为指示器
   */
  private fun loadIndicators() {
    indicators = (0..tabCount).map { scope.indicatorCreator(it) }
  }

  /**
   * 返回 [index] 下标的 Tab 视图的 x 轴信息
   * @return [Pair.first] 标签视图的 start 位置
   * @return [Pair.second] 标签视图的 end 位置
   */
  private fun getTabX(index: Int): Pair<Int, Int> {
    val currentTab = tabs[index]
    val currentIndicator = indicators[index]
    // 只有当宽度没有指定或不为填满时，才不忽略 padding
    val usePadding = currentIndicator.width == LayoutSize.Wrap
    val start = if (usePadding) currentTab.left + currentTab.paddingStart else currentTab.left
    val end = if (usePadding) currentTab.right + currentTab.paddingEnd else currentTab.right
    return start to end
  }

  /**
   * 定义一个 [ViewGroup] 区域，以允许在代码块中可以调用其他 [Ui] 小部件
   * 实际上这没有任何用处，只是达到一个约束效果
   */
  class Scope internal constructor(parent: ViewGroup) : FakeLayoutScope(parent) {
    lateinit var ui: Ui

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun addView(child: View?, index: Int, params: LayoutParams?) {}
    override fun addViewInLayout(
      child: View?,
      index: Int,
      params: LayoutParams?,
      preventRequestLayout: Boolean
    ): Boolean = true
  }

  companion object {
    private var _Scope: Scope? = null
    internal val ViewGroup.scope: Scope
      get() = _Scope ?: run {
        _Scope = Scope(this)
        _Scope!!
      }
  }
}
