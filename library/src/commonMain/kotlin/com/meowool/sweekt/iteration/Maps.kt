@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.iteration

import kotlin.contracts.contract
import kotlin.jvm.JvmName


/**
 * Returns itself if this char sequence is not empty, otherwise null.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <K, V, M: Map<out K, V>> M?.takeIfNotEmpty(): M? {
  contract {
    returnsNotNull() implies (this@takeIfNotEmpty != null)
  }
  return if (isNullOrEmpty()) null else this
}

/**
 * Returns itself if this char sequence is empty, otherwise null.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <K, V, M: Map<out K, V>> M.takeIfEmpty(): M? = if (isEmpty()) this else null

/**
 * Returns a single list of all not null elements yielded from results of [transform] function being invoked on each
 * entry of original map.
 *
 * @see Map.flatMap
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <K, V, R> Map<out K, V>.flatMapNotNull(transform: (Map.Entry<K, V>) -> Iterable<R>?): List<R> {
  return flatMapNotNullTo(ArrayList(), transform)
}

/**
 * Returns a single list of all not null elements yielded from results of [transform] function being invoked on each
 * entry of original map.
 *
 * @see Map.flatMap
 * @author 凛 (https://github.com/RinOrz)
 */
@JvmName("flatMapNotNullSequence")
@OverloadResolutionByLambdaReturnType
inline fun <K, V, R> Map<out K, V>.flatMapNotNull(transform: (Map.Entry<K, V>) -> Sequence<R>?): List<R> {
  return flatMapNotNullTo(ArrayList(), transform)
}

/**
 * Appends all not null elements yielded from results of [transform] function being invoked on each entry of original
 * map, to the given [destination].
 *
 * @see Map.flatMapTo
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun <K, V, R, C : MutableCollection<in R>> Map<out K, V>.flatMapNotNullTo(
  destination: C,
  transform: (Map.Entry<K, V>) -> Iterable<R>?
): C {
  for (element in this) {
    val list = transform(element)
    if (list != null) destination.addAll(list)
  }
  return destination
}

/**
 * Appends all not null elements yielded from results of [transform] function being invoked on each entry of original
 * map, to the given [destination].
 *
 * @see Map.flatMapTo
 * @author 凛 (https://github.com/RinOrz)
 */
@JvmName("flatMapNotNullSequenceTo")
@OverloadResolutionByLambdaReturnType
inline fun <K, V, R, C : MutableCollection<in R>> Map<out K, V>.flatMapNotNullTo(
  destination: C,
  transform: (Map.Entry<K, V>) -> Sequence<R>?
): C {
  for (element in this) {
    val list = transform(element)
    if (list != null) destination.addAll(list)
  }
  return destination
}

/**
 * Appends the string from all the elements separated using [separator] and using the given [prefix] and [postfix]
 * if supplied.
 *
 * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the
 * first [limit] elements will be appended, followed by the [truncated] string (which defaults to "...").
 *
 * For example:
 * ```
 * mapOf("one" to true, "two" to 2).joinTo(StringBuilder(), infix = " -> ", prefix = "[ ", postfix = " ]")
 * ----
 * [ one -> true, two -> 2 ]
 * ```
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <K, V, A : Appendable> Map<K, V>.joinTo(
  buffer: A,
  separator: CharSequence = ", ",
  infix: CharSequence = ": ",
  prefix: CharSequence = "",
  postfix: CharSequence = "",
  limit: Int = -1,
  truncated: CharSequence = "...",
  transform: ((K, V) -> CharSequence)? = null
): A {
  buffer.append(prefix)
  var count = 0
  for (element in this) {
    if (++count > 1) buffer.append(separator)
    if (limit < 0 || count <= limit) {
      buffer.appendKV(infix, element.key, element.value, transform)
    } else break
  }
  if (limit in 0 until count) buffer.append(truncated)
  buffer.append(postfix)
  return buffer
}

/**
 * Creates a string from all the entries separated using [separator] and using the given [prefix] and [postfix]
 * if supplied.
 *
 * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the
 * first [limit] elements will be appended, followed by the [truncated] string (which defaults to "...").
 *
 * For example:
 * ```
 * mapOf("one" to true, "two" to 2).joinToString()
 * ----
 * one: true, two: 2
 * ```
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun <K, V> Map<K, V>.joinToString(
  separator: CharSequence = ", ",
  infix: CharSequence = "=",
  prefix: CharSequence = "",
  postfix: CharSequence = "",
  limit: Int = -1,
  truncated: CharSequence = "...",
  transform: ((K, V) -> CharSequence)? = null
): String = joinTo(
  buffer = StringBuilder(),
  infix = infix,
  separator = separator,
  prefix = prefix,
  postfix = postfix,
  limit = limit,
  truncated = truncated,
  transform = transform
).toString()

private fun <K, V> Appendable.appendKV(
  infix: CharSequence,
  key: K,
  value: V,
  transform: ((K, V) -> CharSequence)?
) {
  fun Any?.get(): CharSequence? = when (this) {
    is CharSequence? -> this
    else -> this.toString()
  }
  when {
    transform != null -> append(transform(key, value))
    else -> append("${key.get()}$infix${value.get()}")
  }
}