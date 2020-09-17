@file:Suppress("FunctionName", "OverridingDeprecatedMember", "DEPRECATION")

package com.mars.ui.core

import android.view.View
import android.view.ViewGroup

/*
 * author: 凛
 * date: 2020/8/8 8:17 PM
 * github: https://github.com/oh-Rin
 * description: 对控件的其他额外调整
 */
interface Modifier {
  /**
   * 链子的开头不要做任何事，将其任命为主管，并且后续的调整器都由交给主管处理
   * @see ModifierManager
   */
  operator fun <T : Modifier> T.unaryPlus(): Modifier = ModifierManager(this)

  fun View.realize(parent: ViewGroup?)

  companion object : Modifier {
    override fun View.realize(parent: ViewGroup?) {
      // do nth
    }
  }
}

/**
 * 用于管理所有调整器并生成任务链
 * @param director 主管，由它来开始分配调整任务
 */
internal class ModifierManager(director: Modifier) : Modifier {
  internal val modifiers = mutableListOf(director)

  /**
   * 添加调整任务
   */
  override fun <T : Modifier> T.unaryPlus(): Modifier = this@ModifierManager.plus(this)

  operator fun plus(modifier: Modifier?): Modifier = also { modifier?.also(modifiers::add) }

  /**
   * 让所有调整器开始执行调整
   */
  override fun View.realize(parent: ViewGroup?) {
    modifiers.forEach { it.apply { realize(parent) } }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ModifierManager

    if (modifiers.size != other.modifiers.size) return false
    if (!modifiers.containsAll(other.modifiers)) return false
    if (modifiers != other.modifiers) return false

    return true
  }

  override fun hashCode(): Int = modifiers.hashCode()

}

/*
 * author: 凛
 * date: 2020/8/18 1:02 PM
 * github: https://github.com/oh-Rin
 * description: 标明这是个可以更新主题颜色的调整器
 */
interface UpdatableModifier {
  fun View.update(parent: ViewGroup?)
}