package com.meowbase.toolkit.iterations

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


// Iterable

/**
 * Parallel performs the given [action] on each element.
 */
suspend inline fun <T> Iterable<T>.forEachParallel(crossinline action: suspend (T) -> Unit) {
  onEachParallel(action)
}

/**
 * Parallel performs the given [action] on each element and
 * returns the collection itself afterwards.
 */
suspend inline fun <T> Iterable<T>.onEachParallel(crossinline action: suspend (T) -> Unit): List<T> =
  parallelMap { action(it); it }

/**
 * Returns a list containing the results of parallel applying the given [transform] function
 * to each element in the original collection.
 */
suspend inline fun <T, R> Iterable<T>.parallelMap(crossinline transform: suspend (T) -> R): List<R> =
  coroutineScope { map { async(Dispatchers.Default) { transform(it) } }.awaitAll() }


// Array

/**
 * Parallel performs the given [action] on each element.
 */
suspend inline fun <T> Array<T>.forEachParallel(crossinline action: suspend (T) -> Unit) {
  onEachParallel(action)
}

/**
 * Parallel performs the given [action] on each element and
 * returns the collection itself afterwards.
 */
suspend inline fun <T> Array<T>.onEachParallel(crossinline action: suspend (T) -> Unit): List<T> =
  parallelMap { action(it); it }

/**
 * Returns a list containing the results of parallel applying the given [transform] function
 * to each element in the original collection.
 */
suspend inline fun <T, R> Array<T>.parallelMap(crossinline transform: suspend (T) -> R): List<R> =
  coroutineScope { map { async(Dispatchers.Default) { transform(it) } }.awaitAll() }