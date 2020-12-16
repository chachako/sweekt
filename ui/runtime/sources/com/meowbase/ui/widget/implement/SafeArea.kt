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