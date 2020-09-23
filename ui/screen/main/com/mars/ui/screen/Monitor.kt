@file:Suppress("ViewConstructor", "MemberVisibilityCanBePrivate")

package com.mars.ui.screen

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mars.toolkit.data.matchParent
import com.mars.toolkit.widget.LayoutParams
import com.mars.ui.Ui

/**
 * 一个持有屏幕的实际显示器
 *
 * @author 凛
 * @date 2020/9/23 下午2:06
 * @github https://github.com/oh-Rin
 * @param activity 屏幕附加到的活动
 * @param rootScreen 当前活动的根屏幕
 * @param lifecycleOwner 如果为 null 则需要手动调用所有生命周期
 */
class Monitor(
  activity: Activity,
  val rootScreen: UiScreen,
  private val lifecycleOwner: LifecycleOwner? = activity as? LifecycleOwner,
) : FrameLayout(activity), Ui {

  init {
    id = Id
    tag = Tag
    layoutParams = LayoutParams(matchParent, matchParent)
    rootScreen.activityOrNull = activity
    lifecycleOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {

    })
  }

  fun dispatchCreate(savedInstanceState: Bundle?) {
    rootScreen.onCreate(savedInstanceState)
  }

  companion object {
    const val Tag = "Monitor"
    val Id = generateViewId()
  }
}