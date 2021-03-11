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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowbase.ui.extension.list.implement

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meowbase.ui.extension.list.implement.data.DataSource
import com.meowbase.ui.extension.list.implement.data.ItemComparator
import com.meowbase.ui.extension.list.implement.data.ItemIdentifier

/*
 * author: 凛
 * date: 2020/8/20 6:02 PM
 * github: https://github.com/RinOrz
 * description: 内部的列表适配器
 */
open class RecyclableAdapter<S : Any>(
  /** List 数据源 */
  dataSource: DataSource<S>,
  /** Item 整体（唯一性）差异对比 */
  areItemsTheSame: ItemComparator<S>? = null,
  /** Item 内容差异对比 */
  areContentsTheSame: ItemComparator<S>? = null,
) : ListAdapter<S, ViewHolder>(DiffCallback(areItemsTheSame, areContentsTheSame)) {
  /**
   * 记录根据不同 Item 数据类型所生成的 ItemType，这应该是唯一的
   * 这能让 [RecyclableAdapter] 知道当前 Item 应该显示什么样的视图
   * @see getItemViewType
   */
  val itemTypes = hashMapOf<DataType, ItemType>()

  /**
   * 记录根据不同 Item 类型定义的块
   * @see onBindViewHolder
   */
  val itemDefines = hashMapOf<ItemType, ItemDefineBlock>()

  /**
   * 记录每一个 Item 数据的类型名称
   * [Any.javaClass].[Class.getName]
   */
  var itemClassNames: Array<String>? = null

  init {
    // 初始化数据源
    submitList(dataSource)
  }

  override fun getItemViewType(position: Int): Int {
    // 在返回前先做一次解析，这样应该可以避免一些错误
    if (itemClassNames == null) resolveItems(currentList)
    // 如果列表数据为空则直接返回 0
    val itemClassName = itemClassNames?.get(position) ?: return 0
    return itemTypes[itemClassName]
      ?: error("Item 数据类型 $itemClassName 没有被定义！请在 ListDefineScope 内使用 DefineItem<$itemClassName> { ... } 定义")
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    checkNotNull(itemDefines[viewType]) {
      """
        出现未知情况！找不到 Item 类型为 $viewType 的对应视图。这是所有已经定义的 Item 类型: {
          ${itemTypes.map { "${it.key} -> ${it.value}" }.joinToString("\n")}
        }
      """
    }.runBlock(parent).viewHolder


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.binder?.bind(position, currentList[position])
  }

  override fun getItemId(position: Int): Long {
    val item = currentList[position]
    return if (item is ItemIdentifier) item.identity
    else super.getItemId(position)
  }

  final override fun submitList(list: MutableList<S>?) {
    super.submitList(list)
    // 提交新的数据源后，我们需要将其转换并注入到 itemClassNames
    resolveItems(list)
  }

  final override fun submitList(list: MutableList<S>?, commitCallback: Runnable?) {
    super.submitList(list, commitCallback)
    // 提交新的数据源后，我们需要将其转换并注入到 itemClassNames
    resolveItems(list)
  }

  /**
   * 解析数据源
   *
   * 1.根据 [S] 的实际类型更新 [itemClassNames]
   * (NOTE: 当数据源中的有不同类型的数据时，[S] 应该不是正确的
   * 因为 [S] 可能是个基类，所以我们需要获取真实的类型名称)
   *
   * 2.如果数据源的所有数据均存在 [ItemIdentifier.identity] 则设置 [setHasStableIds]
   */
  private fun resolveItems(list: MutableList<S>?) {
    var hasStableIds = hasStableIds()
    itemClassNames = when {
      list?.isEmpty() == true -> null
      else -> list?.run {
        val mapped = mutableListOf<String>()
        hasStableIds = true
        forEach {
          if (it !is ItemIdentifier) hasStableIds = false
          mapped.add(it.javaClass.name)
        }
        mapped.toTypedArray()
      }
    }
    if (hasStableIds() != hasStableIds) setHasStableIds(hasStableIds)
  }

  @SuppressLint("DiffUtilEquals")
  private class DiffCallback<S : Any>(
    val areItemsTheSame: ItemComparator<S>?,
    val areContentsTheSame: ItemComparator<S>?
  ) : DiffUtil.ItemCallback<S>() {
    /**
     * Item 整体相同性
     * 如果 [S] 继承了 [ItemIdentifier] 接口则直接判断 [ItemIdentifier.identity]
     */
    override fun areItemsTheSame(oldItem: S, newItem: S): Boolean {
      if (oldItem is ItemIdentifier) {
        if (newItem !is ItemIdentifier) return false
        return oldItem.identity == newItem.identity
      }
      return areItemsTheSame?.invoke(
        oldItem,
        newItem
      ) ?: oldItem.javaClass.name == newItem.javaClass.name && oldItem == newItem
    }

    /**
     * Item 内容相同性
     * 如果 [S] 继承了 [ItemIdentifier] 接口则直接判断 [ItemIdentifier.identity]
     */
    override fun areContentsTheSame(oldItem: S, newItem: S): Boolean =
      areContentsTheSame?.invoke(oldItem, newItem) ?: oldItem == newItem
  }
}

class ViewHolder(
  itemView: View,
  val binder: ItemBinder<*, *>?
) : RecyclerView.ViewHolder(itemView)