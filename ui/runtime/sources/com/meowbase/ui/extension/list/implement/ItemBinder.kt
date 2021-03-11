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

package com.meowbase.ui.extension.list.implement

/*
 * author: 凛
 * date: 2020/8/20 5:14 PM
 * github: https://github.com/RinOrz
 * description: Item 绑定区域
 */
class ItemBinder<S, V> internal constructor(
  val itemContent: V,
  private val bindBlock: (ItemBinder<S, V>.(S) -> Unit)? = null,
  private val bindIndexedBlock: (ItemBinder<S, V>.(Int, S) -> Unit)? = null
) {
  /** 由 [RecyclableAdapter.onBindViewHolder] 调用 */
  @Suppress("UNCHECKED_CAST")
  internal fun bind(index: Int, itemData: Any) {
    itemData as S
    bindBlock?.invoke(this, itemData) ?: bindIndexedBlock?.invoke(this, index, itemData)
  }
}