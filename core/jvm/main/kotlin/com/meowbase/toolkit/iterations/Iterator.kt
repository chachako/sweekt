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