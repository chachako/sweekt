package com.meowool.sweekt

import kotlin.jvm.JvmInline

/**
 * Represents a generic pair of two int values.
 *
 * There is no meaning attached to values in this class, it can be used for any purpose.
 * Pair exhibits value semantics, i.e. two pairs are equal if both components are equal.
 *
 * @property packedValue the packed value of [first] and [second].
 * @constructor Creates a new instance of inline pair.
 *
 * @see Pair
 * @author 凛 (https://github.com/RinOrz)
 */
@JvmInline
value class IntPair(private val packedValue: Long) {
  /**
   * The first value unpacked from the [packedValue].
   */
  val first: Int get() = unpackInt1(packedValue)

  /**
   * The second value unpacked from the [packedValue].
   */
  val second: Int get() = unpackInt2(packedValue)

  /**
   * Returns string representation of the [Pair] including its [first] and [second] values.
   */
  override fun toString(): String = "($first, $second)"

  /**
   * Returns a new copy to overwrite [first] or and [second] values.
   */
  fun copy(first: Int = this.first, second: Int = this.second): IntPair = first to second

  operator fun component1() = this.first
  operator fun component2() = this.second
}

/**
 * Packs [first] and [second] to create an inline int pair.
 *
 * @see Pair
 * @author 凛 (https://github.com/RinOrz)
 */
fun IntPair(first: Int, second: Int): IntPair = IntPair(packInts(first, second))

/**
 * Creates a tuple of type [IntPair] from `this` and [that].
 *
 * This can be useful for creating [Map] literals with less noise.
 *
 * @see Pair
 * @author 凛 (https://github.com/RinOrz)
 */
infix fun Int.to(that: Int): IntPair = IntPair(packInts(this, that))

/**
 * Converts this int pair into a list.
 *
 * @see Pair.toList
 * @author 凛 (https://github.com/RinOrz)
 */
fun IntPair.toList(): List<Int> = listOf(first, second)
