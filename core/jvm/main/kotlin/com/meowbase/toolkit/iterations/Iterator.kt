@file:Suppress("NewApi")

package com.meowbase.toolkit.iterations

import java.util.stream.Stream
import java.util.stream.StreamSupport

fun <T> Iterator<T>.asIterable(): Iterable<T> = Iterable { this }

fun <T> Iterator<T>.stream(): Stream<T> = asIterable().stream()

fun <T> Iterator<T>.parallelStream(): Stream<T> = asIterable().parallelStream()

fun <T> Iterable<T>.stream(): Stream<T> = StreamSupport.stream(spliterator(), false)

fun <T> Iterable<T>.parallelStream(): Stream<T> = StreamSupport.stream(spliterator(), true)

fun <T> Iterable<T>.isEmpty() = !iterator().hasNext()