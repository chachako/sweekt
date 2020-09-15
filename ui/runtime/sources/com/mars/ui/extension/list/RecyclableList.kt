@file:Suppress("FunctionName")

package com.mars.ui.extension.list

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mars.ui.UiKit
import com.mars.ui.core.Orientation
import com.mars.ui.core.SpringEdgeEffect
import com.mars.ui.core.native
import com.mars.ui.extension.list.impl.ItemDefinition2
import com.mars.ui.extension.list.impl.ListDefineScope
import com.mars.ui.extension.list.impl.data.DataSource
import com.mars.ui.extension.list.impl.data.ItemComparator
import com.mars.ui.foundation.With


class RecyclableList @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
  private val springManager = SpringEdgeEffect.Manager()

  init {
    edgeEffectFactory = springManager.createFactory()
  }

  override fun draw(canvas: Canvas) {
    springManager.withSpring(canvas) {
      super.draw(canvas)
    }
  }
}

/** 创建一个拥有多种数据类型 Item 的可回收列表 */
inline fun <Source : Any> UiKit.RecyclableList(
  /** List 数据源 */
  dataSource: DataSource<Source>,
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
): RecyclableList = With(::RecyclableList) {
  it.layoutManager = layoutManager(it.context).apply {
    when(this) {
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
inline fun <reified Source : Any> UiKit.CommonRecyclableList(
  /** List 数据源 */
  dataSource: DataSource<Source>,
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
  areItemsTheSame,
  areContentsTheSame,
  layoutManager,
  orientation,
  fixedSize,
) { DefineItem(defineScope) }