@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt


/**
 * Removes the last char of this string builder.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun StringBuilder.removeLast(): StringBuilder = deleteAt(lastIndex)

/**
 * Removes the first char of this string builder.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun StringBuilder.removeFirst(): StringBuilder = deleteAt(0)

/**
 * Removes characters in the specified range from this string builder and returns this instance.
 *
 * @param startIndex the beginning (inclusive) of the range to remove.
 * @param endIndex the end (exclusive) of the range to remove.
 *
 * @throws IndexOutOfBoundsException or [IllegalArgumentException] when [startIndex] is out of
 *   range of this string builder indices or when `startIndex > endIndex`.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun StringBuilder.removeRange(startIndex: Int = 0, endIndex: Int = this.size): StringBuilder =
  deleteRange(startIndex, endIndex)