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

@file:Suppress("FunctionName", "EXPERIMENTAL_FEATURE_WARNING")

package com.meowbase.ui.extension.list.implement

import android.view.View
import com.meowbase.ui.UiKitMarker

/**
 * List 定义作用域
 * NOTE: [S] 是整个数据源的类型，如果数据源中有多个不同类型的数据时，那 [S] 应该是 [Any]
 * ```
 * 在多类型列表的情况下，数据源应该是这样的： DataSource<Any>("string", false, 0)
 * 例子：
 * // 定义不同类型的 Item
 * DefineItem<String> { ... } // 文本数据
 * DefineItem<Boolean> { ... } // 布尔值数据
 * DefineItem<Int> { ... } // 整数数据
 * ```
 *
 * @author: 凛
 * @date: 2020/8/20 5:59 PM
 * @github: https://github.com/RinOrz
 */
@UiKitMarker
inline class ListDefineScope<S : Any>(val adapter: RecyclableAdapter<S>) {
  /**
   * 定义不同类型的 Item
   * [T] Item 的具体数据类型
   * [V] Item 视图类型 [ItemDefinition1.content]
   */
  inline fun <reified T, V : View> DefineItem(noinline defineScope: ItemDefinition1<T, V>.() -> Unit) =
    ItemDefineBlock1(defineScope).remember<ItemDefineBlock1<T, V>, T>()


  /**
   * 定义 [T] 类型的数据的 Item
   * @see DefineItem
   */
  inline fun <reified T> DefineItem(noinline defineScope: ItemDefinition2<T>.() -> Unit) =
    ItemDefineBlock2(defineScope).remember<ItemDefineBlock2<T>, T>()


  /** 记录 [T] 类型的数据的 Item 定义和其中的数据 */
  @PublishedApi internal inline fun <D : ItemDefineBlock, reified T> D.remember() = also {
    with(this@ListDefineScope.adapter) {
      // 生成不同的 Item 类型
      val itemType = itemTypes.getOrPut(T::class.java.name) { itemTypes.size + 1 }

      // 根据 ItemType 保存对应定义
      itemDefines[itemType] = it
    }
  }
}