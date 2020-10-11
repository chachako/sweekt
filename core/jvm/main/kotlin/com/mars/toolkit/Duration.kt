@file:OptIn(ExperimentalTime::class)
package com.mars.toolkit

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

/** Returns a [Duration] equal to this [Int] number of nanoseconds. */
val Int.nanos get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Long] number of nanoseconds. */
val Long.nanos get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Double] number of nanoseconds. */
val Double.nanos get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Int] number of microseconds. */
val Int.micros get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Long] number of microseconds. */
val Long.micros get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Double] number of microseconds. */
val Double.micros get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Int] number of milliseconds. */
val Int.millis get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Long] number of milliseconds. */
val Long.millis get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Double] number of milliseconds. */
val Double.millis get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Int] number of seconds. */
val Int.seconds get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Long] number of seconds. */
val Long.seconds get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Double] number of seconds. */
val Double.seconds get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Int] number of minutes. */
val Int.minutes get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Long] number of minutes. */
val Long.minutes get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Double] number of minutes. */
val Double.minutes get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Int] number of hours. */
val Int.hours get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Long] number of hours. */
val Long.hours get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Double] number of hours. */
val Double.hours get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Int] number of days. */
val Int.days get() = toDuration(DurationUnit.DAYS)

/** Returns a [Duration] equal to this [Long] number of days. */
val Long.days get() = toDuration(DurationUnit.DAYS)

/** Returns a [Duration] equal to this [Double] number of days. */
val Double.days get() = toDuration(DurationUnit.DAYS)