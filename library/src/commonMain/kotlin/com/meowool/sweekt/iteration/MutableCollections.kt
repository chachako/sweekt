package com.meowool.sweekt.iteration


/**
 * Replaces all elements in this collection with the given [newElements].
 *
 * @return a collection that has been replaced with new elements.
 */
fun <T> MutableCollection<in T>.replaceAll(newElements: Iterable<T>): Boolean {
  clear()
  return addAll(newElements)
}

/**
 * Replaces all elements in this collection with the given [newElements].
 *
 * @return a collection that has been replaced with new elements.
 */
fun <T> MutableCollection<in T>.replaceAll(newElements: Sequence<T>): Boolean {
  clear()
  return addAll(newElements)
}

/**
 * Replaces all elements in this collection with the given [newElements].
 *
 * @return a collection that has been replaced with new elements.
 */
fun <T> MutableCollection<in T>.replaceAll(newElements: Array<out T>): Boolean {
  clear()
  return addAll(newElements)
}