package com.mars.gradle.plugin.uikit

/*
 * author: 凛
 * date: 2020/8/12 4:38 PM
 * github: https://github.com/oh-Rin
 * description: 一些常量
 */
object Constants {
  /** 需要被替换的系统 View 包名 */
  val systemViews = arrayOf(
    Views.TextView.system,
    Views.ImageView.system,
    Views.LinearLayout.system,
    Views.FrameLayout.system,
    Views.ViewGroup.system,
  )

  /** 替换后的新的 View 包名 */
  val facadeViews = arrayOf(
    Views.TextView.uikit,
    Views.ImageView.uikit,
    Views.LinearLayout.uikit,
    Views.FrameLayout.uikit,
    Views.ViewGroup.uikit,
  )

  /** 替换白名单，因为 UiKit 重定义的 View 是基于这些类，所以不能替换 */
  val whitelist = arrayOf(
    "androidx/appcompat/widget/AppCompatTextView",
    "androidx/appcompat/widget/AppCompatImageView",
  )
}