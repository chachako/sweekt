package com.meowool.sweekt.iteration

import kotlin.jvm.JvmName

/**
 * Appends the given [element] at the end of this set object and returns a new [MutableCollection].
 */
fun <T, S: MutableCollection<T>> S.append(element: T): S = apply { add(element) }

/**
 * Appends the given all [elements] at the end of this set object and returns a new [MutableCollection].
 */
fun <T, S: MutableCollection<T>> S.append(vararg elements: T): S = apply { addAll(elements) }

/**
 * Appends the given all [elements] at the end of this set object and returns a new [MutableCollection].
 */
@JvmName("appendArray")
fun <T, S: MutableCollection<T>> S.append(elements: Array<out T>): S = apply { addAll(elements) }

/**
 * Appends the given all [elements] at the end of this set object and returns a new [MutableCollection].
 */
fun <T, S: MutableCollection<T>> S.append(elements: Iterable<T>): S = apply { addAll(elements) }

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
fun <T> MutableCollection<in T>.replaceAll(vararg newElements: T): Boolean {
  clear()
  return addAll(newElements)
}

/**
 * Replaces all elements in this collection with the given [newElements].
 *
 * @return a collection that has been replaced with new elements.
 */
@JvmName("replaceAllArray")
fun <T> MutableCollection<in T>.replaceAll(newElements: Array<out T>): Boolean {
  clear()
  return addAll(newElements)
}