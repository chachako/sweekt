package com.meowool.sweekt.iteration

/** [Iterable] safety convert to [Collection] */
fun <T> Iterable<T>.asCollection(): Collection<T> =
  if (this is Collection<T>) this else toList()

/** [Iterable] safety convert to [MutableList] */
fun <T> Iterable<T>.asMutableList(): MutableList<T> =
  if (this is MutableList) this else toMutableList()

/** [Collection] safety convert to [MutableList] */
fun <T> Collection<T>.asMutableList(): MutableList<T> =
  if (this is MutableList) this else toMutableList()

/** [List] safety convert to [MutableList] */
fun <T> List<T>.asMutableList(): MutableList<T> =
  if (this is MutableList) this else toMutableList()

/** [Iterable] safety convert to [MutableSet] */
fun <T> Iterable<T>.asMutableSet(): MutableSet<T> =
  if (this is MutableSet) this else toMutableSet()

/** [Collection] safety convert to [MutableSet] */
fun <T> Collection<T>.asMutableSet(): MutableSet<T> =
  if (this is MutableSet) this else toMutableSet()

/** [Iterable] safety convert to [Set] */
fun <T> Iterable<T>.asSet(): Set<T> =
  if (this is Set) this else toSet()

/** [Collection] safety convert to [MutableSet] */
fun <T> Collection<T>.asSet(): Set<T> =
  if (this is Set) this else toMutableSet()

/** [Iterable] safety convert to [List] */
fun <T> Iterable<T>.asList(): List<T> =
  if (this is List) this else toList()

/** [Collection] safety convert to [MutableList] */
fun <T> Collection<T>.asList(): List<T> =
  if (this is List) this else toMutableList()


/** [Map] safety convert to [MutableMap] */
fun <K, V> Map<K, V>.asMutableMap(): MutableMap<K, V> =
  if (this is MutableMap<K, V>) this else toMutableMap()