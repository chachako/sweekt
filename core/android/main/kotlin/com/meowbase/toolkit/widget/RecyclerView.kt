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

package com.meowbase.toolkit.widget

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