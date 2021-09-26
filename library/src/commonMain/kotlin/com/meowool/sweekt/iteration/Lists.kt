@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.iteration

import kotlin.jvm.JvmName

/**
 * If this list starts with the given [prefix], returns a list copy of this array with the prefix removed.
 * Otherwise, returns new copy list.
 */
fun <T> List<T>.dropPrefix(prefix: T): List<T> = let {
  if (startsWith(prefix)) dropFirst() else it.toOptimizeList()
}

/**
 * If this list starts with the given [prefix], returns a list copy of this array with the prefix removed.
 * Otherwise, returns new copy list.
 */
fun <T> List<T>.dropPrefix(vararg prefix: T): List<T> = let {
  if (startsWith(*prefix)) dropFirst(prefix.size) else it.toOptimizeList()
}

/**
 * If this list starts with the given [prefix], returns a list copy of this array with the prefix removed.
 * Otherwise, returns new copy list.
 */
@JvmName("dropPrefixArray")
fun <T> List<T>.dropPrefix(prefix: Array<out T>): List<T> = let {
  if (startsWith(prefix)) dropFirst(prefix.size) else it.toOptimizeList()
}

/**
 * If this list starts with the given [prefix], returns a list copy of this array with the prefix removed.
 * Otherwise, returns new copy list.
 */
fun <T> List<T>.dropPrefix(prefix: Iterable<T>): List<T> = let {
  if (startsWith(prefix)) dropFirst(prefix.size) else it.toOptimizeList()
}

/**
 * If this list ends with the given [suffix], returns a list copy of this array with the suffix removed.
 * Otherwise, returns new copy list.
 */
fun <T> List<T>.dropSuffix(suffix: T): List<T> = let {
  if (endsWith(suffix)) dropLast() else it.toOptimizeList()
}

/**
 * If this list ends with the given [suffix], returns a list copy of this array with the suffix removed.
 * Otherwise, returns new copy list.
 */
fun <T> List<T>.dropSuffix(vararg suffix: T): List<T> = let {
  if (endsWith(*suffix)) dropLast(suffix.size) else it.toOptimizeList()
}

/**
 * If this list ends with the given [suffix], returns a list copy of this array with the suffix removed.
 * Otherwise, returns new copy list.
 */
@JvmName("dropSuffixArray")
fun <T> List<T>.dropSuffix(suffix: Array<out T>): List<T> = let {
  if (endsWith(suffix)) dropLast(suffix.size) else it.toOptimizeList()
}

/**
 * If this list ends with the given [suffix], returns a list copy of this array with the suffix removed.
 * Otherwise, returns new copy list.
 */
fun <T> List<T>.dropSuffix(suffix: Iterable<T>): List<T> = let {
  if (endsWith(suffix)) dropLast(suffix.size) else it.toOptimizeList()
}

/**
 * Drops the last element of this [Iterable] and returns a new list.
 */
inline fun <T> List<T>.dropLast(): List<T> = dropLast(1)
