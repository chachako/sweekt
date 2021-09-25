@file:Suppress("NOTHING_TO_INLINE", "SpellCheckingInspection")
@file:OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)

package com.meowool.sweekt

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

/**
 * Converts [any] to [String].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun String(any: Any?): String = any.toString()

/**
 * Returns this string if it's not null and not empty, otherwise returns the [another].
 */
inline infix fun String?.or(another: String): String = if (isNullOrEmpty()) another else this

/**
 * Returns itself if this string is not empty, otherwise null.
 */
inline fun String?.takeIfNotEmpty(): String? {
  contract {
    returnsNotNull() implies (this@takeIfNotEmpty != null)
  }
  return if (isNullOrEmpty()) null else this
}

/**
 * Returns itself if this string is empty, otherwise null.
 */
inline fun String.takeIfEmpty(): String? = if (isEmpty()) this else null

/**
 * Returns the string without blanks.
 */
fun String.removeBlanks(): String = filterNot { it.isWhitespace() }

/**
 * Returns the string without line breaks.
 */
fun String.removeLineBreaks(): String = replace("\n", "").replace("\r", "").replace("\r\n", "")