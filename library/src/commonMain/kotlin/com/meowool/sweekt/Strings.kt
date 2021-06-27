@file:Suppress("NOTHING_TO_INLINE", "SpellCheckingInspection")
@file:OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)

package com.meowool.sweekt

import kotlin.contracts.ExperimentalContracts
import kotlin.experimental.ExperimentalTypeInference

/**
 * Converts [any] to [String].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun String(any: Any?): String = any.toString()

/**
 * Returns this string if its not null and not empty, otherwise returns the [another].
 */
inline infix fun String?.or(another: String): String = if (isNullOrEmpty()) another else this

/**
 * Returns the string without blanks.
 */
fun String.removeBlanks(): String = filterNot { it.isWhitespace() }