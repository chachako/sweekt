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
 * date: 2020/8/12 4:38 PM
 * github: https://github.com/RinOrz
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
  val uikitViews = arrayOf(
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