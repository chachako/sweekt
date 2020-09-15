package com.mars.ui.core.text

/*
 * author: 凛
 * date: 2020/8/8 7:11 PM
 * github: https://github.com/oh-Rin
 * description: 文本滚动字幕效果
 */
data class TextMarquee(
  /** 开启轮播效果 */
  val enable: Boolean = false,
  /** 播放次数，-1 为无限 */
  val repeat: Int = -1
)