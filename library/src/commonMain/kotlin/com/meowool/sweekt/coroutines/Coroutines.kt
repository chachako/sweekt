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
 * Launches a new coroutine without blocking the current thread and returns a reference to the coroutine as a [Job].
 * The coroutine is cancelled when the resulting job is [cancelled][Job.cancel].
 *
 * Use this dispatcher to run a coroutine on the main Android thread. This should be used only for interacting with
 * the UI and performing quick work. Examples include calling suspend functions, running Android UI framework
 * operations, and updating LiveData objects.
 *
 * @see CoroutineScope.launch for more details
 * @see Dispatchers.Main
 */
fun CoroutineScope.launchUI(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.Main, start, action)

/**
 * Launches a new coroutine without blocking the current thread and returns a reference to the coroutine as a [Job].
 * The coroutine is cancelled when the resulting job is [cancelled][Job.cancel].
 *
 * This dispatcher is optimized to perform CPU-intensive work outside the main thread. Example use cases include
 * sorting a list and parsing JSON.
 *
 * @see CoroutineScope.launch for more details
 * @see Dispatchers.Default
 */
fun CoroutineScope.launchDefault(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  action: suspend CoroutineScope.() -> Unit
) = launch(Dispatchers.Default, start, action)

/**
 * Creates a coroutine and returns its future result as an implementation of [Deferred].
 * The running coroutine is cancelled when the resulting deferred is [cancelled][Job.cancel].
 *
 * Use this dispatcher to run a coroutine on the main Android thread. This should be used only for interacting with the
 * UI and performing quick work. Examples include calling suspend functions, running Android UI framework operations,
 * and updating LiveData objects.
 *
 * @see CoroutineScope.async for more details
 * @see Dispatchers.Main
 */
fun <T> CoroutineScope.asyncUI(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = async(Dispatchers.Main, start, block)

/**
 * Creates a coroutine and returns its future result as an implementation of [Deferred]. The running coroutine is
 * cancelled when the resulting deferred is [cancelled][Job.cancel].
 *
 * This dispatcher is optimized to perform CPU-intensive work outside the main thread. Example use cases include
 * sorting a list and parsing JSON.
 *
 * @see CoroutineScope.async for more details
 * @see Dispatchers.Default
 */
fun <T> CoroutineScope.asyncDefault(
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> T
): Deferred<T> = async(Dispatchers.Default, start, block)

/**
 * Calls the specified suspending block with a given coroutine context, suspends until it completes, and returns
 * the result.
 *
 * Use this dispatcher to run a coroutine on the main Android thread. This should be used only for interacting with
 * the UI and performing quick work. Examples include calling suspend functions, running Android UI framework
 * operations, and updating LiveData objects.
 *
 * @see withContext for more details
 */
suspend inline fun <T> withUiContext(noinline block: suspend CoroutineScope.() -> T): T =
  withContext(Dispatchers.Main, block)

/**
 * Calls the specified suspending block with a given coroutine context, suspends until it completes, and returns
 * the result.
 *
 * This dispatcher is optimized to perform CPU-intensive work outside the main thread. Example use cases include
 * sorting a list and parsing JSON.
 *
 * @see withContext for more details
 */
suspend inline fun <T> withDefaultContext(noinline block: suspend CoroutineScope.() -> T): T =
  withContext(Dispatchers.Default, block)
