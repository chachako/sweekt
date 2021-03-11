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

package com.meowbase.plugin.uikit

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