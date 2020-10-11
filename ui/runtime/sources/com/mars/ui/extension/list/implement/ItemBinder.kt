package com.mars.ui.extension.list.implement

/*
 * author: 凛
 * date: 2020/8/20 5:14 PM
 * github: https://github.com/oh-Rin
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