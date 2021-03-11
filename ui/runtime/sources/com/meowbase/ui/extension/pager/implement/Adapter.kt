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

package com.meowbase.ui.extension.pager.implement

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.viewpager2.widget.ViewPager2
import com.meowbase.ui.extension.list.implement.RecyclableAdapter
import com.meowbase.ui.extension.list.implement.ViewHolder
import com.meowbase.ui.extension.list.implement.data.DataSource
import com.meowbase.ui.extension.list.implement.data.ItemComparator

/*
 * author: 凛
 * date: 2020/8/20 1:52 AM
 * github: https://github.com/RinOrz
 * description: 分页适配器
 */
@PublishedApi internal class Adapter<S : Any>(
  /** List 数据源 */
  dataSource: DataSource<S>,
  /** Item 整体（唯一性）差异对比 */
  areItemsTheSame: ItemComparator<S>? = null,
  /** Item 内容差异对比 */
  areContentsTheSame: ItemComparator<S>? = null,
) : RecyclableAdapter<S>(dataSource, areItemsTheSame, areContentsTheSame) {
  /**
   * 对于 [ViewPager2] 我们必须默认内置一个高宽为 [MATCH_PARENT] 的 [LayoutParams]
   * 否则将报: Pages must fill the whole ViewPager2 (use match_parent)
   * @see ViewPager2.enforceChildFillListener
   */
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    checkNotNull(itemDefines[viewType]) {
      """
        出现未知情况！找不到 Item 类型为 $viewType 的对应视图。这是所有已经定义的 Item 类型: {
          ${itemTypes.map { "${it.key} -> ${it.value}" }.joinToString("\n")}
        }
      """
    }.runBlock(parent).apply {
      getItemView().apply {
        @Suppress("UsePropertyAccessSyntax")
        layoutParams ?: setLayoutParams(LayoutParams(MATCH_PARENT, MATCH_PARENT))
      }
    }.viewHolder
}