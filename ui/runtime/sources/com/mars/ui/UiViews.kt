@file:Suppress("LeakingThis", "ViewConstructor")

package com.mars.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mars.ui.Ui.Companion.Id
import com.mars.ui.Ui.Companion.Tag
import com.mars.ui.Ui.Companion._currentContext
import com.mars.ui.foundation.Stack
import com.mars.ui.theme.*

/**
 * UiKit 的核心容器（运行时）
 *
 * @author 凛
 * @date 2020/8/10 12:13 AM
 * @github https://github.com/oh-Rin
 */
@UiKitMarker class UiContainer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
) : Stack(context, attrs), Ui.Container {
  override var colors = LightColors()
  override var typography = Typography()
  override var materials = Materials()
  override var shapes = Shapes()
  override var styles = Styles()
  override var icons = Icons()
  override var buttons = Buttons()

  /** 监听当前 Activity 的生命周期变化 */
  var lifecycleOwner: LifecycleOwner? = null
    set(value) {
      field = value
      value?.enter()
      value?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
          super.onCreate(owner)
          owner.enter()
        }

        override fun onStart(owner: LifecycleOwner) {
          super.onStart(owner)
          owner.enter()
        }

        override fun onResume(owner: LifecycleOwner) {
          super.onResume(owner)
          owner.enter()
        }

        override fun onPause(owner: LifecycleOwner) {
          super.onPause(owner)
          _currentContext = null
        }

        override fun onStop(owner: LifecycleOwner) {
          super.onStop(owner)
          _currentContext = null
        }

        override fun onDestroy(owner: LifecycleOwner) {
          super.onDestroy(owner)
          _currentContext = null
        }
      })
    }

  init {
    tag = Tag
    id = Id
  }

  private fun LifecycleOwner.enter() {
    _currentContext = this as? Context
  }
}

/**
 * 为 [Ui] 提供 IDE 预览功能（IDE 编辑）
 *
 * @author 凛
 * @date 2020/9/15 16:28 PM
 * @github https://github.com/oh-Rin
 * @see UiContainer
 */
open class UiPreview(
  context: Context,
  attrs: AttributeSet?,
  preview: Ui.Preview,
) : FrameLayout(context, attrs), Ui.Container {
  override var colors = LightColors()
  override var typography = Typography()
  override var materials = Materials()
  override var shapes = Shapes()
  override var styles = Styles()
  override var icons = Icons()
  override var buttons = Buttons()

  init {
    _currentContext = context
    currentIdePreview = this
    addView(UiContainer(context).apply(preview.uiBody))
  }

  companion object {
    internal var currentIdePreview: Ui.Container? = null
  }
}