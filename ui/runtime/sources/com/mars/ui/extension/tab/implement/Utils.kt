package com.mars.ui.extension.tab.implement

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.ViewCompat

internal object Utils {
  fun getMeasuredWidth(v: View?): Int {
    return v?.measuredWidth ?: 0
  }

  fun getWidth(v: View?): Int {
    return v?.width ?: 0
  }

  fun getWidthWithMargin(v: View?): Int {
    return getWidth(v) + getMarginHorizontally(v)
  }

  fun getStart(v: View?): Int {
    return getStart(v, false)
  }

  fun getStart(v: View?, withoutPadding: Boolean): Int {
    if (v == null) {
      return 0
    }
    return if (isLayoutRtl(v)) {
      if (withoutPadding) v.right - getPaddingStart(v) else v.right
    } else {
      if (withoutPadding) v.left + getPaddingStart(v) else v.left
    }
  }

  fun getEnd(v: View?): Int {
    return getEnd(v, false)
  }

  fun getEnd(v: View?, withoutPadding: Boolean): Int {
    if (v == null) {
      return 0
    }
    return if (isLayoutRtl(v)) {
      if (withoutPadding) v.left + getPaddingEnd(v) else v.left
    } else {
      if (withoutPadding) v.right - getPaddingEnd(v) else v.right
    }
  }

  fun getPaddingStart(v: View?): Int {
    return if (v == null) {
      0
    } else ViewCompat.getPaddingStart(v)
  }

  fun getPaddingEnd(v: View?): Int {
    return if (v == null) {
      0
    } else ViewCompat.getPaddingEnd(v)
  }

  fun getPaddingHorizontally(v: View?): Int {
    return if (v == null) {
      0
    } else v.paddingLeft + v.paddingRight
  }

  fun getMarginStart(v: View?): Int {
    if (v == null) {
      return 0
    }
    val lp = v.layoutParams as MarginLayoutParams
    return MarginLayoutParamsCompat.getMarginStart(lp)
  }

  fun getMarginEnd(v: View?): Int {
    if (v == null) {
      return 0
    }
    val lp = v.layoutParams as MarginLayoutParams
    return MarginLayoutParamsCompat.getMarginEnd(lp)
  }

  fun getMarginHorizontally(v: View?): Int {
    if (v == null) {
      return 0
    }
    val lp = v.layoutParams as MarginLayoutParams
    return MarginLayoutParamsCompat.getMarginStart(lp) + MarginLayoutParamsCompat.getMarginEnd(lp)
  }

  fun isLayoutRtl(v: View?): Boolean {
    return ViewCompat.getLayoutDirection(v!!) == ViewCompat.LAYOUT_DIRECTION_RTL
  }
}