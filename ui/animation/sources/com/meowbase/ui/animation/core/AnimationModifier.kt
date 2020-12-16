package com.meowbase.ui.animation.core

import android.animation.Animator


/**
 * 动画修饰符
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/8 - 12:38
 */
interface AnimationModifier {
  fun attach(animator: Animator)
}