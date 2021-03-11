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

package com.meowbase.ui.util

import android.view.View

/*
 * author: 凛
 * date: 2020/8/11 1:09 PM
 * github: https://github.com/RinOrz
 * description: 得出 View 限定的最大值
 */
internal fun getMaxSize(measureSpec: Int, maxSize: Int?) =
  if (maxSize == null) measureSpec else View.MeasureSpec.getSize(measureSpec).let { size ->
    val heightSize = if (size <= maxSize) size else maxSize
    when (View.MeasureSpec.getMode(measureSpec)) {
      View.MeasureSpec.UNSPECIFIED -> View.MeasureSpec.makeMeasureSpec(
        heightSize, View.MeasureSpec.AT_MOST
      )
      View.MeasureSpec.AT_MOST -> View.MeasureSpec.makeMeasureSpec(
        size.coerceAtMost(heightSize), View.MeasureSpec.AT_MOST
      )
      View.MeasureSpec.EXACTLY -> View.MeasureSpec.makeMeasureSpec(
        size.coerceAtMost(heightSize), View.MeasureSpec.EXACTLY
      )
      else -> measureSpec
    }
  }