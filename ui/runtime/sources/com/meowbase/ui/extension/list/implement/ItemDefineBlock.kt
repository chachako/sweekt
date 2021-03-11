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

@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.meowbase.ui.extension.list.implement

import android.view.View
import android.view.ViewGroup

interface ItemDefineBlock {
  fun runBlock(parent: ViewGroup): ItemDefinition
}

/**
 * Item 定义块 [ItemDefinition1]
 *
 * 作用：
 * 我们需要一个类来保存泛型 [block], 否则 [RecyclableAdapter] 无法执行未知泛型块
 *
 * @author: 凛
 * @date: 2020/8/21 2:48 PM
 * @github: https://github.com/RinOrz
 */
inline class ItemDefineBlock1<T, V : View>(private val block: ItemDefinition1<T, V>.() -> Unit) :
  ItemDefineBlock {
  override fun runBlock(parent: ViewGroup) = ItemDefinition1<T, V>(parent).apply(block)
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
  override fun runBlock(parent: ViewGroup) = ItemDefinition2<T>(parent).apply(block)
}