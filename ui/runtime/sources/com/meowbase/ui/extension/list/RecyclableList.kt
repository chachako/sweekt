@file:Suppress("FunctionName")

package com.meowbase.ui.extension.list

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.core.Orientation
import com.meowbase.ui.core.native
import com.meowbase.ui.extension.list.implement.ItemDefinition2
import com.meowbase.ui.extension.list.implement.ListDefineScope
import com.meowbase.ui.extension.list.implement.RecyclableAdapter
import com.meowbase.ui.extension.list.implement.RecyclableList
import com.meowbase.ui.extension.list.implement.data.DataSource
import com.meowbase.ui.extension.list.implement.data.ItemComparator
import com.meowbase.ui.widget.With


/** 创建一个拥有多种数据类型 Item 的可回收列表 */
inline fun <Source : Any> Ui.RecyclableList(
  /** List 数据源 */
  dataSource: DataSource<Source>,
  /** 列表视图的其他可选修饰 */
  modifier: Modifier = Modifier,
  /** Item 数据整体（唯一性）差异对比 */
  noinline areItemsTheSame: ItemComparator<Source>? = null,
  /** Item 数据内容差异对比 */
  noinline areContentsTheSame: ItemComparator<Source>? = null,
  /** [RecyclerView.getLayoutManager] */
  layoutManager: (Context) -> LayoutManager = ::LinearLayoutManager,
  /** 对于支持方向的 [LayoutManager] 的快速设置 */
  orientation: Orientation = Orientation.Vertical,
  /**
   * 如果你确保整个 [RecyclableList] 的大小不会因为数据变化而变化
   * 则可以设置 [fixedSize] 为 true
   * 避免每次计算大小所带来的性能代价
   *
   * 例如 [fixedSize] 为 false 时：
   * [RecyclableList] 的大小为 [ViewGroup.LayoutParams.WRAP_CONTENT]
   * 且当列表数据变化时列表因为不会占满屏幕，
   * 所以可能会因为数据变化而增加或者减少视图大小，
   * 这样就会导致每次更新数据时重新计算整个视图的大小
   *
   * @see RecyclerView.hasFixedSize
   */
  fixedSize: Boolean = true,
  /** 列表定义域，可定义不同数据类型的 Item */
  defineScope: ListDefineScope<Source>.() -> Unit
): RecyclableList = With(::RecyclableList, modifier) {
  it.layoutManager = layoutManager(it.context).apply {
    when (this) {
      is LinearLayoutManager -> this.orientation = orientation.native
      is GridLayoutManager -> this.orientation = orientation.native
      is StaggeredGridLayoutManager -> this.orientation = orientation.native
    }
  }
  it.setHasFixedSize(fixedSize)
  ListDefineScope(RecyclableAdapter(dataSource, areItemsTheSame, areContentsTheSame)).apply {
    defineScope()
    dataSource.adapter = adapter
    it.adapter = adapter
  }
}


/** 创建一个拥有多种数据类型 Item 的可回收列表 */
inline fun <reified Source : Any> Ui.CommonRecyclableList(
  /** List 数据源 */
  dataSource: DataSource<Source>,
  /** 列表视图的其他可选修饰 */
  modifier: Modifier = Modifier,
  /** Item 数据整体（唯一性）差异对比 */
  noinline areItemsTheSame: ItemComparator<Source>? = null,
  /** Item 数据内容差异对比 */
  noinline areContentsTheSame: ItemComparator<Source>? = null,
  /** [RecyclerView.getLayoutManager] */
  layoutManager: (Context) -> LayoutManager = ::LinearLayoutManager,
  /** 对于支持方向的 [LayoutManager] 的快速设置 */
  orientation: Orientation = Orientation.Vertical,
  /**
   * 如果你确保整个 [RecyclableList] 的大小不会因为数据变化而变化
   * 则可以设置 [fixedSize] 为 true
   * 避免每次计算大小所带来的性能代价
   *
   * 例如 [fixedSize] 为 false 时：
   * [RecyclableList] 的大小为 [ViewGroup.LayoutParams.WRAP_CONTENT]
   * 且当列表数据变化时列表因为不会占满屏幕，
   * 所以可能会因为数据变化而增加或者减少视图大小，
   * 这样就会导致每次更新数据时重新计算整个视图的大小
   *
   * @see RecyclerView.hasFixedSize
   */
  fixedSize: Boolean = true,
  /** 列表定义域 */
  noinline defineScope: ItemDefinition2<Source>.() -> Unit,
): RecyclableList = RecyclableList(
  dataSource,
  modifier,
  areItemsTheSame,
  areContentsTheSame,
  layoutManager,
  orientation,
  fixedSize,
) { DefineItem(defineScope) }