package com.meowbase.toolkit.iterations


/**
 * Returns a list with elements in reversed order.
 */
fun <T> Sequence<T>.reversed(): List<T> {
  if (count() <= 1) return toList()
  val list = toMutableList()
  list.reverse()
  return list
}