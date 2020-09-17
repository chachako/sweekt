@file:Suppress(
  "FunctionName", "MemberVisibilityCanBePrivate",
  "OverridingDeprecatedMember", "NON_EXHAUSTIVE_WHEN", "NAME_SHADOWING"
)

package com.mars.ui.foundation

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
import com.mars.ui.UiKit
import com.mars.ui.UiKitMarker
import com.mars.ui.core.*
import com.mars.ui.foundation.modifies.BlurEffect
import com.mars.ui.util.BlurHelper

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
  BlurEffect,
  Foreground,
  ModifierProvider,
  UiKit {
  override var foregroundSupport: Drawable? = null

  override var blurHelper: BlurHelper? = null

  override val captured = mutableListOf<View>()

  private var capture = false

  override var modifier: Modifier = Modifier
    set(value) {
      if (field == value || value == Modifier) return
      field = value
      modifier.apply { realize(parent as? ViewGroup) }
    }

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
      val lp = it.layoutParams as? LayoutParams
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


  override fun startCapture() {
    capture = true
  }

  override fun endCapture() {
    capture = false
    captured.clear()
  }

  @Deprecated(
    "不应该使用 setGravity 来定义 Mars-Ui 的对齐方式",
    ReplaceWith("请设置 mainAxisAlign 或 crossAxisAlign", "com.mars.ui.foundation.LinearLayout")
  )
  override fun setGravity(gravity: Int) {
    super.setGravity(gravity)
  }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    if (capture && child != null) captured.add(child)
    super.addView(child, index, params)
  }

  public override fun addViewInLayout(
    child: View?,
    index: Int,
    params: ViewGroup.LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    if (capture && child != null) captured.add(child)
    return super.addViewInLayout(child, index, params, preventRequestLayout)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    super.onLayout(changed, l, t, r, b)
    if (!changed) return

    if (crossAxisAlign == CrossAxisAlignment.Stretch) forEach {
      it.layoutParams = it.layoutParams.also { lp ->
        when (orientation) {
          HORIZONTAL -> lp.height = b - t
          VERTICAL -> lp.width = r - l
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

  override fun dispatchDraw(canvas: Canvas) {
    blurHelper?.drawBlur(canvas)
    // 渲染途中不要渲染自身和子视图
    if (blurHelper == null || !blurHelper!!.isRendering) super.dispatchDraw(canvas)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    blurHelper?.attach()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    blurHelper?.detach()
  }

  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.apply { update(parent as? ViewGroup) }
    }
  }

  /** 调整子控件在此线性布局中的重心 */
  fun Modifier.gravity(align: Alignment) = +LinearLayoutModifier(_alignment = align)

  /** 调整子控件在此线性布局中的权重 */
  fun Modifier.weight(weight: Number) = +LinearLayoutModifier(_weight = weight)
}

/** 线性布局参数的调整实现 */
private data class LinearLayoutModifier(
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