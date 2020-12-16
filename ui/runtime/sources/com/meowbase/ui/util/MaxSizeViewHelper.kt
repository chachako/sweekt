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