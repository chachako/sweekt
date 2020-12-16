package com.meowbase.ui.core.text

/*
 * author: 凛
 * date: 2020/8/8 7:10 PM
 * github: https://github.com/RinOrz
 * description: 处理文本溢出
 * sample: 溢出文本以 hello meowbase 为例
 */
enum class TextOverflow {
  /** 省略号显示在开头 "...meowbase" */
  Start,

  /** 省略号显示在中间 "hel...ars" */
  Middle,

  /** 省略号显示在结尾 "meowbase..." */
  End,
}