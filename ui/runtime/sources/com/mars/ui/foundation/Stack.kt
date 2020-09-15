@file:Suppress("FunctionName", "OverridingDeprecatedMember")

package com.mars.ui.foundation

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
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
@UiKitMarker open class Stack @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes),
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

  override var modifier: Modifier? = null
    set(value) {
      field = value
      modifier?.realize(this, parent as? ViewGroup)
    }

  override fun startCapture() {
    capture = true
  }

  override fun endCapture() {
    capture = false
    captured.clear()
  }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    if (capture && child != null) captured.add(child)
    super.addView(child, index, params)
  }

  override fun addViewInLayout(
    child: View?,
    index: Int,
    params: ViewGroup.LayoutParams?,
    preventRequestLayout: Boolean
  ): Boolean {
    if (capture && child != null) captured.add(child)
    return super.addViewInLayout(child, index, params, preventRequestLayout)
  }

  override fun updateUiKitTheme() {
    // 更新有用到主题颜色库的调整器
    (modifier as? ModifierManager)?.modifiers?.forEach {
      (it as? UpdatableModifier)?.update(this, parent as? ViewGroup)
    }
  }

  /** 调整子控件在此帧布局中的重心 */
  fun Modifier.gravity(align: Alignment) =
    +StackModifier(alignment = align)
}

/** 帧布局参数的调整实现 [FrameLayout.LayoutParams] */
private data class StackModifier(
  val alignment: Alignment? = null,
) : Modifier {
  override fun realize(myself: View, parent: ViewGroup?) {
    myself.layoutParams = when (val lp = myself.layoutParams) {
      is FrameLayout.LayoutParams -> updateLayoutParams(lp)
      null -> updateLayoutParams(FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
      else -> updateLayoutParams(FrameLayout.LayoutParams(lp))
    }
  }

  fun updateLayoutParams(lp: FrameLayout.LayoutParams) = lp.apply {
    alignment?.gravity?.also { gravity = it }
  }
}


/**
 * 堆叠布局
 * @receiver 自动将帧布局添加进父布局中
 */
inline fun UiKit.Stack(
  modifier: Modifier = Modifier,
  children: Stack.() -> Unit
): Stack = With(::Stack) {
  it.modifier = modifier
  it.children()
}
