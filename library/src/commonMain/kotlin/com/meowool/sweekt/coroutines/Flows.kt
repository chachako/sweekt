@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.JvmName

/**
 * Switch the operation upstream of the stream to the main thread.
 *
 * Use this dispatcher to run a coroutine on the main Android thread. This should be used only for
 * interacting with the UI and performing quick work. Examples include calling suspend functions,
 * running Android UI framework operations, and updating LiveData objects.
 *
 * @see Dispatchers.Main
 * @author 凛 (RinOrz)
 */
inline fun <T> Flow<T>.flowOnUI(): Flow<T> = this.flowOn(Dispatchers.Main)

/**
 * Switch the operation upstream of the stream to the default thread.
 *
 * This dispatcher is optimized to perform CPU-intensive work outside the main thread. Example
 * use cases include sorting a list and parsing JSON.
 *
 * @see Dispatchers.Default
 * @author 凛 (RinOrz)
 */
inline fun <T> Flow<T>.flowOnDefault(): Flow<T> = this.flowOn(Dispatchers.Default)

/**
 * Returns the number of elements in the flow.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend inline fun <T> Flow<T>.size(): Int = this.count()

/**
 * Returns `true` if all elements match the given [predicate].
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend fun <T> Flow<T>.all(predicate: suspend (T) -> Boolean): Boolean = try {
  collect { value ->
    if (!predicate(value)) {
      throw CancellationException()
    }
  }
  true
} catch (e: CancellationException) {
  false
}

/**
 * Returns `true` if flow has at least one element.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend fun <T> Flow<T>.any(): Boolean = this.firstOrNull() != null

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend fun <T> Flow<T>.any(predicate: suspend (T) -> Boolean): Boolean =
  firstOrNull(predicate) != null

/**
 * Returns `true` if the flow has no elements.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend inline fun <T> Flow<T>.none(): Boolean = this.firstOrNull() == null

/**
 * Returns `true` if no elements in flow match the given [predicate].
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend fun <T> Flow<T>.none(predicate: suspend (T) -> Boolean): Boolean =
  firstOrNull(predicate) == null

/**
 * Returns `true` if the flow was empty, and then cancels flow's collection.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend inline fun <T> Flow<T>.isEmpty(): Boolean = this.none()

/**
 * Returns `true` if the flow is not empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend inline fun <T> Flow<T>.isNotEmpty(): Boolean = this.any()

/**
 * Returns `true` if this nullable flow is either null or empty.
 *
 * The operation is _terminal_.
 *
 * @see isNotEmpty
 * @author 凛 (RinOrz)
 */
suspend inline fun <T> Flow<T>?.isNullOrEmpty(): Boolean {
  contract { returns(false) implies (this@isNullOrEmpty != null) }
  return this == null || this.isEmpty()
}

/**
 * Returns `true` if this nullable flow is either not `null` or not empty.
 *
 * The operation is _terminal_.
 *
 * @see isNullOrEmpty
 * @author 凛 (RinOrz)
 */
@JvmName("isNotNullNotEmpty")
suspend inline fun <T> Flow<T>?.isNotEmpty(): Boolean {
  contract { returns(true) implies (this@isNotEmpty != null) }
  return this?.isNotEmpty() == true
}

/**
 * Call the given [action] when this flow is not `null` and not empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
@JvmName("onNotNullNotEmpty")
suspend inline fun <T> Flow<T>?.onNotEmpty(action: (Flow<T>) -> Unit): Flow<T>? {
  contract {
    returnsNotNull() implies (this@onNotEmpty != null)
    callsInPlace(action, InvocationKind.AT_MOST_ONCE)
  }
  if (this.isNotEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this flow is not empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend inline fun <T> Flow<T>.onNotEmpty(action: (Flow<T>) -> Unit): Flow<T> {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isNotEmpty()) {
    action(this)
  }
  return this
}

/**
 * Call the given [action] when this flow is null or empty.
 *
 * The operation is _terminal_.
 *
 * @author 凛 (RinOrz)
 */
suspend inline fun <T> Flow<T>?.onNullOrEmpty(action: (Flow<T>?) -> Unit): Flow<T>? {
  contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
  if (this.isNullOrEmpty()) {
    action(this)
  }
  return this
}

/**
 * Returns this [Flow] if it's not `null` and the empty flow otherwise.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Flow<T>?.orEmpty() = this ?: emptyFlow()

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * @author 凛 (RinOrz)
 */
suspend fun <T> Flow<T>.contains(predicate: suspend (T) -> Boolean): Boolean = firstOrNull(predicate) != null

/**
 * Returns `true` if at least one element matches the given [predicate].
 *
 * @author 凛 (RinOrz)
 */
suspend fun <T> Flow<T>.has(predicate: suspend (T) -> Boolean): Boolean = firstOrNull(predicate) != null

/**
 * Returns `true` if at least one element equals to the given [element].
 *
 * @author 凛 (RinOrz)
 */
suspend fun <T> Flow<T>.contains(element: T): Boolean = contains { it == element }

/**
 * Returns a flow containing only distinct elements emitted.
 *
 * @author 凛 (RinOrz)
 */
fun <T> Flow<T>.distinct(): Flow<T> = flow {
  val set = mutableSetOf<T>()
  collect { if (set.add(it)) emit(it) }
}

/**
 * Returns a flow containing only elements having distinct keys returned by the given [selector] function.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, K> Flow<T>.distinctBy(crossinline selector: (T) -> K): Flow<T> = flow {
  val keySet = mutableSetOf<K>()
  collect { if (keySet.add(selector(it))) emit(it) }
}