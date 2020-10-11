package com.mars.ui.skeleton.owners

import com.mars.ui.skeleton.Skeleton
import com.mars.ui.skeleton.StackManager

/**
 * [Skeleton] 的导航持有者
 *
 * @author 凛
 * @github https://github.com/oh-Rin
 * @date 2020/10/4 - 15:03
 */
interface NavigationOwner : SkeletonOwner {
  /**
   * 将 [to] 推到栈顶
   * 换句话说，从当前界面跳转到 [to]
   *
   * @param withTransition 跳转时播放过渡动画
   */
  fun push(to: Skeleton, withTransition: Boolean = true) {
    // 初始化下一个界面
    to.system = current.system
    to.activityOrNull = current.activity
    to.onCreate(savedInstanceState = null)
    to.onCreated()
    to.onAppear()
    // 暂停当前界面
    when {
      withTransition -> current.onPushChange(to) { current.onDisappear() }
      else -> current.onDisappear()
    }

    // 提交推送结果
    stackManager.push(to)
  }

  /**
   * 弹出 [StackManager] 的栈顶
   * 换句话说，从当前界面返回到 [StackManager.previous]
   *
   * @param withTransition 销毁时播放过渡动画
   */
  fun pop(withTransition: Boolean = true) {
    // 显示上一个界面
    stackManager.previous?.onAppear()

    // 销毁当前界面
    when {
      withTransition -> current.onPopChange(stackManager.previous) {
        current.onDisappear()
        current.onDestroy()
      }
      else -> {
        current.onDisappear()
        current.onDestroy()
      }
    }

    // 提交弹出结果
    stackManager.pop()
  }
}