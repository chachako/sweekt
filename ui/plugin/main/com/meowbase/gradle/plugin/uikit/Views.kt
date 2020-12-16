package com.meowbase.gradle.plugin.uikit

/*
 * author: 凛
 * date: 2020/8/12 4:55 PM
 * github: https://github.com/RinOrz
 * description: 声明系统与 UiKit 重定义后对应的 View 名称
 */
enum class Views(
  // 系统的 View 类名
  val system: String,
  // UiKit 的 View 类名
  val uikit: String
) {
  TextView("android/widget/TextView", "com/meowbase/ui/widget/implement/Text"),
  ImageView("android/widget/ImageView", "com/meowbase/ui/widget/implement/Image"),
  LinearLayout("android/widget/LinearLayout", "com/meowbase/ui/widget/implement/Linear"),
  FrameLayout("android/widget/FrameLayout", "com/meowbase/ui/widget/implement/Box"),
  ViewGroup("android/view/ViewGroup", "com/meowbase/ui/widget/implement/LayoutScope"),
}