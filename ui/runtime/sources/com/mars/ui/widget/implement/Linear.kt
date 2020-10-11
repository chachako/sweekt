@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate",
  "OverridingDeprecatedMember", "NON_EXHAUSTIVE_WHEN", "NAME_SHADOWING"
)

package com.mars.ui.widget.implement

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.forEach
import com.mars.toolkit.float
import com.mars.toolkit.widget.LinearLayoutParams
import com.mars.ui.Theme
import com.mars.ui.Ui
import com.mars.ui.UiKitMarker
import com.mars.ui.core.*
import com.mars.ui.core.decoupling.*
import com.mars.ui.realParent

/*
 * author: 凛
 * date: 2020/8/8 8:30 PM
 * github: https://github.com/oh-Rin
 * description: 线性布局的扩展
 */
@UiKitMarker
open class Linear @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes),
  Theme.User,
  ViewCatcher,
  ViewCanvasProvider,
  LayoutCanvasProvider,
  ForegroundProvider,
  ModifierProvider,
  Ui {
  /** 获取子控件的总宽度/高度 */
  val childrenSize
    get() = children.map {
      if (orientation == HORIZONTAL) {
        it.width
      } else {
        it.height
      }
    }.fold(0) { a, b -> a + b }

  /** 获取子控件的总宽度/高度，需要包含子控件的 Margin 边距大小 */
  val childrenSizeWithMargin
    get() = children.map {
      val lp = it.layoutParams as? MarginLayoutParams
      if (orientation == HORIZONTAL) {
        it.width + (lp?.marginStart ?: 0) + (lp?.marginEnd ?: 0)
      } else {
        it.height + (lp?.topMargin ?: 0) + (lp?.bottomMargin ?: 0)
      }
    }.fold(0) { a, b -> a + b }

  /** 获取自身的剩余空间 */
  val remainingSize
    get() = if (orientation == HORIZONTAL) {
      width - (paddingStart + paddingEnd)
    } else {
      height - (paddingTop + paddingBottom)
    } - childrenSizeWithMargin

  /** 子内容的主轴方向对齐 */
  var mainAxisAlign: MainAxisAlignment = MainAxisAlignment.Start
    set(value) {
      if (field == value) return
      field = value
      value.toGravity(orientation == HORIZONTAL)?.also {
        var gravity = it
        crossAxisAlign.toGravity(orientation == HORIZONTAL)?.apply {
          gravity = gravity or this
        }
        super.setGravity(gravity)
      }
    }

  /** 子内容的交叉轴方向对齐 */
  var crossAxisAlign: CrossAxisAlignment = CrossAxisAlignment.Start
    set(value) {
      if (field == value) return
      field = value
      value.toGravity(orientation == HORIZONTAL)?.also {
        var gravity = it
        mainAxisAlign.toGravity(orientation == HORIZONTAL)?.apply {
          gravity = gravity or this
        }
        super.setGravity(gravity)
      }
    }

  @Deprecated(
    "不应该使用 setGravity 来定义 Mars-Ui 的对齐方式",
    ReplaceWith("请设置 mainAxisAlign 或 crossAxisAlign", "com.mars.ui.foundation.LinearLayout"),
    DeprecationLevel.HIDDEN
  )
  override fun setGravity(gravity: Int) {
    super.setGravity(gravity)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    super.onLayout(changed, l, t, r, b)
    if (!changed) return

    if (crossAxisAlign == CrossAxisAlignment.Stretch) forEach {
      it.layoutParams = it.layoutParams.also { lp ->
        val marginHorizontally = if (lp is MarginLayoutParams) lp.leftMargin + lp.rightMargin else 0
        val marginVertically = if (lp is MarginLayoutParams) lp.topMargin + lp.bottomMargin else 0
        when (orientation) {
          HORIZONTAL -> lp.height = b - t - marginVertically
          VERTICAL -> lp.width = r - l - marginHorizontally
        }
      }
    }

    if (mainAxisAlign == MainAxisAlignment.SpaceAround ||
      mainAxisAlign == MainAxisAlignment.SpaceBetween ||
      mainAxisAlign == MainAxisAlignment.SpaceEvenly
    ) {
      var gapSize = 0
      var begin = 0
      when (mainAxisAlign) {
        MainAxisAlignment.SpaceAround -> {
          /**
           * 例如：
           * ```
           * | 1 || 2 || 3 || 4 |
           *
           * | 为 A
           * || 为 B
           * (A * 2) == B * 1
           * 2A + 3B = 4
           *
           * 所以需要等分剩余空间的四份之一，即为每个间隙的大小
           * ```
           */
          gapSize = remainingSize / childCount
          // 两端的边距是子内容之间的边距的一半
          begin = gapSize / 2
        }
        MainAxisAlignment.SpaceBetween -> {
          /**
           * 例如：
           * ```
           * 1 | 2 | 3 | 4
           *
           * 首尾不需要边距，所以直接抽取剩余空间的三份之一，即为每个间隙的大小
           * ```
           */
          gapSize = remainingSize / (childCount - 1)
        }
        MainAxisAlignment.SpaceEvenly -> {
          /**
           * 例如：
           * ```
           * | 1 | 2 | 3 | 4 |
           *
           * 首尾边距与子内容之间的间隙完全一致，所以直接抽取剩余空间的三份之一，即为每个间隙的大小
           * ```
           */
          gapSize = remainingSize / (childCount + 1)
          begin = gapSize
        }
      }

      var current = begin
      forEach { view ->
        // 移动 view
        view.width.also { width ->
          view.left = current
          view.right = current + width
        }
        current += view.width + gapSize
      }
    }
  }

  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(realParent) }
    }
  }

  /** 调整子控件在此线性布局中的对齐 */
  fun Modifier.align(alignment: Alignment) = +ChildrenModifier(_alignment = alignment)

  /** 调整子控件在此线性布局中的权重 */
  fun Modifier.weight(weight: Number) = +ChildrenModifier(_weight = weight)

  /** 线性布局参数的调整实现 */
  private data class ChildrenModifier(
    val _weight: Number? = null,
    val _alignment: Alignment? = null,
  ) : Modifier {
    override fun View.realize(parent: ViewGroup?) {
      layoutParams = LinearLayoutParams {
        _alignment?.gravity?.also { gravity = it }
        _weight?.float?.also { weight = it }
      }
    }
  }


  // ---------------------------------------------------------------
  // -                  Decoupling Implementation                  -
  // ---------------------------------------------------------------

  override var captured: ArrayDeque<View>? = null
  override var foregroundSupport: Drawable? = null
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
      modifier.apply { realize(realParent) }
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
    if (!intercepted) super.draw(canvas)

    // 执行调用后的回调
    afterDrawCallbacks?.forEach {
      it.invoke(canvas)
    }
  }
}