@file:[OptIn(ExperimentalTime::class) Suppress("MemberVisibilityCanBePrivate")]

package com.meowbase.ui.animation.core

import android.view.View
import com.meowbase.ui.animation.core.leanback.Animator
import com.meowbase.ui.animation.core.leanback.AnimatorSet
import com.meowbase.ui.animation.definition.PlayMode
import kotlin.time.ExperimentalTime

/**
 * 用于定义动画
 *
 * @author 凛
 * @github https://github.com/RinOrz
 * @date 2020/10/5 - 22:40
 */
interface AnimationDefinition {
  /**
   * 代表这个动画定义类在 [getAnimator] 前
   * 是否需要确保需要动画的 view 已经至少经历过一次 layout
   */
  val needArrange: Boolean get() = false

  /**
   * 返回一个符合当前类定义的动画师实例
   * @param view 代表需要执行动画的视图
   */
  fun getAnimator(view: View): Animator

  /**
   * 添加其他动画定义
   * 这会使用 [AnimationDefinitionManger] 生成一条动画链
   */
  operator fun plus(another: AnimationDefinition): AnimationDefinition =
    AnimationDefinitionManger(this, another)

  /**
   * 添加所有动画的基本参数修饰符
   * 这会让已经定义的动画 [AnimationDefinitionManger.definitions] 都使用这些设置
   */
  operator fun plus(modifier: AnimationModifier): AnimationDefinition =
    error("你必须要在动画定义后再定义动画修饰符，`view using animator* + modifier(...)`")
}


/**
 * 用于多个动画定义与其他完整设置
 * @param director 代表由这个 [AnimationDefinition] 发起的动画链生成
 */
class AnimationDefinitionManger(vararg director: AnimationDefinition) : AnimationDefinition {
  private val definitions = mutableListOf(*director)
  private val modifiers = mutableListOf<AnimationModifier>()
  private var sequentially: Boolean = false

  /**
   * 当为 [view] 定义好所有的动画后
   * Motion 将会自动调用此方法来创建 [view] 的每个动画实例
   */
  override fun getAnimator(view: View): Animator = AnimatorSet().apply {
    val animators = definitions.map {
      it.getAnimator(view).apply {
        modifiers.forEach { modifier ->
          modifier.attach(this)
        }
      }
    }
    if (sequentially) {
      playSequentially(animators)
    } else {
      playTogether(animators)
    }
  }

  override fun plus(another: AnimationDefinition) = apply {
    definitions += another
  }

  override fun plus(modifier: AnimationModifier) = apply {
    if (modifier is PlayMode) {
      sequentially = modifier.sequentially
    } else {
      modifiers += modifier
    }
  }
}