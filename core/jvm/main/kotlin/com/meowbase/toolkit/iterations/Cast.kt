package com.meowbase.toolkit.iterations

import kotlin.collections.toList
import kotlin.collections.toMutableList
import kotlin.collections.toMutableMap
import kotlin.collections.toMutableSet
import kotlin.collections.toSet

/** [Iterable] safety convert to [MutableList] */
fun <T> Iterable<T>.toMutableList(): MutableList<T> =
  if (this is MutableList) this else toMutableList()

/** [Collection] safety convert to [MutableList] */
fun <T> Collection<T>.toMutableList(): MutableList<T> =
  if (this is MutableList) this else toMutableList()

/** [List] safety convert to [MutableList] */
fun <T> List<T>.toMutableList(): MutableList<T> =
  if (this is MutableList) this else toMutableList()


/** [Iterable] safety convert to [MutableSet] */
fun <T> Iterable<T>.toMutableSet(): MutableSet<T> =
  if (this is MutableSet) this else toMutableSet()

/** [Collection] safety convert to [MutableSet] */
fun <T> Collection<T>.toMutableSet(): MutableSet<T> =
  if (this is MutableSet) this else toMutableSet()

/** [Iterable] safety convert to [Set] */
fun <T> Iterable<T>.toSet(): Set<T> =
  if (this is Set) this else toSet()

/** [Collection] safety convert to [MutableSet] */
fun <T> Collection<T>.toSet(): Set<T> =
  if (this is Set) this else toMutableSet()


/** [Iterable] safety convert to [List] */
fun <T> Iterable<T>.toList(): List<T> =
  if (this is List) this else toList()

/** [Collection] safety convert to [MutableList] */
fun <T> Collection<T>.toList(): List<T> =
  if (this is List) this else toMutableList()


/** [Map] safety convert to [MutableMap] */
fun <K, V> Map<K, V>.toMutableMap(): MutableMap<K, V> =
  if (this is MutableMap<K, V>) this else toMutableMap()