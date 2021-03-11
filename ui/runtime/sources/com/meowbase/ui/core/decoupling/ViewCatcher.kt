/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

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