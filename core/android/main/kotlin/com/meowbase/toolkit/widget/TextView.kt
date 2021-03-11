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

package com.meowbase.toolkit.widget

import android.content.res.AssetManager
import android.os.Build.VERSION.SDK_INT
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.meowbase.toolkit.NoGetter
import com.meowbase.toolkit.appContext
import com.meowbase.toolkit.graphics.TypefaceName
import com.meowbase.toolkit.graphics.loadTypeface
import com.meowbase.toolkit.noGetter
import java.io.File
import kotlin.DeprecationLevel.HIDDEN

var TextView.textColor: Int
  get() = currentTextColor
  set(value) = setTextColor(value)

var TextView.hintTextColor: Int
  @Deprecated(NoGetter, level = DeprecationLevel.ERROR) get() = noGetter
  set(value) = setHintTextColor(value)

inline var TextView.textResource: Int
  @Deprecated(NoGetter, level = HIDDEN) get() = noGetter
  set(@StringRes value) = setText(value)

var TextView.textAppearance: Int
  @Deprecated(NoGetter, level = HIDDEN) get() = noGetter
  @Suppress("DEPRECATION")
  set(@StyleRes value) = if (SDK_INT < 23) setTextAppearance(context, value)
  else setTextAppearance(value)

inline var TextView.lines: Int
  @Deprecated(NoGetter, level = HIDDEN) get() = noGetter
  set(value) = setLines(value)

/** 将文本内容对齐到视图中心位置 */
fun TextView.alignTextToCenter() {
  textAlignment = View.TEXT_ALIGNMENT_CENTER
  gravity = Gravity.CENTER
}

/** 将文本内容对齐到视图开头位置 */
fun TextView.alignTextToStart() {
  textAlignment = View.TEXT_ALIGNMENT_VIEW_START
  gravity = Gravity.START
}

/** 将文本内容对齐到视图结尾位置 */
fun TextView.alignTextToEnd() {
  textAlignment = View.TEXT_ALIGNMENT_VIEW_END
  gravity = Gravity.END
}

/**
 * 设置字体文件到文本视图中
 * @see loadTypeface
 */
fun TextView.setTypeface(name: TypefaceName, file: File) {
  typeface = loadTypeface(name, file)
}

/**
 * 设置 Assets 中的字体到文本视图中
 * @see loadTypeface
 */
fun TextView.setTypeface(
  name: TypefaceName,
  path: String,
  assets: AssetManager = appContext.assets
) {
  typeface = loadTypeface(name, path, assets)
}

/** 将左边 [TextView] 中的文本字符串复制并覆盖到右边的 [TextView] */
fun TextView.copyTextTo(target: TextView) {
  target.text = text
}