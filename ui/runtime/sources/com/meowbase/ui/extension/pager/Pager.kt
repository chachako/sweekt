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

@file:Suppress("FunctionName")

package com.meowbase.ui.extension.pager

import com.meowbase.ui.Ui
import com.meowbase.ui.core.Modifier
import com.meowbase.ui.extension.list.implement.ItemDefinition2
import com.meowbase.ui.extension.list.implement.ListDefineScope
import com.meowbase.ui.extension.list.implement.data.DataSource
import com.meowbase.ui.extension.list.implement.data.ItemComparator
import com.meowbase.ui.extension.pager.implement.Adapter
import com.meowbase.ui.extension.pager.implement.Pager
import com.meowbase.ui.widget.With


/**
 * 多类型的分页器
 * 支持拥有多种数据类型 Page 的 [dataSource]
 * @see CommonPager
 */
inline fun <Source : Any> Ui.Pager(
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
inline fun <reified Source : Any> Ui.CommonPager(
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