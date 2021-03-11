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

@file:Suppress("FunctionName")

package com.meowbase.ui.widget.implement


import android.content.Context
import android.util.AttributeSet
import com.meowbase.ui.UiKitMarker
import com.meowbase.ui.widget.modifier.safeArea


/*
 * author: 凛
 * date: 2020/9/29 上午4:19
 * github: https://github.com/RinOrz
 * description: 类似于 Flutter 的屏幕安全区域包装实现
 */
@UiKitMarker class SafeArea @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : Box(context, attrs, defStyleAttr, defStyleRes) {
  var protectedLeft = true
  var protectedRight = true
  var protectedTop = true
  var protectedBottom = true

  /** 对区域上锁 */
  fun lockup() = modifier.safeArea(
    top = protectedTop,
    bottom = protectedBottom,
    left = protectedLeft,
    right = protectedRight
  ).apply { realize(null) }
}