@file:Suppress("NOTHING_TO_INLINE")

package com.meowbase.toolkit.view

import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.core.view.descendants
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import com.meowbase.toolkit.iterations.reversed


/** 返回当前的子视图树，Same as [ViewGroup.allViews] */
val ViewGroup.subtree: Sequence<View> get() = descendants

/**
 * 遍历视图树
 * @param recursively 遍历时是否包含自身
 */
inline fun ViewGroup.forEach(recursively: Boolean = false, action: (View) -> Unit) {
  onEach(recursively, action)
}

/**
 * 遍历视图树
 *
 * @param recursively 遍历时是否包含自身
 * @return 返回遍历的视图树
 */
inline fun ViewGroup.onEach(recursively: Boolean = false, action: (View) -> Unit): Sequence<View> =
  if (recursively) tree.apply { forEach(action) } else forEach(action).run { descendants }

/**
 *
 * 遍历视图树
 *
 * @param recursively 遍历时是否包含自身
 * @param action 遍历时的操作，并提供每个视图的下标
 */
inline fun ViewGroup.forEachIndexed(
  recursively: Boolean = false,
  action: (index: Int, view: View) -> Unit
) {
  onEachIndexed(recursively, action)
}

/**
 * 遍历视图树
 *
 * @param recursively 遍历时是否包含自身
 * @param action 遍历时的操作，并提供每个视图的下标
 * @return 返回遍历的视图树
 */
inline fun ViewGroup.onEachIndexed(
  recursively: Boolean = false,
  action: (index: Int, view: View) -> Unit
): Sequence<View> = if (recursively) tree.apply { forEachIndexed(action) }
else forEachIndexed(action).run { descendants }

/** 从父布局中返回 [index] 位置的视图，不存在时则为空 */
inline fun ViewGroup.getOrNull(index: Int): View? = getChildAt(index)

/**
 * 遍历当前子视图树, 直到找到符合 [V] 类型的 [View] 并返回
 *
 * @param reverse 决定返回时是返回第一个找到的还是最后一个找到的
 * @return 返回开头或结尾时找到的视图, 由 [reverse] 决定
 */
inline fun <reified V : View> ViewGroup.findByType(reverse: Boolean = false): V? =
  if (reverse) descendants.reversed().filterIsInstance<V>().firstOrNull()
  else descendants.filterIsInstance<V>().firstOrNull()
