@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import java.util.Locale
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Converts first character of this char sequence to title case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.titlecase
 * @author 凛 (https://github.com/RinOrz)
 */
actual fun CharSequence.firstCharTitlecase(locale: Locale): String = this.toString().replaceFirstChar {
  if (it.isLowerCase()) it.titlecase(locale) else it.toString()
}

/**
 * Converts first character of this char sequence to upper case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.uppercase
 * @author 凛 (https://github.com/RinOrz)
 */
actual fun CharSequence.firstCharUppercase(locale: Locale): String = this.toString().replaceFirstChar {
  if (it.isLowerCase()) it.uppercase(locale) else it.toString()
}

/**
 * Converts first character of this char sequence to lower case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.lowercase
 * @author 凛 (https://github.com/RinOrz)
 */
actual fun CharSequence.firstCharLowercase(locale: Locale): String = this.toString().replaceFirstChar {
  if (it.isUpperCase()) it.lowercase(locale) else it.toString()
}

/**
 * Converts last character of this char sequence to upper case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.uppercase
 * @author 凛 (https://github.com/RinOrz)
 */
actual fun CharSequence.lastCharUppercase(locale: Locale): String = replaceLastChar {
  if (it.isLowerCase()) it.uppercase(locale) else it.toString()
}

/**
 * Converts last character of this char sequence to lower case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.lowercase
 * @author 凛 (https://github.com/RinOrz)
 */
actual fun CharSequence.lastCharLowercase(locale: Locale): String = replaceLastChar {
  if (it.isUpperCase()) it.lowercase(locale) else it.toString()
}

/**
 * Returns a copy of this char sequence having its last character replaced with the result of the specified [transform],
 * or the original char sequence if it's empty.
 *
 * @param transform function that takes the last character and returns the result of the transform applied to
 *   the character.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
@OverloadResolutionByLambdaReturnType
@JvmName("replaceLastCharToChar")
actual inline fun CharSequence.replaceLastChar(transform: (Char) -> Char): String {
  contract { callsInPlace(transform, InvocationKind.EXACTLY_ONCE) }
  return if (isNotEmpty()) substring(0, lastIndex) + transform(this[lastIndex]) else this.toString()
}

/**
 * Returns a copy of this char sequence having its last character replaced with the result of the specified [transform],
 * or the original char sequence if it's empty.
 *
 * @param transform function that takes the last character and returns the result of the transform applied to
 *   the character.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
@OverloadResolutionByLambdaReturnType
@JvmName("replaceLastCharToCharSequence")
actual inline fun CharSequence.replaceLastChar(transform: (Char) -> CharSequence): String {
  contract { callsInPlace(transform, InvocationKind.EXACTLY_ONCE) }
  return if (isNotEmpty()) substring(0, lastIndex) + transform(this[lastIndex]) else this.toString()
}