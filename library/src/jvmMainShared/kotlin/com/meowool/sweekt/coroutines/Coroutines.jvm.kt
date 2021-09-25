@file:Suppress("DeferredIsResult")

package com.meowool.sweekt.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Launches a new coroutine without blocking the current thread and returns a reference to the
 * coroutine as a [Job]. The coroutine is cancelled when the resulting job
 * is [cancelled][Job.cancel].
 *
 * This dispatcher is optimized to perform disk or network I/O outside of the main thread. Examples
 * include using the Room component, reading from or writing to files, and running any network
 * operations.
 *
 * @see CoroutineScope.launch for more details
 * @see Dispatchers.IO
 */
fun CoroutineScope.launchIO(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.IO, start, action)

/**
 * Creates a coroutine and returns its future result as an implementation of [Deferred].
 * The running coroutine is cancelled when the resulting deferred is [cancelled][Job.cancel].
 *
 * This dispatcher is optimized to perform disk or network I/O outside of the main thread. Examples
 * include using the Room component, reading from or writing to files, and running any network
 * operations.
 *
 * @see CoroutineScope.async for more details
 * @see Dispatchers.IO
 */
fun <T> CoroutineScope.asyncIO(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = async(Dispatchers.IO, start, block)

/**
 * Calls the specified suspending block with a given coroutine context, suspends until it completes,
 * and returns the result.
 *
 * This dispatcher is optimized to perform disk or network I/O outside of the main thread. Examples
 * include using the Room component, reading from or writing to files, and running any network
 * operations.
 *
 * @see withContext for more details
 */
suspend inline fun <T> withIOContext(noinline block: suspend CoroutineScope.() -> T): T =
  withContext(Dispatchers.IO, block)