/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

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