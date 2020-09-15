@file:Suppress(
  "PARAMETER_NAME_CHANGED_ON_OVERRIDE", "MemberVisibilityCanBePrivate",
  "NAME_SHADOWING"
)

package com.mars.ui.extension.list.impl.data

import com.mars.toolkit.os.isMainLooper
import com.mars.ui.extension.list.RecyclableAdapter

/**
 * 一个能与 [RecyclableAdapter] 高性能联动的数据源
 * 当数据源有改动时都会直接更新 UI（仅更新改动的位置）
 * @see lockListUI 避免轻微改动后不想更新 UI
 */
class DataSource<Type : Any> : ArrayList<Type> {
  constructor() : super()
  constructor(elements: Collection<Type>) : super(elements)

  @PublishedApi internal var adapter: RecyclableAdapter<Type>? = null

  private var changeListeners: MutableList<DataSourceOnChanged<Type>>? = null
  private var locked = false

  /**
   * 锁定列表 UI，避免轻微改动也会更新列表
   * NOTE: 这不会影响 [replaceWith]
   * @see unlockListUI
   */
  fun lockListUI() {
    locked = true
  }

  /**
   * 解锁定列表 UI
   * 后续的任何数据源改动都会更新其数据在 UI 中对应的位置
   * NOTE: 这不会影响 [replaceWith]
   * @see lockListUI
   */
  fun unlockListUI() {
    locked = true
  }

  /**
   * 替换所有数据源并在 Diff 所有数据后快速刷新列表
   * @see RecyclableAdapter.DiffCallback
   * @param newItems 需要替换的数据源
   * @param onCommit 数据与 UI 更新后的回调
   */
  fun replaceWith(newItems: List<Type>, onCommit: Runnable? = null) {
    super.clear()
    super.addAll(newItems)
    adapter?.submitList(
      if (newItems is MutableList) newItems else newItems.toMutableList(),
      onCommit
    )
  }

  /**
   * 替换所有数据源并在 Diff 所有数据后快速刷新列表
   * @see RecyclableAdapter.DiffCallback
   * @param newItems 需要替换的数据源
   * @param onCommit 数据与 UI 更新后的回调
   */
  fun replaceWith(vararg newItems: Type, onCommit: Runnable? = null) {
    val newItems = newItems.toMutableList()
    super.clear()
    super.addAll(newItems)
    adapter?.submitList(newItems, onCommit)
  }

  /** 追加多个 Item [items] 到当前数据源末端 */
  fun add(vararg items: Type) {
    addAll(items)
  }

  /** 追加一个 Item [element] 到当前数据源末端 */
  override fun add(element: Type): Boolean {
    val oldSize = size
    return super.add(element).apply {
      updateListUI { notifyItemChanged(oldSize + 1) }
    }
  }

  /** 追加整个 [items] 列表到当前数据源末端 */
  override fun addAll(items: Collection<Type>): Boolean {
    val startPosition = size + 1
    return super.addAll(items).apply {
      updateListUI {
        notifyItemRangeInserted(startPosition, items.size)
      }
    }
  }

  /** 在 [index] Item 后追加一个新的 Item [element] */
  override fun add(index: Int, element: Type) {
    super.add(index, element)
    updateListUI { notifyItemInserted(index) }
  }

  override fun remove(item: Type): Boolean {
    val index = indexOf(item)
    if (index == -1) return false
    removeAt(index)
    return true
  }

  /** 删除 [index] 下标对应的 Item */
  override fun removeAt(index: Int): Type =
    super.removeAt(index).apply {
      updateListUI { notifyItemRemoved(index) }
    }

  /**
   * 将两个 Item 数据进行交换
   * [left] [right] 下标对应 Item 互换
   * ```
   * 例如: swap(0, 3)
   * { 0, 1, 2, 3 } -> { 3, 1, 2, 0 }
   * ```
   */
  fun swap(left: Int, right: Int) {
    val leftItem = get(left)
    set(left, get(right))
    set(right, leftItem)
    updateListUI {
      notifyItemChanged(left)
      notifyItemChanged(right)
    }
  }

  /**
   * 移动 Item 位置
   * ```
   * 例如: move(0, 3)
   * { 0, 1, 2, 3 } -> { 1, 2, 3, 0 }
   * ```
   */
  fun move(from: Int, to: Int) {
    val item = get(from)
    super.removeAt(from)
    super.add(to, item)
    updateListUI { notifyItemMoved(from, to) }
  }

  /** 清空数据源 */
  override fun clear() {
    super.clear()
    updateListUI()
  }

  /** 当数据源被修改后将会触发回调 */
  fun onChanged(listener: (DataSource<Type>) -> Unit) =
    (changeListeners ?: mutableListOf<DataSourceOnChanged<Type>>().also { changeListeners = it })
      .add(listener)

  /** 数据改变后，刷新列表 UI */
  fun updateListUI(block: RecyclableAdapter<Type>.() -> Unit = { submitList(this@DataSource) }) {
    // 被锁定了则不要更新
    if (locked) return
    check(isMainLooper) { "必须在主 (UI) 线程上进行数据源更新操作" }
    adapter?.apply(block)
    changeListeners?.forEach { it(this@DataSource) }
  }
}

typealias DataSourceOnChanged<T> = (DataSource<T>) -> Unit

/** 根据数组 [items] 构造一个数据源 */
fun <T : Any> dataSourceOf(items: Collection<T>): DataSource<T> = DataSource(items)

/** 根据数组 [items] 构造一个数据源 */
fun <T : Any> dataSourceOf(vararg items: T): DataSource<T> = DataSource(items.toList())

/** 构造一个空壳的数据源 */
fun <T : Any> dataSourceOf(): DataSource<T> = emptyDataSource()

/** 构造一个空壳的数据源 */
fun <T : Any> emptyDataSource(): DataSource<T> = DataSource()


/** 转换已有的数据列表/数组为数据源 */

fun <T : Any> Iterable<T>.toDataSource(): DataSource<T> = dataSourceOf(toList())

fun <T : Any> Collection<T>.toDataSource(): DataSource<T> = dataSourceOf(this)

fun <T : Any> Array<T>.toDataSource(): DataSource<T> = dataSourceOf(*this)