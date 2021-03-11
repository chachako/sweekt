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