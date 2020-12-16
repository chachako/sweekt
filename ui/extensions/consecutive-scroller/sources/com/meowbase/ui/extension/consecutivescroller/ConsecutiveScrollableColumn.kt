@file:Suppress("MemberVisibilityCanBePrivate", "FunctionName", "UnusedImport")

package com.meowbase.ui.extension.consecutivescroller

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.DIRECTION_BOTTOM
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.DIRECTION_TOP
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout.LayoutParams
import com.meowbase.toolkit.data.matchParent
import com.meowbase.toolkit.data.wrapContent
import com.meowbase.ui.Ui
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.core.CrossAxisAlignment
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.SpringEdgeEffect
import com.meowbase.ui.core.decoupling.*
import com.meowbase.ui.core.unit.SizeUnit
import com.meowbase.ui.core.unit.toIntPxOrNull
import com.meowbase.ui.uiParent
import com.meowbase.ui.widget.Column
import com.meowbase.ui.widget.With
import com.meowbase.ui.widget.modifier.margin as _margin
import com.meowbase.ui.widget.modifier.marginVertical as _marginVertical

/*
 * author: 凛
 * date: 2020/9/20 上午10:55
 * github: https://github.com/RinOrz
 * description: see https://github.com/donkingliang/ConsecutiveScroller
 * todo: Allow modifiers to modify all attributes
 */
