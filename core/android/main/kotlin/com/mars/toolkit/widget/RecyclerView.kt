package com.mars.toolkit.widget

import androidx.recyclerview.widget.RecyclerView

/**
 * 设置或获取是否固定 [RecyclerView] 视图自身大小
 * 忽略掉内部 Item 的增加或减少所带来的 [RecyclerView] 大小变化
 *
 * @see RecyclerView.hasFixedSize
 * @see RecyclerView.setHasFixedSize
 */
inline var RecyclerView.fixedSize: Boolean
  get() = hasFixedSize()
  set(value) = setHasFixedSize(value)