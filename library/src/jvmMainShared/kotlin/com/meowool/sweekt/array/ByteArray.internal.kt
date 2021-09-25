package com.meowool.sweekt.array

import java.lang.reflect.Array
import java.util.Arrays


/**
 * Inserts elements into an array at the given index (starting from zero).
 */
internal actual fun <T: Any> T.arrayInsertArray(index: Int, newArray: Any): T {
  val length = Array.getLength(this)
  val addedLength = Array.getLength(newArray)
  if (addedLength == 0) return this
  if (index !in 0..length) throw IndexOutOfBoundsException("Index: $index, Length: $length")
  val result = Array.newInstance(this.javaClass.componentType, length + addedLength)
  System.arraycopy(newArray, 0, result, index, addedLength)
  if (index > 0) {
    System.arraycopy(this, 0, result, 0, index)
  }
  if (index < length) {
    System.arraycopy(this, index, result, index + addedLength, length - index)
  }
  @Suppress("UNCHECKED_CAST")
  return result as T
}

/**
 * Removes multiple array elements specified by index.
 *
 * Copy of https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/ArrayUtils.java
 */
internal actual fun <T: Any> T.arrayRemove(index: Int): T {
  val length = Array.getLength(this)
  if (index !in 0..length) throw IndexOutOfBoundsException("Index: $index, Length: $length")

  @Suppress("UNCHECKED_CAST")
  return Array.newInstance(this.javaClass.componentType, length - 1).also { result ->
    System.arraycopy(this, 0, result, 0, index)
    if (index < length - 1) {
      System.arraycopy(this, index + 1, result, index, length - index - 1)
    }
  } as T
}

/**
 * Removes multiple array elements specified by index.
 *
 * Copy of https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/ArrayUtils.java
 */
internal actual fun <T: Any> T.arrayRemove(vararg indices: Int): T {
  val length = Array.getLength(this)
  var diff = 0 // number of distinct indexes, i.e. number of entries that will be removed
  Arrays.sort(indices)

  // identify length of result array
  if (indices.isNotEmpty()) {
    var i = indices.size
    var prevIndex = length
    while (--i >= 0) {
      val index = indices[i]
      if (index < 0 || index >= length) {
        throw java.lang.IndexOutOfBoundsException("Index: $index, Length: $length")
      }
      if (index >= prevIndex) {
        continue
      }
      diff++
      prevIndex = index
    }
  }

  // create result array
  val result = Array.newInstance(this.javaClass.componentType, length - diff)
  if (diff < length) {
    var end = length // index just after last copy
    var dest = length - diff // number of entries so far not copied
    for (i in indices.indices.reversed()) {
      val index = indices[i]
      if (end - index > 1) { // same as (cp > 0)
        val cp = end - index - 1
        dest -= cp
        System.arraycopy(this, index + 1, result, dest, cp)
        // After this copy, we still have room for dest items.
      }
      end = index
    }
    if (end > 0) {
      System.arraycopy(this, 0, result, 0, end)
    }
  }
  @Suppress("UNCHECKED_CAST")
  return result as T
}

/**
 * Delete range array
 */
internal actual fun <T: Any> T.arrayRemoveRange(startIndex: Int, endIndex: Int): T {
  val length = Array.getLength(this)
  if (startIndex !in 0..length) {
    throw IndexOutOfBoundsException("StartIndex: $startIndex, Length: $length")
  }
  if (endIndex > length) {
    throw IndexOutOfBoundsException("EndIndex: $endIndex, Length: $length")
  }
  require(endIndex > startIndex) { "endIndex:$endIndex < startIndex:$startIndex" }
  val newSize = length - (endIndex - startIndex)
  val result = Array.newInstance(this.javaClass.componentType, newSize)

  // First part: 0..startIndex
  System.arraycopy(this, 0, result, 0, startIndex)
  // Second part: endIndex..length
  System.arraycopy(this, endIndex, result, startIndex, newSize - startIndex)

  @Suppress("UNCHECKED_CAST")
  return result as T
}