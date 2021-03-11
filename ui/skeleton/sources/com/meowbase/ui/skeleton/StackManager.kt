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

package com.meowbase.ui.skeleton

import kotlin.collections.ArrayDeque


/**
 * 用于管理 Skeleton 栈
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/4 - 15:03
 */
class StackManager {
  /** 储存 [Skeleton] 的回退栈 */
  private val backStack = ArrayDeque<Skeleton>(2)

  /** 返回栈数量 */
  val count: Int get() = backStack.size

  /** 确保栈中只有一个 [Skeleton] */
  val isOnlyOne: Boolean get() = count == 1

  /**
   * 返回当前栈顶的 [Skeleton]
   * 栈顶既是当前屏幕所显示的界面
   */
  val current: Skeleton get() = backStack.first()

  /** 返回上一个界面 */
  val previous: Skeleton? get() = backStack.getOrNull(1)

  /** 将 [to] 推入栈顶 */
  fun push(to: Skeleton) = backStack.addFirst(to)

  /** 从回退栈中弹出栈顶 */
  fun pop() = backStack.removeFirst()
}