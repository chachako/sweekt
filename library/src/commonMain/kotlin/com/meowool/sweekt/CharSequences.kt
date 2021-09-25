@file:Suppress("NO_ACTUAL_FOR_EXPECT", "NOTHING_TO_INLINE")

package com.meowool.sweekt

import com.meowool.sweekt.coroutines.onNotNullEmpty
import kotlin.contracts.contract

/**
 * Returns the size of this [CharSequence]
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline val CharSequence.size: Int get() = length

/**
 * Returns this char sequence if it's not null and not empty, otherwise returns the [another].
 */
inline infix fun <C: CharSequence> C?.or(another: C): C = if (isNullOrEmpty()) another else this

/**
 * Returns itself if this char sequence is not empty, otherwise null.
 */
inline fun <C: CharSequence> C?.takeIfNotEmpty(): C? {
  contract {
    returnsNotNull() implies (this@takeIfNotEmpty != null)
  }
  return if (isNullOrEmpty()) null else this
}

/**
 * Returns itself if this char sequence is empty, otherwise null.
 */
inline fun <C: CharSequence> C.takeIfEmpty(): C? = if (isEmpty()) this else null

/**
 * Returns the char sequence without blanks.
 */
fun CharSequence.removeBlanks(): CharSequence = filterNot { it.isWhitespace() }

/**
 * Returns the char sequence without line breaks.
 */
fun CharSequence.removeLineBreaks(): String = this.toString().replace("\n", "").replace("\r", "").replace("\r\n", "")

/**
 * Converts first character of this char sequence to title case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.titlecase
 */
expect fun CharSequence.firstCharTitlecase(locale: Locale = defaultLocale()): String

/**
 * Converts first character of this char sequence to upper case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.uppercase
 */
expect fun CharSequence.firstCharUppercase(locale: Locale = defaultLocale()): String

/**
 * Converts first character of this char sequence to lower case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.lowercase
 */
expect fun CharSequence.firstCharLowercase(locale: Locale = defaultLocale()): String

/**
 * Converts last character of this char sequence to upper case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.uppercase
 */
expect fun CharSequence.lastCharUppercase(locale: Locale = defaultLocale()): String

/**
 * Converts last character of this char sequence to lower case using Unicode mapping rules of the specified [locale].
 *
 * @see Char.lowercase
 */
expect fun CharSequence.lastCharLowercase(locale: Locale = defaultLocale()): String

/**
 * Returns a copy of this char sequence having its last character replaced with the result of the specified
 * [transform], or the original char sequence if it's empty.
 *
 * @param transform the function that takes the last character and returns the result of the transform applied to
 *   the character.
 */
@OverloadResolutionByLambdaReturnType
expect inline fun CharSequence.replaceLastChar(transform: (Char) -> Char): String

/**
 * Returns a copy of this char sequence having its last character replaced with the result of the specified
 * [transform], or the original char sequence if it's empty.
 *
 * @param transform the function that takes the last character and returns the result of the transform applied to
 *   the character.
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
 * Returns `true` if all contents of this char sequence is digit.
 */
fun CharSequence?.isDigits(): Boolean {
  contract { returns(true) implies (this@isDigits != null) }
  if (this == null) return false
  return all { it.isDigit() }
}

/**
 * Returns `true` if all contents of this char sequence is chinese.
 */
fun CharSequence?.isChinese(): Boolean {
  contract { returns(true) implies (this@isChinese != null) }
  if (this == null) return false
  return all { it.isChinese() }
}

/**
 * Returns `true` if the content of this char sequence contains chinese.
 *
 * @param ignorePunctuation if the value is `true`, do not check Chinese punctuation
 */
fun CharSequence?.isContainsChinese(ignorePunctuation: Boolean = false): Boolean {
  contract { returns(true) implies (this@isContainsChinese != null) }
  if (this == null) return false
  return any {
    if (ignorePunctuation) it.isChineseNotPunctuation() else it.isChinese()
  }
}

/**
 * Returns `true` if all contents of this char sequence is chinese.
 *
 * @param allowPunctuation if the value is `false`, any punctuation in the char sequence is not allowed.
 */
fun CharSequence?.isEnglish(allowPunctuation: Boolean = true): Boolean {
  contract { returns(true) implies (this@isEnglish != null) }
  if (this == null) return false
  return all {
    if (allowPunctuation) it.isEnglish() else it.isEnglishNotPunctuation()
  }
}

/**
 * Returns `true` if the content of this char sequence contains chinese.
 *
 * @param ignorePunctuation if the value is `true`, do not check Chinese punctuation
 */
fun CharSequence?.isContainsEnglish(ignorePunctuation: Boolean = false): Boolean {
  contract { returns(true) implies (this@isContainsEnglish != null) }
  if (this == null) return false
  return any {
    if (ignorePunctuation) it.isEnglishNotPunctuation() else it.isEnglish()
  }
}

/**
 * Returns a substring before the [index].
 *
 * If the char sequence does not have the given [index], returns [noIndexValue] which defaults to the original
 * char sequence.
 */
fun CharSequence.substringBefore(index: Int, noIndexValue: CharSequence = this): String {
  return if (index in 0..length) substring(0, index) else noIndexValue.toString()
}

/**
 * Returns a substring before the first occurrence of [delimiter].
 *
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original
 * char sequence.
 */
