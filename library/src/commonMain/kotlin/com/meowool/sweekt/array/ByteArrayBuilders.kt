@file:OptIn(ExperimentalContracts::class)
@file:Suppress("MemberVisibilityCanBePrivate")

package com.meowool.sweekt.array

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.max

/**
 * A mutable bytes object used to build a bytearray at any time, similar to [StringBuilder].
 *
 * @author 凛 (https://github.com/RinOrz)
 */
class ByteArrayBuilder {

  private var value: ByteArray

  /** The size of this builder (the number of bytes it contains). */
  var size: Int = 0
    private set

  /** Constructs an empty bytearray builder. */
  constructor() : this(capacity = 8)

  /** Constructs a builder that contains the same bytes as the specified [content] bytearray. */
  constructor(content: ByteArray) {
    value = content.copyOf(content.size + 8)
  }

  /** Constructs an empty bytearray builder with the specified initial [capacity]. */
  constructor(capacity: Int) {
    value = when {
      capacity > 0 -> ByteArray(capacity)
      capacity == 0 -> EmptyByteArray
      else -> throw IllegalArgumentException("Illegal Capacity: $capacity")
    }
  }

  /** Returns the byte in this byte array at the given [index]. */
  operator fun get(index: Int): Byte = this.value[index]

  /** Sets the byte in this byte array at the given [index] to the given [value]. */
  operator fun set(index: Int, value: Byte) = this.value.set(index, value)

  /** Creates an iterator over this byte array. */
  operator fun iterator(): ByteIterator = this.value.iterator()

  /** Appends all the specified byte array [value] into this byte array. */
  fun append(value: ByteArray): ByteArrayBuilder = apply {
    val oldSize = this.size
    this.size += value.size
    ensureCapacity(this.size).write(value, oldSize)
  }

  /** Appends the specified byte [value] into byte array. */
  fun append(value: Byte): ByteArrayBuilder = apply {
    ensureCapacity().write(value, this.size)
    this.size++
  }

  /**
   * Returns a byte array containing a copy of the bytes in this builder.
   *
   * @param trimSize The size of the returned byte array, if the size is smaller than the total
   *   size of the byte array of this builder, some bytes will be trimmed.
   */
  fun toByteArray(trimSize: Int = this.size): ByteArray = value.copyOf(trimSize)

  /**
   * Reverse the byte array.
   *
   * @see value
   */
  fun reverse(): ByteArrayBuilder = apply { value.reverse() }

  /**
   * Ensures that the capacity of this bytearray builder is at least equal to the
   * specified [minimumCapacity].
   *
   * If the current capacity is less than the [minimumCapacity], a new backing storage is allocated
   * with greater capacity. Otherwise, this method takes no action and simply returns.
   */
  fun ensureCapacity(minimumCapacity: Int = value.size + 1): ByteArray = when {
    minimumCapacity > value.size &&
      !(value.contentEquals(EmptyByteArray) &&
        minimumCapacity <= DefaultCapacity) -> grow(minimumCapacity)
    else -> value
  }

  /**
   * Increases the capacity to ensure that it can hold at least the
   * number of elements specified by the minimum capacity argument.
   *
   * @param minCapacity the desired minimum capacity
   * @throws IllegalArgumentException if minCapacity is less than zero
   */
  private fun grow(minCapacity: Int = size + 1): ByteArray =
    value.copyOf(newCapacity(minCapacity)).also { value = it }

  private fun newCapacity(minCapacity: Int): Int {
    val oldCapacity: Int = value.size
    val newCapacity = oldCapacity + (oldCapacity shr 1)
    if (newCapacity - minCapacity <= 0) {
      if (value.contentEquals(EmptyByteArray)) return max(DefaultCapacity, minCapacity)
      require(minCapacity >= 0) { "minCapacity:$minCapacity < 0" }
      return minCapacity
    }
    return if (newCapacity - MaxArraySize <= 0) newCapacity else hugeCapacity(minCapacity)
  }

  private fun hugeCapacity(minCapacity: Int): Int {
    require(minCapacity >= 0) { "minCapacity:$minCapacity < 0" }
    return if (minCapacity > MaxArraySize) Int.MAX_VALUE else MaxArraySize
  }

  /** Reference JVM [ArrayList] */
  private companion object {
    const val DefaultCapacity = 10
    const val MaxArraySize = Int.MAX_VALUE - 8
    val EmptyByteArray = ByteArray(0)
  }
}

/**
 * Builds new byte by populating newly created [ByteArrayBuilder] using provided [builderAction]
 * and then converting it to [ByteArray].
 */
inline fun buildByteArray(builderAction: ByteArrayBuilder.() -> Unit): ByteArray {
  contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
  return ByteArrayBuilder().apply(builderAction).toByteArray()
}

/**
 * Builds new bytes by populating newly created [ByteArrayBuilder] initialized with the given
 * [capacity] using provided [builderAction] and then converting it to [ByteArray].
 */
inline fun buildByteArray(capacity: Int, builderAction: ByteArrayBuilder.() -> Unit): ByteArray {
  contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
  return ByteArrayBuilder(capacity).apply(builderAction).toByteArray()
}