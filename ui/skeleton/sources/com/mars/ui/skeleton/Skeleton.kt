@file:Suppress("MemberVisibilityCanBePrivate", "LeakingThis", "Unused")

package com.mars.ui.skeleton

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.mars.toolkit.view.gone
import com.mars.toolkit.view.idOrNew
import com.mars.toolkit.view.visible
import com.mars.ui.UIBody
import com.mars.ui.Ui
import com.mars.ui.animation.core.MotionDirection
import com.mars.ui.core.Modifier
import com.mars.ui.core.modify
import com.mars.ui.isSpecified
import com.mars.ui.setUiContent
import com.mars.ui.skeleton.animation.SkeletonTransition
import com.mars.ui.skeleton.owners.NavigationOwner
import com.mars.ui.skeleton.owners.TransitionOwner
import com.mars.ui.widget.modifier.background
import com.mars.ui.widget.modifier.matchParent
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.coroutines.CoroutineContext


/**
 * 骨骼对象可以视为用户界面或行为的一部分
 * 类似于 [Activity] / [Fragment]
 *
 * ```
 * onCreate
 * onAppear
 * onDisappear
 * onDestroy
 * ```
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/10/3 - 22:58
 */
abstract class Skeleton : SkeletonTransfer(),
  Ui.Preview,
  LifecycleOwner,
  ViewModelStoreOwner,
  SavedStateRegistryOwner,
  CoroutineScope,
  NavigationOwner,
  TransitionOwner {
  /** 持有当前骨骼对象的 [View] 引擎 */
  lateinit var system: SkeletalSystem

  /**
   * 代表了当前骨架上显示的视图
   * @see uiBody
   */
  private var _view: View? = null
  val view: View get() = _view!!.apply { idOrNew }

  /** 定义了当前骨架的唯一 ID */
  val id: Int get() = view.id

  /**
   * 创建当前 [Skeleton] 的整体 Ui 视图（默认不指定）
   * 重写此属性以指定 Ui, 或者在其他任意时机手动调用 [setUiContent] 来指定 Ui
   *
   * @warn [UIBody] 在成功加载到 [Skeleton] 后
   * 将会自动重新设置为 [Ui.Unspecified] 以释放内存
   */
  override var uiBody: UIBody = Ui.Unspecified

  private var _viewModelStore: ViewModelStore? = null
  private var _lifecycleRegistry: LifecycleRegistry? = null
  private val _savedStateRegistryController = SavedStateRegistryController.create(this)

  private val children = hashMapOf<UUID, Skeleton>()

  override val coroutineContext: CoroutineContext
    get() = lifecycleScope.coroutineContext

  override var defaultPushTransition: SkeletonTransition? = null
    get() = field ?: SkeletonTransition.Push + MotionDirection.EndToStart

  override var defaultPopTransition: SkeletonTransition? = null
    get() = field ?: SkeletonTransition.Push + MotionDirection.StartToEnd


  /**
   * 代表当前 [Skeleton] 处于内容创建阶段
   * [uiBody] 如果指定，则会在继承类调用 `super.onCreate` 时加载
   *
   * @see savedInstanceState
   */
  @MainThread @CallSuper
  open fun onCreate(savedInstanceState: Bundle?) {
    if (_lifecycleRegistry == null) {
      _lifecycleRegistry = LifecycleRegistry(this)
      _savedStateRegistryController.performRestore(savedInstanceState)
    }
    _lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    // 如果 Ui 已经指定，则先添加内容视图
    attachView()
  }

  /**
   * 代表当前 [Skeleton] 内容已经创建完成
   * @see onCreate
   */
  internal fun onCreated() {
    // 我们需要确保当前界面至少显示一个空白 UI
    if (_view == null) {
      _view = system.setUiContent(
        modifier = modify {
          matchParent()
          background()
          +modifier
        },
        theme = theme
      ) {} as View
    }
    // 如果已经在 onCreate 方法中指定了 Ui 内容则需要释放它
    if (uiBody.isSpecified) uiBody = Ui.Unspecified
  }

  /**
   * 代表当前 [Skeleton] 处于可见状态，并且 [uiBody] 已经附加
   *
   * @see [Activity.onStart] [Activity.onResume] 类似状态
   */
  @MainThread @CallSuper
  open fun onAppear() {
    _lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_START)
    view.visible()
    _lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
  }

  /**
   * 代表当前 [Skeleton] 处于不可见状态
   * NOTE: 例如进入后台、关闭屏幕、界面切换
   *
   * @see [Activity.onPause] [Activity.onStop] 类似状态
   */
  @MainThread @CallSuper
  open fun onDisappear() {
    _lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    view.gone()
    _lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
  }

  /**
   * 代表当前 [Skeleton] 准备销毁
   * NOTE: 可以在此阶段进行资源释放，以及进行一些回收工作
   *
   * @see Activity.onDestroy 类似状态
   */
  @MainThread @CallSuper
  open fun onDestroy() {
    _viewModelStore?.clear()
    _viewModelStore = null
    // 将当前 Ui 内容从屏幕中删除
    system.removeView(view)
    _lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  }

  /**
   * 在 [Skeleton] 销毁或系统调用时保存临时状态数据
   * [更多资料](https://developer.android.com/guide/components/activities/state-changes)
   *
   * @param outState 将需要保存的数据放置在其中
   * @see Activity.onSaveInstanceState
   * @see Fragment.onSaveInstanceState
   */
  @CallSuper
  open fun onSaveInstanceState(outState: Bundle) {
    _lifecycleRegistry!!.currentState = Lifecycle.State.CREATED
    _savedStateRegistryController.performSave(outState)
  }

  /**
   * 代表在当前 [Skeleton] 界面点击了返回按钮
   * @see Activity.onBackPressed 类似状态
   */
  @MainThread
  open fun onBackPressed() {
    // 弹出当前界面
    pop()
  }


  /**
   * 设置 Ui 内容到骨架上
   * @see uiBody
   */
  @MainThread fun setUiContent(ui: UIBody) {
    _view = system.setUiContent(
      modifier = Modifier.background().plus(modifier),
      theme = theme,
      content = ui
    ) as View
  }

  internal fun attachView() {
    if (uiBody.isSpecified) {
      setUiContent(uiBody)
    }
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

  override fun getLifecycle(): Lifecycle = _lifecycleRegistry!!

  override fun getSavedStateRegistry(): SavedStateRegistry =
    _savedStateRegistryController.savedStateRegistry

  override fun getViewModelStore(): ViewModelStore {
    if (_viewModelStore == null) _viewModelStore = ViewModelStore()
    return _viewModelStore!!
  }


  // ViewModel

  fun <T> LiveData<T>.observe(onChanged: (T) -> Unit): Observer<T> =
    observe(owner = this@Skeleton, onChanged = onChanged)
}