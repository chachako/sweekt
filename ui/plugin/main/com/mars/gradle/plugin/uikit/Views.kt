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
  TextView("android/widget/TextView", "com/mars/ui/foundation/Text"),
  ImageView("android/widget/ImageView", "com/mars/ui/foundation/Image"),
  LinearLayout("android/widget/LinearLayout", "com/mars/ui/foundation/Linear"),
  FrameLayout("android/widget/FrameLayout", "com/mars/ui/foundation/Stack"),
  ViewGroup("android/widget/ViewGroup", "com/mars/ui/foundation/LayoutScope"),
}