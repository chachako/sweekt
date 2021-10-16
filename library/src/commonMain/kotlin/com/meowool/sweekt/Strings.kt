@file:Suppress("NOTHING_TO_INLINE", "SpellCheckingInspection")

package com.meowool.sweekt

import kotlin.contracts.contract
import kotlin.jvm.JvmName

/**
 * Converts [any] to [String].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun String(any: Any?): String = any.toString()

/**
 * Returns this string if it's not null and not empty, otherwise returns the [another].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline infix fun String?.or(another: String): String = if (isNullOrEmpty()) another else this

/**
 * Returns itself if this string is not empty, otherwise null.
 *
 * @author 凛 (https://github.com/RinOrz)
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
 * @author 凛 (https://github.com/RinOrz)
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
 * @author 凛 (https://github.com/RinOrz)
 */
fun String.removeBlanks(): String = filterNot { it.isWhitespace() }

/**
 * Returns the string without line breaks.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun String.removeLineBreaks(): String = replace("\n", "").replace("\r", "").replace("\r\n", "")