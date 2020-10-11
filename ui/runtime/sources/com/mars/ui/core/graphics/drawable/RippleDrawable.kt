package com.mars.ui.core.graphics.drawable

import android.graphics.drawable.Drawable
import com.mars.ui.core.graphics.ColorStates

typealias NativeRippleDrawable = android.graphics.drawable.RippleDrawable

/*
 * author: 凛
 * date: 2020/10/2 下午5:25
 * github: https://github.com/oh-Rin
 * description: 为 NativeRippleDrawable 提供涟漪色更新能力
 */
class RippleDrawable(val colorStates: ColorStates, content: Drawable?, mask: Drawable?) :
  NativeRippleDrawable(colorStates.toColorStateList(), content, mask)