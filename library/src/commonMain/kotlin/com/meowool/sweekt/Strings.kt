@file:Suppress("NOTHING_TO_INLINE", "SpellCheckingInspection")

package com.meowool.sweekt

import kotlin.contracts.contract
import kotlin.jvm.JvmName

/**
 * Converts [any] to [String].
 *
 * @author 凛 (RinOrz)
 */
inline fun String(any: Any?): String = any.toString()

/**
 * Returns this string if it's not null and not empty, otherwise returns the [another].
 *
 * @author 凛 (RinOrz)
 */
inline infix fun String?.or(another: String): String = if (isNullOrEmpty()) another else this

/**
 * Returns itself if this string is not empty, otherwise null.
 *
 * @author 凛 (RinOrz)
 */
inline fun String?.takeIfNotEmpty(): String? {
  contract {
    returnsNotNull() implies (this@takeIfNotEmpty != null)
  }
  return if (isNullOrEmpty()) null else this
}

/**
 * Returns itself if this string is empty, otherwise null.
 *
 * @author 凛 (RinOrz)
 */
inline fun String.takeIfEmpty(): String? = if (isEmpty()) this else null

/**
 * Returns `true` if this string is not `null` and not empty.
 */
inline fun String?.isNotEmpty(): Boolean {
  contract { returns(true) implies (this@isNotEmpty != null) }
  return this != null && length > 0
}

/**
 * Returns this string if it's not `null` and not empty, or the result of calling [defaultValue] function if the
 * string is `null` or empty.
 */
inline fun String?.ifNullOrEmpty(defaultValue: () -> String): String =
  if (isNullOrEmpty()) defaultValue() else this

/**
 * Returns this string if it's `null` or empty, or the result of calling [defaultValue] function if the string is not
 * `null` and not empty.
 */
@JvmName("ifNotNullNotEmpty")
inline fun String?.ifNotEmpty(defaultValue: (String) -> String): String? =
  if (isNotEmpty()) defaultValue(this) else this

/**
 * Returns this string if it's empty, or the result of calling [defaultValue] function if the string is not empty.
 */
inline fun String.ifNotEmpty(defaultValue: (String) -> String): String =
  if (isNotEmpty()) defaultValue(this) else this

/**
 * Returns the string without blanks.
 *
 * @author 凛 (RinOrz)
 */
fun String.removeBlanks(): String = filterNot { it.isWhitespace() }

/**
 * Returns the string without line breaks.
 *
 * @author 凛 (RinOrz)
 */
fun String.removeLineBreaks(): String = replace("\n", "").replace("\r", "").replace("\r\n", "")

/**
 * If this string starts with the given [prefix], returns a copy of this string
 * with the prefix removed. Otherwise, returns this string.
 *
 * @author 凛 (RinOrz)
 */
fun String.removePrefix(prefix: Char): String = if (first() == prefix) substring(1) else this

/**
 * If this string ends with the given [suffix], returns a copy of this string
 * with the suffix removed. Otherwise, returns this string.
 *
 * @author 凛 (RinOrz)
 */
fun String.removeSuffix(suffix: Char): String = if (last() == suffix) substring(0, lastIndex) else this

/**
 * Starting from [offset], splits this char sequence into [destination] with [delimiter].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("splitToStrings")
fun <C: MutableCollection<String>> CharSequence.splitTo(
  destination: C,
  delimiter: Char,
  offset: Int = 0
): C = splitTo(destination, offset) { it == delimiter }

/**
 * Starting from [offset], splits this string into [destination] by [predicate].
 *
 * @author 凛 (RinOrz)
 */
@JvmName("splitToStrings")
inline fun <C: MutableCollection<String>> CharSequence.splitTo(
  destination: C,
  offset: Int = 0,
  predicate: (Char) -> Boolean
):C = destination.apply { forEachSplitBy(predicate, offset, ::add) }