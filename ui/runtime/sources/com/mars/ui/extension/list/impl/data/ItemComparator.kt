package com.mars.ui.extension.list.impl.data

/**
 * Item 比较
 * 提供了 old 和 new 实例来验证其相同性
 */
typealias ItemComparator<IT> = (old: IT, new: IT) -> Boolean
