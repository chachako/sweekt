@file:Suppress("MemberVisibilityCanBePrivate", "LeakingThis", "Unused")

package com.mars.ui.screen

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.mars.ui.Ui
import com.mars.ui.UiBody
import com.mars.ui.setUiContent
import com.mars.ui.core.Modifier
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.coroutines.CoroutineContext

/*
 * author: 凛
 * date: 2020/9/21 下午6:20
 * github: https://github.com/oh-Rin
 * description: 代表了一个附加于手机显示器上具有用户界面的单一屏幕，类似于 Activity / Fragment
 */
abstract class UiScreen : ScreenTransfer(),
  Ui.Preview,
  LifecycleOwner,
  ViewModelStoreOwner,
  SavedStateRegistryOwner,
  CoroutineScope {
  /** 储存了持有当前屏幕的显示器 [View] */
  lateinit var monitor: Monitor

  /** 储存了当前屏幕显示的根视图 */
  lateinit var root: View

  /** 定义了当前屏幕的唯一 ID */
  val id: UUID = UUID.randomUUID()

  private var _viewModelStore: ViewModelStore? = null
  private val _lifecycleRegistry = LifecycleRegistry(this)
  private val _savedStateRegistryController = SavedStateRegistryController.create(this)

  private val children = hashMapOf<UUID, UiScreen>()

  override val coroutineContext: CoroutineContext
    get() = lifecycleScope.coroutineContext

  /**
   * 创建当前屏幕的 Ui 主视图
   * @see setContent
   */
  open var body: UiBody? = null

  /** [body] 的可选修饰 */
  open val bodyModifier = Modifier

  /** @see body */
  @Deprecated("Please declare body to current UiScreen.", level = DeprecationLevel.HIDDEN)
  override val uiBody: UiBody get() = body!!


  @MainThread @CallSuper
  open fun onCreate(savedInstanceState: Bundle?) {
    allScreen { onCreate(savedInstanceState) }
  }

  /**
   * 代表当前屏幕附加到 [Activity] 后
   */
  open fun onAppear() {
  }

  /** 代表 [root] 被附加到屏幕后 */
  open fun onDisappear() {
    _viewModelStore?.clear()
    _viewModelStore = null
  }

  /**
   * 设置 Ui 内容到屏幕上
   * @see body
   */
  @MainThread
  open fun setContent(uiBody: UiBody) {
    body = uiBody
  }

  internal fun dispatchCreate(container: ViewGroup, savedInstanceState: Bundle?) {
    // 将 Ui 内容附加到屏幕上
    if (body != null) {
      container.setUiContent(this, bodyModifier, body!!)
    }
    container.addView()
  }

  private fun initViewTreeOwners() {
    // Set the view tree owners before setting the content view so that the inflation process
    // and attach listeners will see them already present
    ViewTreeLifecycleOwner.set(root, this)
    ViewTreeViewModelStoreOwner.set(root, this)
    ViewTreeSavedStateRegistryOwner.set(root, this)
  }

  private inline fun allScreen(block: UiScreen.() -> Unit) {
    children.forEach { (_, screen) -> screen.block() }
  }

  override fun getLifecycle(): Lifecycle = _lifecycleRegistry

  override fun getSavedStateRegistry(): SavedStateRegistry =
    _savedStateRegistryController.savedStateRegistry

  override fun getViewModelStore(): ViewModelStore {
    if (_viewModelStore == null)
      _viewModelStore = ViewModelStore()
    return _viewModelStore!!
  }
}