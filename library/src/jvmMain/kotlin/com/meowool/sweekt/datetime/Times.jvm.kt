@file:Suppress("NOTHING_TO_INLINE", "NewApi")

package com.meowool.sweekt.datetime

/**
 * Returns the current instant from the system clock.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
actual inline val nowMilliseconds: Long get() = nowInstant.epochMillis