@file:Suppress("FunctionName")

package com.mars.ui.extension.pager

import androidx.viewpager2.widget.ViewPager2
import com.mars.ui.UiKit
import com.mars.ui.core.Modifier
import com.mars.ui.extension.list.impl.ItemDefinition2
import com.mars.ui.extension.list.impl.ListDefineScope
import com.mars.ui.extension.list.impl.data.DataSource
import com.mars.ui.extension.list.impl.data.ItemComparator
import com.mars.ui.extension.pager.impl.Adapter
import com.mars.ui.foundation.With

/*
 * author: 凛
 * date: 2020/8/20 12:27 AM
 * github: https://github.com/oh-Rin
 * description: 分页容器
 */
typealias Pager = ViewPager2


/**
 * 多类型的分页器
 * 支持拥有多种数据类型 Page 的 [dataSource]
 * @see CommonPager
 */
inline fun <Source : Any> UiKit.Pager(
  /** 分页数据源 */
  dataSource: DataSource<Source>,
  /** 对于视图的其他额外调整 */
  modifier: Modifier = Modifier,
  /** Page 数据整体（唯一性）差异对比 */
  noinline arePagesTheSame: ItemComparator<Source>? = null,
  /** Page 数据内容差异对比 */
  noinline areContentsTheSame: ItemComparator<Source>? = null,
  /** 分页定义域，可定义不同数据类型的 PageItem */
  defineScope: ListDefineScope<Source>.() -> Unit,
): Pager = With(::Pager, modifier) {
  ListDefineScope(Adapter(dataSource, arePagesTheSame, areContentsTheSame)).apply {
    defineScope()
    dataSource.adapter = adapter
    it.adapter = adapter
  }
}

/**
 * 普通分页器
 * 确保所有页面的数据都必须是同一个类型 [Source]，
 * 对于不同页面拥有不同类型的数据，请使用 [Pager]
 */
inline fun <reified Source : Any> UiKit.CommonPager(
  /** 分页数据源 */
  dataSource: DataSource<Source>,
  /** 对于视图的其他额外调整 */
  modifier: Modifier = Modifier,
  /** Page 数据整体（唯一性）差异对比 */
  noinline arePagesTheSame: ItemComparator<Source>? = null,
  /** Page 数据内容差异对比 */
  noinline areContentsTheSame: ItemComparator<Source>? = null,
  /** 分页定义域 */
  noinline defineScope: ItemDefinition2<Source>.() -> Unit,
): Pager = Pager(
  dataSource,
  modifier,
  arePagesTheSame,
  areContentsTheSame
) { DefineItem(defineScope) }