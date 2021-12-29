@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.iteration

/**
 * If this [Iterable] is [Collection] then returns `this`, otherwise converts `this` to [Collection].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.asCollection(): Collection<T> = if (this is Collection<T>) this else toList()

/**
 * If this [Iterable] is [MutableList] then returns `this`, otherwise converts `this` to [MutableList].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.asMutableList(): MutableList<T> = if (this is MutableList) this else toMutableList()

/**
 * If this [Collection] is [MutableList] then returns `this`, otherwise converts `this` to [MutableList].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Collection<T>.asMutableList(): MutableList<T> = if (this is MutableList) this else toOptimizeMutableList()

/**
 * If this [List] is [MutableList] then returns `this`, otherwise converts `this` to [MutableList].
 *
 * @author 凛 (RinOrz)
 */
fun <T> List<T>.asMutableList(): MutableList<T> = if (this is MutableList) this else toOptimizeMutableList()

/**
 * If this [Iterable] is [MutableSet] then returns `this`, otherwise converts `this` to [MutableSet].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.asMutableSet(): MutableSet<T> = if (this is MutableSet) this else toMutableSet()

/**
 * If this [Collection] is [MutableSet] then returns `this`, otherwise converts `this` to [MutableSet].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Collection<T>.asMutableSet(): MutableSet<T> = if (this is MutableSet) this else toMutableSet()

/**
 * If this [Iterable] is [Set] then returns `this`, otherwise converts `this` to [Set].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.asSet(): Set<T> = if (this is Set) this else toSet()

/**
 * If this [Collection] is [Set] then returns `this`, otherwise converts `this` to [Set].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Collection<T>.asSet(): Set<T> = if (this is Set) this else toMutableSet()

/**
 * If this [Iterable] is [List] then returns `this`, otherwise converts `this` to [List].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Iterable<T>.asList(): List<T> = if (this is List) this else toList()

/**
 * If this [Collection] is [List] then returns `this`, otherwise converts `this` to [List].
 *
 * @author 凛 (RinOrz)
 */
fun <T> Collection<T>.asList(): List<T> = if (this is List) this else toOptimizeMutableList()

/**
 * If this [Map] is [MutableMap] then returns `this`, otherwise converts `this` to [MutableMap].
 *
 * @author 凛 (RinOrz)
 */
inline fun <K, V> Map<K, V>.asMutableMap(): MutableMap<K, V> =
  if (this is MutableMap<K, V>) this else toMutableMap()


internal fun <T> Collection<T>.toOptimizeList() = when (size) {
  0 -> emptyList()
  1 -> listOf(this.first())
  else -> toList()
}

internal fun <T> Collection<T>.toOptimizeMutableList() = when (size) {
  1 -> mutableListOf(this.first())
  else -> toMutableList()
}