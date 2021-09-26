@file:Suppress("NOTHING_TO_INLINE", "NO_ACTUAL_FOR_EXPECT")

package com.meowool.sweekt.array

/**
 * Write the given [bytes] into this array.
 *
 * For example, `arrayOf(a, b, c, d).write(arrayOf(e, f))` is `arrayOf(e, f, c, d)`.
 *
 * @param offset where to start writing.
 * @param byteCount how many element of [bytes] are writing.
 * @param filter whether to filter out bytes without writing.
 * @return this array
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun ByteArray.write(
  bytes: ByteArray,
  offset: Int = 0,
  byteCount: Int = bytes.size,
  filter: (Byte) -> Boolean = { true },
): ByteArray = apply {
  require(offset in 0..this.size) { "offset: $offset, total size: $size" }
  require(byteCount <= bytes.size) {
    "byteCount `$byteCount` cannot be greater than the total size of given the bytes."
  }
  var readIndex = 0
  var writeIndex = offset
  val writeCount = offset + byteCount
  while(writeIndex < writeCount) {
    val byte = bytes[readIndex++]
    if (filter(byte)) this[writeIndex++] = byte
  }
}

/**
 * Write the given [byte] into this array.
 *
 * For example, `arrayOf(a, b, c, d).write(e, offset = 1)` is `arrayOf(a, e, c, d)`.
 *
 * @param offset where to start writing.
 * @return this array
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun ByteArray.write(byte: Byte, offset: Int = 0): ByteArray = apply {
  require(offset in 0..this.size) { "offset: $offset, total size: $size" }
  this[offset] = byte
}

/**
 * Returns the subarray of this array starting from the [startIndex] and ending right
 * before the [endIndex].
 *
 * For example, `arrayOf(a, b, c, d).subarray(0, 2)` is `arrayOf(a, b, c)`.
 *
 * @param startIndex the start index (inclusive).
 * @param endIndex the end index (exclusive).
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun ByteArray.subarray(startIndex: Int = 0, endIndex: Int = this.size): ByteArray =
  if (startIndex == 0 && endIndex == this.size) this
  else this.copyOfRange(startIndex, endIndex)

/**
 * Returns true if the beginning of this array with the given [prefix].
 *
 * @param prefix the prefix to be looking.
 * @param offset the offset as the starting index of the looking.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun ByteArray.startsWith(vararg prefix: Byte, offset: Int = 0): Boolean {
  require(offset >= 0) { "offset < 0" }
  if (this.contentEquals(prefix)) return false
  if (prefix.size + offset > this.size) return false
  return prefix.indices.all {
    val expect = prefix[it + offset]
    val actual = this[it + offset]
    expect == actual
  }
}

/**
 * Returns a (new) array and adds the given [value] at the array first.
 *
 * @param value the value to be added.
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun ByteArray.addFirst(value: Byte): ByteArray = add(0, value)

/**
 * Returns a (new) array and adds all the given [values] at the array beginning.
 *
 * @param values all values to be added.
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun ByteArray.addFirstAll(values: ByteArray): ByteArray = addAll(0, values)

/**
 * Returns a (new) array and inserts the given [value] at the given index (starting from zero).
 *
 * @param index the position within array to add the new [value].
 * @param value the value to be added.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun ByteArray.add(index: Int, value: Byte): ByteArray = arrayInsertArray(index, byteArrayOf(value))

/**
 * Returns a (new) array and inserts all the given [values] at the given index (starting from zero).
 *
 * @param index the position within array to add the new [values].
 * @param values all values to be added.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun ByteArray.addAll(index: Int, values: ByteArray): ByteArray = arrayInsertArray(index, values)

/**
 * Returns a (new) array and adds the given [value] at the array last.
 *
 * @param value the value to be added.
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun ByteArray.add(value: Byte): ByteArray = plus(value)

/**
 * Returns a (new) array and adds all the given [values] at the array last.
 *
 * @param values all values to be added.
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun ByteArray.addAll(vararg values: Byte): ByteArray = plus(values)

/**
 * Returns a new array and removes the element with the specified index.
 *
 * For example:
 * ```
 * arrayOf(a).remove(0)     = emptyArray()
 * arrayOf(a, b).remove(0)  = arrayOf(b)
 * arrayOf(a, b).remove(1)  = emptyArray(a)
 * ```
 *
 * @param index the index of the element to be removed.
 * @author 凛 (https://github.com/RinOrz)
 */
fun ByteArray.remove(index: Int): ByteArray = arrayRemove(index)

/**
 * Returns a new array and removes the element with the specified index.
 *
 * For example:
 * ```
 * arrayOf(a).remove(0)              = emptyArray()
 * arrayOf(a, b).remove(0)           = arrayOf(b)
 * arrayOf(a, b).remove(0, 1)        = emptyArray()
 * arrayOf(a, b, c).remove(1, 2)     = arrayOf(a)
 * arrayOf(a, b, c).remove(0, 2)     = arrayOf(b)
 * arrayOf(a, b, c).remove(0, 1, 2)  = emptyArray()
 * ```
 *
 * @param indices the index of the element to be removed.
 * @author 凛 (https://github.com/RinOrz)
 */
fun ByteArray.remove(vararg indices: Int): ByteArray = arrayRemove(*indices)

/**
 * Returns a new array and removes all elements in the range from the beginning of the [startIndex]
 * to the end of the [endIndex].
 *
 * For example, `arrayOf(a, b, c, d, e).removeRange(2, 4)` is `arrayOf(a, b, d)`.
 *
 * @param startIndex the start index (inclusive).
 * @param endIndex the end index (exclusive).
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun ByteArray.removeRange(startIndex: Int = 0, endIndex: Int = this.size): ByteArray =
  arrayRemoveRange(startIndex, endIndex)

/** Inserts elements into an array at the given index (starting from zero). */
internal expect fun <T: Any> T.arrayInsertArray(index: Int, newArray: Any): T

/** Removes multiple array elements specified by index. */
internal expect fun <T: Any> T.arrayRemove(index: Int): T

/** Removes multiple array elements specified by index. */
internal expect fun <T: Any> T.arrayRemove(vararg indices: Int): T

/** Delete range array */
internal expect fun <T: Any> T.arrayRemoveRange(startIndex: Int, endIndex: Int): T