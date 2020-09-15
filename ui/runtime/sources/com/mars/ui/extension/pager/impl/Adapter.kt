@file:Suppress("MemberVisibilityCanBePrivate")

package com.mars.ui.extension.pager.impl

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.viewpager2.widget.ViewPager2
import com.mars.ui.extension.list.RecyclableAdapter
import com.mars.ui.extension.list.ViewHolder
import com.mars.ui.extension.list.impl.data.DataSource
import com.mars.ui.extension.list.impl.data.ItemComparator

/*
 * author: 凛
 * date: 2020/8/20 1:52 AM
 * github: https://github.com/oh-Rin
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
    }.runBlock().apply {
      getItemView().apply {
        @Suppress("UsePropertyAccessSyntax")
        layoutParams ?: setLayoutParams(LayoutParams(MATCH_PARENT, MATCH_PARENT))
      }
    }.viewHolder
}