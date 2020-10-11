package com.mars.gradle.plugin.uikit

/*
 * author: 凛
 * date: 2020/8/12 4:55 PM
 * github: https://github.com/oh-Rin
 * description: 声明系统与 UiKit 重定义后对应的 View 名称
 */
enum class Views(
  // 系统的 View 类名
  val system: String,
  // UiKit 的 View 类名
  val uikit: String
) {
  TextView("android/widget/TextView", "com/mars/ui/widget/implement/Text"),
  ImageView("android/widget/ImageView", "com/mars/ui/widget/implement/Image"),
  LinearLayout("android/widget/LinearLayout", "com/mars/ui/widget/implement/Linear"),
  FrameLayout("android/widget/FrameLayout", "com/mars/ui/widget/implement/Box"),
  ViewGroup("android/view/ViewGroup", "com/mars/ui/widget/implement/LayoutScope"),
}