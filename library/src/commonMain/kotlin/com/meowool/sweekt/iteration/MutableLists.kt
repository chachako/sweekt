package com.meowool.sweekt.iteration

/**
 * If this list starts with the given [prefix], then removes the prefix and returns list itself.
 */
fun <T, I : MutableList<T>> I.removePrefix(prefix: T): I = apply {
  if (startsWith(prefix)) removeFirst()
}

/**
 * If this list starts with the given [prefix], then removes the prefix and returns list itself.
 */
fun <T, I : MutableList<T>> I.removePrefix(vararg prefix: T): I = apply {
  if (startsWith(*prefix)) removeFirst(prefix.size)
}

/**
 * If this list starts with the given [prefix], then removes the prefix and returns list itself.
 */
fun <T, I : MutableList<T>> I.removePrefix(prefix: Array<out T>): I = apply {
  if (startsWith(prefix)) removeFirst(prefix.size)
}

/**
 * If this list starts with the given [prefix], then removes the prefix and returns list itself.
 */
fun <T, I : MutableList<T>> I.removePrefix(prefix: Iterable<T>): I = apply {
  if (startsWith(prefix)) removeFirst(prefix.size)
}

/**
 * If this list ends with the given [suffix], then removes the suffix and returns list itself.
 */
fun <T, I : MutableList<T>> I.removeSuffix(suffix: T): I = apply {
  if (endsWith(suffix)) removeLast()
}

/**
 * If this list ends with the given [suffix], then removes the suffix and returns list itself.
 */
fun <T, I : MutableList<T>> I.removeSuffix(vararg suffix: T): I = apply {
  if (endsWith(*suffix)) removeLast(suffix.size)
}

/**
 * If this list ends with the given [suffix], then removes the suffix and returns list itself.
 */
fun <T, I : MutableList<T>> I.removeSuffix(suffix: Array<out T>): I = apply {
  if (endsWith(suffix)) removeLast(suffix.size)
}

/**
 * If this list ends with the given [suffix], then removes the suffix and returns list itself.
 */
fun <T, I : MutableList<T>> I.removeSuffix(suffix: Iterable<T>): I = apply {
  if (endsWith(suffix)) removeLast(suffix.size)
}

/**
 * Removes the first [n] elements from this mutable list and return itself.
 */
fun <T, I : MutableList<T>> I.removeLast(n: Int): I = apply {
  subList(this.size - n, this.size).clear()
}

/**
 * Removes the first [n] elements from this mutable list and return itself.
 */
fun <T, I : MutableList<T>> I.removeFirst(n: Int): I = apply {
  subList(0, n).clear()
}

/**
 * Inserts the given [element] at the [index] of the [Iterable] object and returns a new [MutableList].
 */
fun <T, I : MutableList<T>> I.insertAt(index: Int, element: T): I = apply { add(index, element) }

/**
 * Inserts the given all [elements] at the [index] of the [Iterable] object and returns a new [MutableList].
 */
fun <T, I : MutableList<T>> I.insertAt(index: Int, vararg elements: T): I = apply { addAll(index, elements.toList()) }

/**
 * Inserts the given all [elements] at the [index] of the [Iterable] object and returns a new [MutableList].
 */
fun <T, I : MutableList<T>> I.insertAt(index: Int, elements: Array<out T>): I =
  apply { addAll(index, elements.toList()) }

/**
 * Inserts the given all [elements] at the [index] of the [Iterable] object and returns a new [MutableList].
 */
fun <T, I : MutableList<T>> I.insertAt(index: Int, elements: Iterable<T>): I =
  apply { addAll(index, elements.asList()) }

/**
 * Inserts the given [element] at the first of this list object and returns a new [MutableList].
 */
fun <T, I : MutableList<T>> I.insertFirst(element: T): I = apply { add(0, element) }

/**
 * Inserts the given all [elements] at the first of this list object and returns a new [MutableList].
 */
fun <T, I : MutableList<T>> I.insertFirst(vararg elements: T): I = apply { addAll(0, elements.toList()) }

/**
 * Inserts the given all [elements] at the first of this list object and returns a new [MutableList].
 */
fun <T, I : MutableList<T>> I.insertFirst(elements: Array<out T>): I = apply { addAll(0, elements.toList()) }

/**
 * Inserts the given all [elements] at the first of this list object and returns a new [MutableList].
 */
fun <T, I : MutableList<T>> I.insertFirst(elements: Iterable<T>): I = apply { addAll(0, elements.toList()) }

/**
 * Inserts the given [element] at the last of the [Iterable] object and returns a new [MutableList].
 *
 * @see MutableCollection.append
 */
fun <T, I : MutableList<T>> I.insertLast(element: T): I = apply { add(element) }

/**
 * Inserts the given all [elements] at the last of the [Iterable] object and returns a new [MutableList].
 *
 * @see MutableCollection.append
 */
fun <T, I : MutableList<T>> I.insertLast(vararg elements: T): I = apply { addAll(elements) }

/**
 * Inserts the given all [elements] at the last of the [Iterable] object and returns a new [MutableList].
 *
 * @see MutableCollection.append
 */
fun <T, I : MutableList<T>> I.insertLast(elements: Array<out T>): I = apply { addAll(elements) }

/**
 * Inserts the given all [elements] at the last of the [Iterable] object and returns a new [MutableList].
 *
 * @see MutableCollection.append
 */
fun <T, I : MutableList<T>> I.insertLast(elements: Iterable<T>): MutableList<T> = apply { addAll(elements) }