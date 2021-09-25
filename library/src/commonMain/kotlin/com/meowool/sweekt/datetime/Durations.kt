package com.meowool.sweekt.datetime

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

/** Returns a [Duration] equal to this [Int] number of nanoseconds. */
inline val Int.nanos: Duration get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Long] number of nanoseconds. */
inline val Long.nanos: Duration get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Double] number of nanoseconds. */
inline val Double.nanos: Duration get() = toDuration(DurationUnit.NANOSECONDS)

/** Returns a [Duration] equal to this [Int] number of microseconds. */
inline val Int.micros: Duration get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Long] number of microseconds. */
inline val Long.micros: Duration get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Double] number of microseconds. */
inline val Double.micros: Duration get() = toDuration(DurationUnit.MICROSECONDS)

/** Returns a [Duration] equal to this [Int] number of milliseconds. */
inline val Int.millis: Duration get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Long] number of milliseconds. */
inline val Long.millis: Duration get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Double] number of milliseconds. */
inline val Double.millis: Duration get() = toDuration(DurationUnit.MILLISECONDS)

/** Returns a [Duration] equal to this [Int] number of seconds. */
inline val Int.seconds: Duration get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Long] number of seconds. */
inline val Long.seconds: Duration get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Double] number of seconds. */
inline val Double.seconds: Duration get() = toDuration(DurationUnit.SECONDS)

/** Returns a [Duration] equal to this [Int] number of minutes. */
inline val Int.minutes: Duration get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Long] number of minutes. */
inline val Long.minutes: Duration get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Double] number of minutes. */
inline val Double.minutes: Duration get() = toDuration(DurationUnit.MINUTES)

/** Returns a [Duration] equal to this [Int] number of hours. */
inline val Int.hours: Duration get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Long] number of hours. */
inline val Long.hours: Duration get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Double] number of hours. */
inline val Double.hours: Duration get() = toDuration(DurationUnit.HOURS)

/** Returns a [Duration] equal to this [Int] number of days. */
inline val Int.days: Duration get() = toDuration(DurationUnit.DAYS)

/** Returns a [Duration] equal to this [Long] number of days. */
inline val Long.days: Duration get() = toDuration(DurationUnit.DAYS)

/** Returns a [Duration] equal to this [Double] number of days. */
inline val Double.days: Duration get() = toDuration(DurationUnit.DAYS)