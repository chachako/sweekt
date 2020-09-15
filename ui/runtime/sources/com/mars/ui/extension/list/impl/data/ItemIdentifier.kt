package com.mars.ui.extension.list.impl.data

import com.mars.ui.extension.list.RecyclableAdapter

/*
 * author: 凛
 * date: 2020/8/20 7:14 PM
 * github: https://github.com/oh-Rin
 * description: 为数据类增加标识
 */
interface ItemIdentifier {
  /**
   * Item 的唯一 Id
   * @see RecyclableAdapter.hasStableIds
   * @see RecyclableAdapter.DiffCallback.areItemsTheSame
   */
  val identity: Long
}