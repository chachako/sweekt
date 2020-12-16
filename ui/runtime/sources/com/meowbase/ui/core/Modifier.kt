@file:Suppress("FunctionName", "OverridingDeprecatedMember", "DEPRECATION")

package com.meowbase.ui.core

import android.view.View
import android.view.ViewGroup

/*
 * author: 凛
 * date: 2020/8/8 8:17 PM
 * github: https://github.com/RinOrz
 * description: 对控件的其他额外调整
 */
interface Modifier {
  /**
   * 链子的开头不要做任何事，将其任命为主管，并且后续的修饰符都由交给主管处理
   * @see ModifierManager
   */
  operator fun <T : Modifier> T.unaryPlus(): Modifier = ModifierManager(this)

  operator fun plus(modifier: Modifier?): Modifier = ModifierManager(modifier)

  fun View.realize(parent: ViewGroup?)

  companion object : Modifier {
    override fun View.realize(parent: ViewGroup?) {
      // do nth
    }
  }
}


/**
 * 用于管理所有修饰符并生成任务链
 * @param director 主管，由它来开始分配修饰任务
 */
@PublishedApi internal class ModifierManager(director: Modifier? = null) : Modifier {
  internal val modifiers = ArrayDeque<Modifier>().apply { director?.apply(::add) }

  /**
   * 添加修饰符
   */
  override operator fun <T : Modifier> T.unaryPlus(): Modifier = this@ModifierManager.plus(this)

  override operator fun plus(modifier: Modifier?): Modifier = also {
    if (modifier is ModifierManager) modifiers.addAll(modifier.modifiers)
    else modifier?.also(modifiers::add)
  }

  /**
   * 让所有修饰符开始执行修饰
   */
  override fun View.realize(parent: ViewGroup?) {
    modifiers.forEach { it.apply { realize(parent) } }
  }

  override fun equals(other: Any?): Boolean {
    if (other !is ModifierManager) return false
    if (javaClass != other.javaClass) return false

    if (modifiers.size != other.modifiers.size) return false
    if (!modifiers.containsAll(other.modifiers)) return false
    if (modifiers != other.modifiers) return false

    return true
  }

  override fun hashCode(): Int = modifiers.hashCode()

}


/**
 * 声明这是个可以更新主题颜色的修饰符
 */
interface UpdatableModifier {
  fun View.update(parent: ViewGroup?)
}


/** 以 DSL 形式创建修饰符 */
inline fun modify(block: Modifier.() -> Unit) = Modifier.apply(block)