@UiKitMarker
open class ConsecutiveScrollableColumn @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
) : ConsecutiveScrollerLayout(context, attrs, defStyleAttr),
  Ui,
  ViewCatcher,
  ViewCanvasProvider,
  LayoutCanvasProvider,
  ModifierProvider {
  private var scrollable: Boolean = true
  private val springManager = SpringEdgeEffect.Manager()

  /** 复用 Modifier 以提高性能 */
  private var childrenModifier: ChildrenModifier = ChildrenModifier()

  init {
    replaceEdgesEffect()
  }

  private fun replaceEdgesEffect() {
    javaClass.getDeclaredField("mEdgeGlowTop")
      .apply { if (!isAccessible) isAccessible = true }
      .set(this, springManager.createEdgeEffect(this, DIRECTION_TOP))

    javaClass.getDeclaredField("mEdgeGlowBottom")
      .apply { if (!isAccessible) isAccessible = true }
      .set(this, springManager.createEdgeEffect(this, DIRECTION_BOTTOM))
  }

  override fun setOverScrollMode(overScrollMode: Int) {
    super.setOverScrollMode(overScrollMode)
    replaceEdgesEffect()
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(ev: MotionEvent?): Boolean =
    if (scrollable) super.onTouchEvent(ev) else true

  /**
   * 更新连续滑动布局的属性
   *
   * @param enabled 允许滚动
   * @param modifier 其他更多的可选调整
   * @param stickyOffset 设置距离顶部一定距离时开始悬停吸顶
   * @param permanentSticky 常驻吸顶
   * 当前吸顶的 view 不会被后续的吸顶 view 推出屏幕，最终会形成多个 view 排列吸附在顶部的效果
   */
  fun update(
    enabled: Boolean = this.scrollable,
    modifier: Modifier = this.modifier,
    stickyOffset: SizeUnit = SizeUnit.Unspecified,
    permanentSticky: Boolean = this.isPermanent,
  ) = also {
    stickyOffset.toIntPxOrNull()?.apply(::setStickyOffset)
    it.isPermanent = permanentSticky
    it.scrollable = enabled
    it.modifier = modifier
  }

  /**
   * 打断 [ConsecutiveScrollerLayout] 的整体滚动
   *
   * [ConsecutiveScrollerLayout] 默认将所有的子控件视作一个整体，
   * 由它统一处理页面的滑动事件，所以它默认会拦截可滑动的子 View 的滑动事件。
   * 如果你希望某个子控件自己处理自己的滑动事件
   * 可以通过此方法来告诉 [ConsecutiveScrollerLayout] 不要拦截它的滑动事件
   * 这样就可以实现在这个 View 内滑动自己的内容，而不会作为 [ConsecutiveScrollerLayout] 的一部分来处理。
   *
   * @param consecutive [LayoutParams.isConsecutive]
   */
  fun Modifier.interruptParentScroll(consecutive: Boolean): Modifier {
    if (this !is ChildrenModifier) +childrenModifier
    childrenModifier.isConsecutive = consecutive
    return childrenModifier
  }

  /**
   * 让子控件在此滑动布局中有吸顶的粘性效果
   *
   * @param enabled 开启/关闭吸顶悬浮
   * @param sink 吸顶下沉模式, see also [LayoutParams.isSink]
   * @param consumed 当值为 true 时，触摸可吸顶的控件将不会触发滑动布局的滑动，反之
   * @see LayoutParams.isSticky
   */
  fun Modifier.sticky(
    enabled: Boolean = false,
    sink: Boolean = false,
    consumed: Boolean = true,
  ): Modifier {
    if (this !is ChildrenModifier) +childrenModifier
    childrenModifier.isSticky = enabled
    childrenModifier.isStickySink = sink
    childrenModifier.isStickyConsumed = consumed
    return childrenModifier
  }

  /**
   * 调整子控件在此滑动布局中的交叉轴对齐
   * @see LayoutParams.Align
   */
  fun Modifier.align(crossAxisAlignment: CrossAxisAlignment): Modifier {
    if (this !is ChildrenModifier) +childrenModifier
    childrenModifier.crossAxisAlignment = crossAxisAlignment
    return childrenModifier
  }

  /** 调整 View 离滑动布局的左右边缘的距离 */
  fun Modifier.marginHorizontal(size: SizeUnit): Modifier {
    if (this !is ChildrenModifier) +childrenModifier
    childrenModifier.marginStart = size
    childrenModifier.marginEnd = size
    return childrenModifier
  }

  /** 单独调整 View 离滑动布局的左右边缘的距离 */
  fun Modifier.marginHorizontal(start: SizeUnit? = null, end: SizeUnit? = null): Modifier {
    if (this !is ChildrenModifier) +childrenModifier
    start?.also { childrenModifier.marginStart = it }
    end?.also { childrenModifier.marginEnd = it }
    return childrenModifier
  }

  /** 帧布局参数的调整实现 [ConsecutiveScrollerLayout.LayoutParams] */
  private class ChildrenModifier(
    var crossAxisAlignment: CrossAxisAlignment? = null,
    var isSticky: Boolean = false,
    var isStickySink: Boolean = false,
    var isStickyConsumed: Boolean = true,
    var isConsecutive: Boolean = true,
    var marginStart: SizeUnit? = null,
    var marginEnd: SizeUnit? = null,
  ) : Modifier {
    override fun View.realize(parent: ViewGroup?) {
      parent as? ConsecutiveScrollerLayout
        ?: error("要设置 [align], 父布局必须是 ConsecutiveScrollerLayout")

      val lp = layoutParams as? LayoutParams
        ?: LayoutParams(layoutParams?.width ?: matchParent, layoutParams?.height ?: wrapContent)

      lp.isConsecutive = isConsecutive
      lp.isSticky = isSticky
      lp.isSink = isStickySink
      lp.isTriggerScroll = !isStickyConsumed

      marginStart?.toIntPxOrNull()?.also { lp.marginStart = it }
      marginEnd?.toIntPxOrNull()?.also { lp.marginEnd = it }

      if (crossAxisAlignment != null) {
        when (val align = crossAxisAlignment!!.toAlign()) {
          null -> {
            // 填满交叉轴
            lp.width = matchParent
            updatePadding(left = 0, right = 0)
          }
          else -> lp.align = align
        }
      }

      layoutParams = lp
    }

    /**
     * 转换为原生 [LayoutParams.Align]
     */
    private fun CrossAxisAlignment.toAlign() = when (this) {
      CrossAxisAlignment.Start -> LayoutParams.Align.LEFT
      CrossAxisAlignment.Center -> LayoutParams.Align.CENTER
      CrossAxisAlignment.End -> LayoutParams.Align.RIGHT
      CrossAxisAlignment.Stretch -> null
    }
  }


  // ---------------------------------------------------------------
  // -                         Hidden Api                          -
  // ---------------------------------------------------------------

  @Deprecated(deprecatedMessage, ReplaceWith(deprecatedReplaceWith), level = DeprecationLevel.ERROR)
  fun Modifier.margin(
    start: SizeUnit? = null,
    top: SizeUnit? = null,
    end: SizeUnit? = null,
    bottom: SizeUnit? = null,
  ) = _margin(start, top, end, bottom)

  @Deprecated(deprecatedMessage, ReplaceWith(deprecatedReplaceWith), level = DeprecationLevel.ERROR)
  fun Modifier.margin(all: SizeUnit) = _margin(all)

  @Deprecated(deprecatedMessage, ReplaceWith(deprecatedReplaceWith), level = DeprecationLevel.ERROR)
  fun Modifier.margin(
    horizontal: SizeUnit? = null,
    vertical: SizeUnit? = null,
  ) = _margin(horizontal, vertical)

  @Deprecated(deprecatedMessage, ReplaceWith(deprecatedReplaceWith), level = DeprecationLevel.ERROR)
  fun Modifier.marginVertical(size: SizeUnit) =
    _marginVertical(size)


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var captured: ArrayDeque<View>? = null
  override var beforeDispatchDrawCallbacks: ArrayDeque<DrawEventWithValue>? = null
  override var afterDispatchDrawCallbacks: ArrayDeque<DrawEvent>? = null
  override var beforeDrawCallbacks: ArrayDeque<DrawEventWithValue>? = null
  override var afterDrawCallbacks: ArrayDeque<DrawEvent>? = null
  override var beforeDrawChildCallbacks: ArrayDeque<DrawChildEventWithValue>? = null
  override var afterDrawChildCallbacks: ArrayDeque<DrawChildEvent>? = null
  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(uiParent) }
    }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    captureChildView(child)
    super.addView(child, index, params)
  }

  public override fun addViewInLayout(
    child: View?,
    index: Int,
    params: ViewGroup.LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    captureChildView(child)
    return super.addViewInLayout(child, index, params, preventRequestLayout)
  }

  override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
    var intercepted: Boolean? = null

    // 执行调用前的回调
    beforeDrawChildCallbacks?.forEach {
      val result = it.invoke(canvas, child, drawingTime)
      if (result != null) intercepted = result
    }

    // 如果上一个回调返回的值为 null 则代表不拦截 super()
    val result = if (intercepted == null) {
      super.drawChild(canvas, child, drawingTime)
    } else {
      intercepted!!
    }

    // 执行调用后的回调
    afterDrawChildCallbacks?.forEach {
      it.invoke(canvas, child, drawingTime)
    }

    return result
  }

  override fun dispatchDraw(canvas: Canvas) {
    var intercepted = false

    // 执行调用前的回调
    beforeDispatchDrawCallbacks?.forEach {
      if (it.invoke(canvas)) intercepted = true
    }

    // 如果上一个回调返回的值为 false 则代表不拦截 super()
    if (!intercepted) super.dispatchDraw(canvas)

    // 执行调用后的回调
    afterDispatchDrawCallbacks?.forEach {
      it.invoke(canvas)
    }
  }

  override fun draw(canvas: Canvas) {
    var intercepted = false

    // 执行调用前的回调
    beforeDrawCallbacks?.forEach {
      if (it.invoke(canvas)) intercepted = true
    }

    // 如果上一个回调返回的值为 false 则代表不拦截 super()
    if (!intercepted) springManager.withSpring(canvas) { super.draw(canvas) }

    // 执行调用后的回调
    afterDrawCallbacks?.forEach {
      it.invoke(canvas)
    }
  }

  companion object {
    private const val deprecatedMessage = "不允许调整垂直的 margin, 因为这不会有任何效果，详情请看: https://github.com/donkingliang/ConsecutiveScroller#关于margin"
    private const val deprecatedReplaceWith = "marginHorizontal(start = horizontal, end = horizontal), marginHorizontal(horizontal)"
  }
}


/**
 * 可连续滚动的列视图 [Ui.Column]
 *
 * @param enabled 允许滚动
 * @param modifier 其他更多的可选调整
 * @param stickyOffset 设置距离顶部一定距离时开始悬停吸顶
 * @param permanentSticky 常驻吸顶
 * 当前吸顶的 view 不会被后续的吸顶 view 推出屏幕，最终会形成多个 view 排列吸附在顶部的效果
 */
fun Ui.ConsecutiveScrollableColumn(
  enabled: Boolean = true,
  modifier: Modifier = Modifier,
  stickyOffset: SizeUnit = SizeUnit.Unspecified,
  permanentSticky: Boolean = false,
  children: ConsecutiveScrollableColumn.() -> Unit
): ConsecutiveScrollableColumn = With(::ConsecutiveScrollableColumn) {
  it.update(enabled, modifier, stickyOffset, permanentSticky)
  it.children()
}