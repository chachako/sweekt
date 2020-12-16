@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.ui.skeleton

import android.app.Activity
import android.app.Application
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.view.ContextThemeWrapper
import com.meowbase.toolkit.data.ActivityProvider
import com.meowbase.toolkit.nullPointer as _nullPointer

/*
 * author: 凛
 * date: 2020/9/23 下午4:13
 * github: https://github.com/RinOrz
 * description: 转接了一些桥方法
 */
abstract class SkeletonTransfer : ContextThemeWrapper(), ActivityProvider {
  /** 代表当前屏幕附加到的 [Activity] */
  var activityOrNull: Activity? = null
    set(value) {
      field = value
      if (baseContext == null) attachBaseContext(value)
    }
  val activity: Activity get() = activityOrNull ?: nullPointer("activity")

  /** 返回持有当前屏幕的 [Application] */
  val applicationOrNull: Application? get() = activity.application
  val application: Application get() = applicationOrNull ?: nullPointer("application")

  /** 返回持有当前屏幕的 [Window] */
  val windowOrNull: Window? get() = activity.window
  val window: Window get() = windowOrNull ?: nullPointer("window")

  /** @see [Activity.getWindowManager] */
  val windowManagerOrNull: WindowManager? get() = activity.windowManager
  val windowManager: WindowManager get() = windowManagerOrNull ?: nullPointer("windowManager")

  /** 返回持有当前屏幕的窗口体的根视图 */
  val windowViewOrNull: View? get() = window.decorView
  val windowView: View get() = windowViewOrNull ?: nullPointer("windowView")

  /** 返回持有当前屏幕的窗口体的根布局 */
  val windowLayoutOrNull: ViewGroup? get() = windowViewOrNull as? ViewGroup
  val windowLayout: ViewGroup
    get() = windowLayoutOrNull
      ?: nullPointer(msg = "当前窗口体的根视图不能转换为布局，它的基类是 android.view.View 而不是 android.view.ViewGroup")


  fun nullPointer(property: String? = null, msg: String? = null): Nothing =
    _nullPointer(msg ?: "你无法获取 '$property' 属性，因为它现在是空的, 或者你可以看看 '${property}OrNull' 属性，以更安全的方式去获取。")

  override fun provideActivity(): Activity = activity
}