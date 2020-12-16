package com.meowbase.ui.extension.list.implement.data

/**
 * Item 比较
 * 提供了 old 和 new 实例来验证其相同性
 */
typealias ItemComparator<IT> = (old: IT, new: IT) -> Boolean
