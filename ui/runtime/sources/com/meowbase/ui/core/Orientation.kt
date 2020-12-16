package com.meowbase.ui.core

import android.widget.LinearLayout

/*
 * author: 凛
 * date: 2020/8/8 10:27 PM
 * github: https://github.com/RinOrz
 * description: 定义方向
 */
enum class Orientation {
  /** 水平 */
  Horizontal,

  /** 垂直 */
  Vertical,
}

/** [LinearLayout.HORIZONTAL] [LinearLayout.VERTICAL] */
val Orientation.native
  get() = when (this) {
    Orientation.Horizontal -> 0
    Orientation.Vertical -> 1
  }