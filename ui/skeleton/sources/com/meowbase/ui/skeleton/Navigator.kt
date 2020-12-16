//package com.meowbase.ui.skeleton
//
///**
// * [Skeleton] 的导航器
// *
// * @author 凛
// * @github https://github.com/RinOrz
// * @date 2020/10/4 - 19:57
// */
//object Navigator {
//
//  /**
//   * 将 [to] 推到当前界面上
//   * 并暂停 [current]
//   */
//  fun push(to: Skeleton) {
//    // 添加下一个界面
//    to.system = current.system
//    to.activityOrNull = current.activity
//    to.onCreate(savedInstanceState = null)
//    to.onCreated()
//    // 暂停当前界面
//    current.onDisappear()
//  }
//
//}