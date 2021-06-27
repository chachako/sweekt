@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import java.util.Locale
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Make the titlecase of first character of this char sequence and return.
 *
 * @see Char.titlecase
 */
fun CharSequence.firstCharTitlecase(locale: Locale): String = this.toString().replaceFirstChar {
  if (it.isLowerCase()) it.titlecase(locale) else it.toString()
}

/**
 * Make the uppercase of first character of this char sequence and return.
 *
 * @see Char.uppercase
 */
fun CharSequence.firstCharUppercase(locale: Locale): String = this.toString().replaceFirstChar {
  if (it.isLowerCase()) it.uppercase(locale) else it.toString()
}

/**
 * Make the lowercase of first character of this char sequence and return.
 *
 * @see Char.lowercase
 */
fun CharSequence.firstCharLowercase(locale: Locale): String = this.toString().replaceFirstChar {
  if (it.isUpperCase()) it.lowercase(locale) else it.toString()
}

/**
 * Make the uppercase of last character of this char sequence and return.
 *
 * @see Char.uppercase
 */
fun CharSequence.lastCharUppercase(locale: Locale): String = replaceLastChar {
  if (it.isLowerCase()) it.uppercase(locale) else it.toString()
}

/**
 * Make the lowercase of last character of this char sequence and return.
 *
 * @see Char.lowercase
 */
fun CharSequence.lastCharLowercase(locale: Locale): String = replaceLastChar {
  if (it.isUpperCase()) it.lowercase(locale) else it.toString()
}

/**
 * Returns a copy of this char sequence having its last character replaced with the result of the specified [transform],
 * or the original char sequence if it's empty.
 *
 * @param transform function that takes the last character and returns the result of the transform applied to the character.
 */
@OverloadResolutionByLambdaReturnType
@JvmName("replaceLastCharWithChar")
actual inline fun CharSequence.replaceLastChar(transform: (Char) -> Char): String {
  contract { callsInPlace(transform, InvocationKind.EXACTLY_ONCE) }
  return if (isNotEmpty()) substring(0, lastIndex) + transform(this[lastIndex]) else this.toString()
}

/**
 * Returns a copy of this char sequence having its last character replaced with the result of the specified [transform],
 * or the original char sequence if it's empty.
 *
 * @param transform function that takes the last character and returns the result of the transform applied to the character.
 */
@OverloadResolutionByLambdaReturnType
@JvmName("replaceLastCharWithCharSequence")
actual inline fun CharSequence.replaceLastChar(transform: (Char) -> CharSequence): String {
  contract { callsInPlace(transform, InvocationKind.EXACTLY_ONCE) }
  return if (isNotEmpty()) substring(0, lastIndex) + transform(this[lastIndex]) else this.toString()
}