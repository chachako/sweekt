package com.mars.ui.core

/*
 * author: 凛
 * date: 2020/8/11 3:09 PM
 * github: https://github.com/oh-Rin
 * description: 定义是以什么为标准的测量
 */
enum class Benchmark {
  /** 以根为单位的测量，例如屏幕宽高 */
  Root,

  /** 以父亲为单位的测量，例如父布局宽高 */
  Parent
}
