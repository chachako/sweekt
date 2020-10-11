package com.mars.ui.skeleton

import kotlin.collections.ArrayDeque


/**
 * 用于管理 Skeleton 栈
 *
 * @author 凛
 * @github https://github.com/oh-Rin
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