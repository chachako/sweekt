@file:OptIn(ExperimentalTime::class)

package com.meowool.sweekt.datetime

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

/** Returns a [Duration] equal to this [Int] number of nanoseconds. */
inline val Int.nanos get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Long] number of nanoseconds. */
inline val Long.nanos get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Double] number of nanoseconds. */
inline val Double.nanos get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Int] number of microseconds. */
inline val Int.micros get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Long] number of microseconds. */
inline val Long.micros get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Double] number of microseconds. */
inline val Double.micros get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Int] number of milliseconds. */
inline val Int.millis get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Long] number of milliseconds. */
inline val Long.millis get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Double] number of milliseconds. */
inline val Double.millis get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Int] number of seconds. */
inline val Int.seconds get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Long] number of seconds. */
inline val Long.seconds get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Double] number of seconds. */
inline val Double.seconds get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Int] number of minutes. */
inline val Int.minutes get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Long] number of minutes. */
inline val Long.minutes get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Double] number of minutes. */
inline val Double.minutes get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Int] number of hours. */
inline val Int.hours get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Long] number of hours. */
inline val Long.hours get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Double] number of hours. */
val Double.hours get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Int] number of days. */
val Int.days get() = toDuration(DurationUnit.DAYS)

/** Returns a [Duration] equal to this [Long] number of days. */
val Long.days get() = toDuration(DurationUnit.DAYS)

/** Returns a [Duration] equal to this [Double] number of days. */
val Double.days get() = toDuration(DurationUnit.DAYS)