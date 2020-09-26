@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.mars.ui.extension.list.impl

import android.content.Context
import android.view.View
import com.mars.ui.extension.list.RecyclableAdapter

interface ItemDefineBlock {
  fun runBlock(context: Context): ItemDefinition
}

/**
 * Item 定义块 [ItemDefinition1]
 *
 * 作用：
 * 我们需要一个类来保存泛型 [block], 否则 [RecyclableAdapter] 无法执行未知泛型块
 *
 * @author: 凛
 * @date: 2020/8/21 2:48 PM
 * @github: https://github.com/oh-Rin
 */
inline class ItemDefineBlock1<T, V : View>(private val block: ItemDefinition1<T, V>.() -> Unit) :
  ItemDefineBlock {
  override fun runBlock(context: Context) = ItemDefinition1<T, V>(context).apply(block)
}


/**
 * Item 定义块 [ItemDefinition2]
 *
 * 作用：
 * 我们需要一个类来保存泛型 [block], 否则 [RecyclableAdapter] 无法执行未知泛型块
 *
 * @see ItemDefineBlock1
 */
inline class ItemDefineBlock2<T>(private val block: ItemDefinition2<T>.() -> Unit) :
  ItemDefineBlock {
  override fun runBlock(context: Context) = ItemDefinition2<T>(context).apply(block)
}