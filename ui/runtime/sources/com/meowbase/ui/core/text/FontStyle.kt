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