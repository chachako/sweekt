//@file:OptIn(ExperimentalTime::class)
//
//package com.meowbase.ui.animation
//
//import android.view.View
//import androidx.transition.Fade
//import com.meowbase.ui.core.graphics.Color
//import kotlin.time.Duration
//import kotlin.time.ExperimentalTime
//import kotlin.time.minutes
//import kotlin.time.seconds
//
///**
// * 用于简单处理 View 的动画
// *
// * @author 凛
// * @github https://github.com/RinOrz
// * @date 2020/10/5 - 00:35
// */
//class ViewAnimator {
//}
//
//
//fun x() {
//  Motion(delay = 10) {
//    button {
//      scale = 0 to 1
//      fadeIn()
//    }
//    fab {
//      slideInVertically(initial = 80.px)
//    }
//  }
//
//  Motion(delay = 10) {
//    // 以淡入与放大动画来显示圆形按钮
//    button using scale(0, 1, delay = 200) + fadeIn()
//    // 将 fab 按钮从其下方的 80 像素处垂直滑动回 0
//    fab using slideInVertically(initial = 80.px)
//    // 过渡颜色
//    transition(
//      tween = Color.White to Color.Black,
//      duration = 200,
//      easing = Easing.Decelerate
//    ) { box.backgroundColor = it.argb }
//    // 将一个 View 过渡到另一个 View
//    transition(view1 to view2, easing = Easing.Standard)
//  }
//}