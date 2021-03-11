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

package com.meowbase.ui.core.text

import android.graphics.Typeface

/*
 * author: 凛
 * date: 2020/8/10 11:22 PM
 * github: https://github.com/RinOrz
 * description: 定义字体的粗体斜体风格，这可能在自定义字体后被覆盖？
 */
enum class FontStyle(val real: Int) {
  Normal(Typeface.NORMAL),

  /** 粗体 */
  Bold(Typeface.BOLD),

  /** 斜体 */
  Italic(Typeface.ITALIC),

  /** 粗斜体 */
  BoldItalic(Typeface.BOLD_ITALIC),
}