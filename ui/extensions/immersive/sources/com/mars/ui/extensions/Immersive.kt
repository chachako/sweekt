@file:Suppress("DEPRECATION", "MemberVisibilityCanBePrivate")

package com.mars.ui.extensions

import android.app.Activity
import android.view.View.*
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.mars.toolkit.addFlags
import com.mars.toolkit.content.windowView
import com.mars.toolkit.removeFlags
import com.mars.ui.UiKit.Companion.currentActivity
import com.mars.ui.core.graphics.Color
import com.mars.ui.core.graphics.useOrNull

/*
 * author: 凛
 * date: 2020/9/13 下午7:53
 * github: https://github.com/oh-Rin
 * description: 
 */
object Immersive {
  private const val EdgeToEdge = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
  private const val HideStatusBar = SYSTEM_UI_FLAG_FULLSCREEN or INVISIBLE

  val builder by lazy { ImmersionBar.with(activity) }

  internal var activity: Activity = currentActivity // 可能未初始化，需要手动初始化
  internal val window get() = activity.window
  internal val windowView get() = activity.windowView

  fun init(activity: Activity) {
    this.activity = activity
  }

  /** 单独应用到状态栏 */
  fun applyStatusBar(
    color: Color = Color.Unset,
    brightness: Brightness = Brightness.Unset,
    hide: Boolean? = null,
  ) {
    builder.transparentStatusBar()
//    immerse()
    color.useOrNull()?.argb?.let(window::setStatusBarColor)
    brightness.apply(isStatusBar = true)
    if (hide == true) {
      builder.hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
    } else {
      builder.hideBar(BarHide.FLAG_SHOW_BAR)
    }
    builder.init()
  }

  /** 应用沉浸式效果 */
  fun immerse() {
    windowView.systemUiVisibility = windowView.systemUiVisibility.addFlags(EdgeToEdge or SYSTEM_UI_FLAG_LAYOUT_STABLE)
  }

  /** 取消沉浸式效果 */
  fun cancel() {
    windowView.systemUiVisibility = windowView.systemUiVisibility.removeFlags(EdgeToEdge)
  }
}