fun CharSequence.substringBefore(delimiter: Char, missingDelimiterValue: CharSequence = this): String {
  val index = indexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(0, index)
}

/**
 * Returns a substring before the first occurrence of [delimiter].
 *
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original
 * char sequence.
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
 *
 * If the char sequence does not have the given [index], returns [noIndexValue] which defaults to the original
 * char sequence.
 */
fun CharSequence.substringAfter(index: Int, noIndexValue: CharSequence = this): String {
  return if (index in 0..length) substring(index + 1, length) else noIndexValue.toString()
}

/**
 * Returns a substring after the first occurrence of [delimiter].
 *
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original
 * char sequence.
 */
fun CharSequence.substringAfter(delimiter: Char, missingDelimiterValue: CharSequence = this): String {
  val index = indexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(index + 1, length)
}

/**
 * Returns a substring after the first occurrence of [delimiter].
 *
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original
 * char sequence.
 */
fun CharSequence.substringAfter(delimiter: String, missingDelimiterValue: CharSequence = this): String {
  val index = indexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(index + delimiter.length, length)
}

/**
 * Returns a substring before the last occurrence of [delimiter].
 *
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original
 * char sequence.
 */
fun CharSequence.substringBeforeLast(delimiter: Char, missingDelimiterValue: CharSequence = this): String {
  val index = lastIndexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(0, index)
}

/**
 * Returns a substring before the last occurrence of [delimiter].
 *
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original
 * char sequence.
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
 *
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original
 * char sequence.
 */
fun CharSequence.substringAfterLast(delimiter: Char, missingDelimiterValue: CharSequence = this): String {
  val index = lastIndexOf(delimiter)
  return if (index == -1) missingDelimiterValue.toString() else substring(index + 1, length)
}

/**
 * Returns a substring after the last occurrence of [delimiter].
 *
 * If the char sequence does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original
 * char sequence.
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
fun CharSequence.remove(index: Int): String = buildString(length) {
  append(substringBefore(index))
  append(substringAfter(index))
}

/**
 * Remove the first character from this char sequence and returns new char sequence.
 */
inline fun CharSequence.removeFirst(): String = substringAfter(0)

/**
 * Remove the last character from this char sequence and returns new char sequence.
 */
inline fun CharSequence.removeLast(): String = substringBefore(lastIndex)

/**
 * Returns a substring of chars from a range of this char sequence starting at the [startIndex] and ending right before the [endIndex].
 *
 * @param startIndex the start index (inclusive).
 * @param endIndex the end index (exclusive). If not specified, the length of the char sequence is used.
 */
inline fun CharSequence.substring(startIndex: Int = 0, endIndex: Int = length): String =
  subSequence(startIndex, endIndex).toString()

/**
 * Starting from [offset], splits this char sequence with [delimiter].
 */
fun CharSequence.split(delimiter: Char, offset: Int): List<String> =
  splitBy(offset) { it == delimiter }

/**
 * Starting from [offset], splits this char sequence by [predicate].
 */
inline fun CharSequence.splitBy(offset: Int = 0, predicate: (Char) -> Boolean): List<String> =
  ArrayList<String>(this.length).apply { forEachSplitBy(predicate, offset, ::add) }

/**
 * Starting from [offset], splits this char sequence into [destination] with [delimiter].
 */
fun <C: MutableCollection<CharSequence>> CharSequence.splitTo(
  destination: C,
  delimiter: Char,
  offset: Int = 0
): C = splitTo(destination, offset) { it == delimiter }

/**
 * Starting from [offset], splits this char sequence into [destination] by [predicate].
 */
inline fun <C: MutableCollection<CharSequence>> CharSequence.splitTo(
  destination: C,
  offset: Int = 0,
  predicate: (Char) -> Boolean
):C = destination.apply { forEachSplitBy(predicate, offset, ::add) }

/**
 * Starting from [offset], splits this char sequence with [delimiter] and call the given [action] for each segment
 * after splitting.
 */
inline fun CharSequence.forEachSplit(
  delimiter: Char,
  offset: Int = 0,
  action: (segment: String) -> Unit
) = this.forEachSplitBy({ it == delimiter }, offset, action)

/**
 * Starting from [offset], splits this char sequence by [predicate] and call the given [action] for each segment
 * after splitting.
 */
inline fun CharSequence.forEachSplitBy(
  predicate: (Char) -> Boolean,
  offset: Int = 0,
  action: (segment: String) -> Unit
) = forEachSplitIndexedBy(predicate, offset) { _, segment -> action(segment) }

/**
 * Starting from [offset], splits this char sequence by [predicate] and call the given [action] for each segment
 * after splitting.
 *
 * @param action the action called for each segment after splitting, receives segment and segment index parameters.
 */
inline fun CharSequence.forEachSplitIndexedBy(
  predicate: (Char) -> Boolean,
  offset: Int = 0,
  action: (index: Int, segment: String) -> Unit
) {
  var prev = offset
  var actionIndex = 0
  this.forEachIndexed { index, char ->
    if (predicate(char) && index >= prev) {
      action(actionIndex, this.substring(prev, index))
      actionIndex++
      prev = index + 1
    }
  }
  if (prev < this.length) action(actionIndex, this.substring(prev, this.length))
}