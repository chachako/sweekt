package com.meowool.sweekt.datetime

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Returns a [Duration] equal to this [Int] number of nanoseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Int.nanos: Duration get() = toDuration(DurationUnit.NANOSECONDS)

/**
 * Returns a [Duration] equal to this [Long] number of nanoseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Long.nanos: Duration get() = toDuration(DurationUnit.NANOSECONDS)

/**
 * Returns a [Duration] equal to this [Double] number of nanoseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Double.nanos: Duration get() = toDuration(DurationUnit.NANOSECONDS)

/**
 * Returns a [Duration] equal to this [Int] number of microseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Int.micros: Duration get() = toDuration(DurationUnit.MICROSECONDS)

/**
 * Returns a [Duration] equal to this [Long] number of microseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Long.micros: Duration get() = toDuration(DurationUnit.MICROSECONDS)

/**
 * Returns a [Duration] equal to this [Double] number of microseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Double.micros: Duration get() = toDuration(DurationUnit.MICROSECONDS)

/**
 * Returns a [Duration] equal to this [Int] number of milliseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Int.millis: Duration get() = toDuration(DurationUnit.MILLISECONDS)

/**
 * Returns a [Duration] equal to this [Long] number of milliseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Long.millis: Duration get() = toDuration(DurationUnit.MILLISECONDS)

/**
 * Returns a [Duration] equal to this [Double] number of milliseconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Double.millis: Duration get() = toDuration(DurationUnit.MILLISECONDS)

/**
 * Returns a [Duration] equal to this [Int] number of seconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Int.seconds: Duration get() = toDuration(DurationUnit.SECONDS)

/**
 * Returns a [Duration] equal to this [Long] number of seconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Long.seconds: Duration get() = toDuration(DurationUnit.SECONDS)

/**
 * Returns a [Duration] equal to this [Double] number of seconds.
 *
 * @author 凛 (RinOrz)
 */
inline val Double.seconds: Duration get() = toDuration(DurationUnit.SECONDS)

/**
 * Returns a [Duration] equal to this [Int] number of minutes.
 *
 * @author 凛 (RinOrz)
 */
inline val Int.minutes: Duration get() = toDuration(DurationUnit.MINUTES)

/**
 * Returns a [Duration] equal to this [Long] number of minutes.
 *
 * @author 凛 (RinOrz)
 */
inline val Long.minutes: Duration get() = toDuration(DurationUnit.MINUTES)

/**
 * Returns a [Duration] equal to this [Double] number of minutes.
 *
 * @author 凛 (RinOrz)
 */
inline val Double.minutes: Duration get() = toDuration(DurationUnit.MINUTES)

/**
 * Returns a [Duration] equal to this [Int] number of hours.
 *
 * @author 凛 (RinOrz)
 */
inline val Int.hours: Duration get() = toDuration(DurationUnit.HOURS)

/**
 * Returns a [Duration] equal to this [Long] number of hours.
 *
 * @author 凛 (RinOrz)
 */
inline val Long.hours: Duration get() = toDuration(DurationUnit.HOURS)

/**
 * Returns a [Duration] equal to this [Double] number of hours.
 *
 * @author 凛 (RinOrz)
 */
inline val Double.hours: Duration get() = toDuration(DurationUnit.HOURS)

/**
 * Returns a [Duration] equal to this [Int] number of days.
 *
 * @author 凛 (RinOrz)
 */
inline val Int.days: Duration get() = toDuration(DurationUnit.DAYS)

/**
 * Returns a [Duration] equal to this [Long] number of days.
 *
 * @author 凛 (RinOrz)
 */
inline val Long.days: Duration get() = toDuration(DurationUnit.DAYS)

/**
 * Returns a [Duration] equal to this [Double] number of days.
 *
 * @author 凛 (RinOrz)
 */
inline val Double.days: Duration get() = toDuration(DurationUnit.DAYS)