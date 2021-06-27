@file:Suppress("NO_ACTUAL_FOR_EXPECT", "NOTHING_TO_INLINE")
@file:OptIn(ExperimentalTypeInference::class, ExperimentalContracts::class)

package com.meowool.sweekt

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.ExperimentalTypeInference

/**
 * Returns the size of this [CharSequence]
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline val CharSequence.size: Int get() = length

/**
 * Returns this char sequence if its not null and not empty, otherwise returns the [another].
 */
inline infix fun CharSequence?.or(another: CharSequence): CharSequence = if (isNullOrEmpty()) another else this

/**
 * Returns the char sequence without blanks.
 */
fun CharSequence.removeBlanks(): CharSequence = filterNot { it.isWhitespace() }

/**
 * Make the titlecase of first character of this char sequence and return.
 *
 * @see Char.titlecase
 */
fun CharSequence.firstCharTitlecase(): String = this.toString().replaceFirstChar {
  if (it.isLowerCase()) it.titlecaseChar() else it
}

/**
 * Make the uppercase of first character of this char sequence and return.
 *
 * @see Char.uppercase
 */
fun CharSequence.firstCharUppercase(): String = this.toString().replaceFirstChar {
  if (it.isLowerCase()) it.uppercase() else it.toString()
}

/**
 * Make the lowercase of first character of this char sequence and return.
 *
 * @see Char.lowercase
 */
fun CharSequence.firstCharLowercase(): String = this.toString().replaceFirstChar {
  if (it.isUpperCase()) it.lowercase() else it.toString()
}

/**
 * Make the uppercase of last character of this char sequence and return.
 *
 * @see Char.uppercase
 */
fun CharSequence.lastCharUppercase(): String = replaceLastChar {
  if (it.isLowerCase()) it.uppercase() else it.toString()
}

/**
 * Make the lowercase of last character of this char sequence and return.
 *
 * @see Char.lowercase
 */
fun CharSequence.lastCharLowercase(): String = replaceLastChar {
  if (it.isUpperCase()) it.lowercase() else it.toString()
}

/**
 * Returns a copy of this char sequence having its last character replaced with the result of the specified [transform],
 * or the original char sequence if it's empty.
 *
 * @param transform function that takes the last character and returns the result of the transform applied to the character.
 */
@OverloadResolutionByLambdaReturnType
expect inline fun CharSequence.replaceLastChar(transform: (Char) -> Char): String

/**
 * Returns a copy of this char sequence having its last character replaced with the result of the specified [transform],
 * or the original char sequence if it's empty.
 *
 * @param transform function that takes the last character and returns the result of the transform applied to the character.
 */
@OverloadResolutionByLambdaReturnType
expect inline fun CharSequence.replaceLastChar(transform: (Char) -> CharSequence): String

/**
 * Returns a copy of this char sequence having its last character replaced with the specified [newLast],
 * or the original char sequence if it's empty.
 */
inline fun CharSequence.replaceLastChar(newLast: Char): String = replaceLastChar { newLast }

/**
 * Returns a copy of this char sequence having its last character replaced with the specified [newLast],
 * or the original char sequence if it's empty.
 */
inline fun CharSequence.replaceLastChar(newLast: CharSequence): String = replaceLastChar { newLast }

/**
 * Returns `true` if all contents of this char sequence is chinese.
 */
fun CharSequence.isChinese(): Boolean = all { it.isChinese() }

/**
 * Returns `true` if the content of this char sequence contains chinese.
 *
 * @param ignorePunctuation if the value is `true`, do not check Chinese punctuation
 */
fun CharSequence.isContainsChinese(ignorePunctuation: Boolean = false): Boolean = any {
  if (ignorePunctuation) it.isChineseNotPunctuation() else it.isChinese()
}

/**
 * Returns `true` if all contents of this char sequence is chinese.
 */
fun CharSequence.isEnglish(): Boolean = all { it.isEnglish() }

/**
 * Returns `true` if the content of this char sequence contains chinese.
 *
 * @param ignorePunctuation if the value is `true`, do not check Chinese punctuation
 */
fun CharSequence.isContainsEnglish(ignorePunctuation: Boolean = false): Boolean = any {
  if (ignorePunctuation) it.isEnglishNotPunctuation() else it.isEnglish()
}

/**
 * Returns a substring before the [index].
 * If the char sequence does not has the given [index], returns [noIndexValue] which defaults to the original char sequence.
 */
fun CharSequence.substringBefore(index: Int, noIndexValue: CharSequence = this): String {
  return if (index in 0..length) substring(0, index) else noIndexValue.toString()
}

/**
 * Returns a substring before the first occurrence of [delimiter].
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original char sequence.
 */
fun CharSequence.substringBefore(delimiter: Char, missingDelimiterValue: CharSequence = this): String {
  val index = indexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(0, index)
}

/**
 * Returns a substring before the first occurrence of [delimiter].
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original char sequence.
 */
fun CharSequence.substringBefore(
  delimiter: String,
  missingDelimiterValue: CharSequence = this,
): String {
  val index = indexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(0, index)
}

/**
 * Returns a substring after the [index].
 * If the char sequence does not has the given [index], returns [noIndexValue] which defaults to the original char sequence.
 */
fun CharSequence.substringAfter(index: Int, noIndexValue: CharSequence = this): String {
  return if (index in 0..length) substring(index + 1, length) else noIndexValue.toString()
}

/**
 * Returns a substring after the first occurrence of [delimiter].
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original char sequence.
 */
fun CharSequence.substringAfter(delimiter: Char, missingDelimiterValue: CharSequence = this): String {
  val index = indexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(index + 1, length)
}

/**
 * Returns a substring after the first occurrence of [delimiter].
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original char sequence.
 */
fun CharSequence.substringAfter(delimiter: String, missingDelimiterValue: CharSequence = this): String {
  val index = indexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(index + delimiter.length, length)
}

/**
 * Returns a substring before the last occurrence of [delimiter].
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original char sequence.
 */
fun CharSequence.substringBeforeLast(delimiter: Char, missingDelimiterValue: CharSequence = this): String {
  val index = lastIndexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(0, index)
}

/**
 * Returns a substring before the last occurrence of [delimiter].
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original char sequence.
 */
fun CharSequence.substringBeforeLast(
  delimiter: String,
  missingDelimiterValue: CharSequence = this,
): String {
  val index = lastIndexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(0, index)
}

/**
 * Returns a substring after the last occurrence of [delimiter].
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original char sequence.
 */
fun CharSequence.substringAfterLast(delimiter: Char, missingDelimiterValue: CharSequence = this): String {
  val index = lastIndexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(index + 1, length)
}

/**
 * Returns a substring after the last occurrence of [delimiter].
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original char sequence.
 */
fun CharSequence.substringAfterLast(
  delimiter: String,
  missingDelimiterValue: CharSequence = this,
): String {
  val index = lastIndexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(index + delimiter.length, length)
}

/**
 * Remove the corresponding character from this char sequence according to the given [index] and returns
 * new char sequence.
 */
fun CharSequence.remove(index: Int): String = buildString {
  this@remove.forEachIndexed { i, c ->
    if (index != i) {
      append(c)
    }
  }
}

/**
 * Remove the first character from this char sequence and returns new char sequence.
 */
inline fun CharSequence.removeFirst(): String = substringAfter(0)

/**
 * Remove the last character from this char sequence and returns new char sequence.
 */
inline fun CharSequence.removeLast(): String = substringBefore(lastIndex)