@file:Suppress("MemberVisibilityCanBePrivate", "LeakingThis", "Unused")

package com.mars.ui.skeleton

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.mars.ui.*
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.coroutines.CoroutineContext

/*
 * author: 凛
 * date: 2020/9/21 下午6:20
 * github: https://github.com/oh-Rin
 * description: 代表了一个附加于手机显示器上具有用户界面的单一屏幕，类似于 Activity / Fragment
 * onCreate
 * onAppear
 * onStart
 * onResume
 * onPause
 * onDisappear
 * onDestroy
 */
abstract class Skeleton : SkeletonTransfer(),
  Ui.Preview,
  LifecycleOwner,
  ViewModelStoreOwner,
  SavedStateRegistryOwner,
  CoroutineScope {
  /** 持有当前骨骼对象的 [View] 引擎 */
  lateinit var engine: SkeletalEngine

  /**
   * 代表了当前骨架上显示的视图
   * @see uiBody
   */
  lateinit var view: View

  /** 定义了当前骨架的唯一 ID */
  val id: Int get() = view.id

  /**
   * 创建当前骨架整体的 Ui 视图（默认不指定）
   * 重写此属性以指定 Ui, 或者在其他任意时机手动调用 [setUiContent] 来指定 Ui
   *
   * @warn [UIBody] 在成功加载到 [Skeleton] 后会自动设置为 [Ui.Unspecified]
   */
  override var uiBody: UIBody = Ui.Unspecified

  private var _viewModelStore: ViewModelStore? = null
  private val _lifecycleRegistry = LifecycleRegistry(this)
  private val _savedStateRegistryController = SavedStateRegistryController.create(this)

  private val children = hashMapOf<UUID, Skeleton>()

  override val coroutineContext: CoroutineContext
    get() = lifecycleScope.coroutineContext


  /**
   * 代表当前 [Skeleton] 处于内容创建阶段
   * [uiBody] 如果指定，则会在继承类调用 `super.onCreate` 时加载
   */
  @MainThread @CallSuper
  open fun onCreate(savedInstanceState: Bundle?) {
    // 如果 Ui 已经指定，则先添加内容视图
    if (uiBody.isSpecified) {
      setUiContent(uiBody)
    }
    // 释放指定的 Ui
    uiBody = Ui.Unspecified
  }

  /**
   * 代表当前 [Skeleton] 处于可见状态，并且 [uiBody] 已经附加
   *
   * @see [Activity.onStart] [Activity.onResume] 类似状态
   */
  @MainThread
  open fun onAppear() {
  }

  /**
   * 代表当前 [Skeleton] 处于不可见状态
   * NOTE: 例如进入后台、关闭屏幕、界面切换
   *
   * @see [Activity.onPause] [Activity.onStop] 类似状态
   */
  @MainThread
  open fun onDisappear() {
  }

  /**
   * 代表当前 [Skeleton] 准备销毁
   * NOTE: 可以在此阶段进行资源释放，以及进行一些回收工作
   *
   * @see Activity.onDestroy 类似状态
   */
  @MainThread
  open fun onDestroy() {
    _viewModelStore?.clear()
    _viewModelStore = null
  }

  /**
   * 设置 Ui 内容到骨架上
   * @see uiBody
   */
  @MainThread
  open fun setUiContent(ui: UIBody) {
    view = engine.setUiContent(modifier, theme, ui) as View
    view.id = id
  }

  private fun initViewTreeOwners() {
    // Set the view tree owners before setting the content view so that the inflation process
    // and attach listeners will see them already present
    ViewTreeLifecycleOwner.set(view, this)
    ViewTreeViewModelStoreOwner.set(view, this)
    ViewTreeSavedStateRegistryOwner.set(view, this)
  }

  private inline fun allScreen(block: Skeleton.() -> Unit) {
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


  // ViewModel

  fun <T> LiveData<T>.observe(onChanged: (T) -> Unit): Observer<T> =
    observe(owner = this@Skeleton, onChanged = onChanged)
}