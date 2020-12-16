package com.meowbase.ui.core.decoupling

import android.view.View
import androidx.annotation.CallSuper

/*
 * author: 凛
 * date: 2020/8/12 2:34 PM
 * github: https://github.com/RinOrz
 * description: View 捕捉器，当所有子 View 添加时都进行捕获处理
 */
interface ViewCatcher {
  /** 捕获所有在范围内添加的 [View] */
  var captured: ArrayDeque<View>?

  // 开始捕获 View
  @CallSuper fun startCapture() {
    captured = ArrayDeque()
  }

  // 结束捕获 View
  @CallSuper fun endCapture() {
    captured = null
  }

  fun captureChildView(child: View?) {
    if (!captured.isNullOrEmpty() && child != null) captured!!.add(child)
  }
}