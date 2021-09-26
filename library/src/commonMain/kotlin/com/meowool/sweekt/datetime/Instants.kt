@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.datetime

import com.meowool.sweekt.Locale
import com.meowool.sweekt.defaultLocale
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Returns the current instant from the system clock.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline val nowInstant: Instant get() = Clock.System.now()

/**
 * Converts this instant to the number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline val Instant.epochMillis: Long get() = this.toEpochMilliseconds()

/**
 * Converts the instant to zoned date-time.
 *
 * @param zone the time-zone to create zoned date-time.
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Instant.toDateTime(zone: TimeZone = TimeZone.system()): LocalDateTime = toLocalDateTime(zone)

/**
 * Returns `true` if this instant belongs to this year.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Instant.isCurrentYear(zone: TimeZone = TimeZone.system()): Boolean = this.toDateTime(zone).isCurrentYear()

/**
 * Returns `true` if this instant belongs to this month.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Instant.isCurrentMonth(zone: TimeZone = TimeZone.system()): Boolean = this.toDateTime(zone).isCurrentMonth()

/**
 * Returns `true` if this instant belongs to today.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Instant.isToday(zone: TimeZone = TimeZone.system()): Boolean = this.toDateTime(zone).isToday()

/**
 * Formats this instant using the localized formatter.
 *
 * @param locale the locale of the formatter.
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Instant.format(locale: Locale = defaultLocale()): String =
  this.toDateTime().format(locale)

/**
 * Formats the instant using the given pattern and locale.
 *
 * @param pattern the pattern used to format date-time.
 * @param locale the locale of the formatter.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Instant.format(pattern: String, locale: Locale = defaultLocale()): String =
  this.toDateTime().format(pattern, locale)

/**
 * Returns `true` if this [Instant] is between the given range.
 *
 * @param start the range start.
 * @param end the range end.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun Instant.inRange(
  start: Instant,
  end: Instant,
): Boolean = this in start..end

/**
 * Returns `true` if this [Instant] is between the given range.
 *
 * @param start the range start.
 * @param end the range end.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun Instant.inRange(
  start: LocalDateTime,
  end: LocalDateTime,
): Boolean = this in start.toInstant(TimeZone.system())..end.toInstant(TimeZone.system())

/**
 * Returns `true` if this [Instant] is between the given range.
 *
 * @param startTime the start time string of range.
 * @param endTime the stop time string of range.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Instant.inRange(
  startTime: String,
  endTime: String,
): Boolean = this in startTime.toInstant()..endTime.toInstant()

/**
 * Resolves this [CharSequence] to [Instant] using the given pattern and locale.
 *
 * @param pattern the pattern used to resolve time string.
 * @param locale the locale of the formatter.
 * @param zone the zone id of the formatter.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun CharSequence.toInstant(
  pattern: String,
  locale: Locale = defaultLocale(),
  zone: TimeZone = TimeZone.system(),
): Instant = toDateTime(pattern, locale, zone).toInstant(zone)

/**
 * Resolves this [CharSequence] to [Instant] using the given [formatter].
 *
 * @param formatter the formatter to resolve time string.
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun CharSequence.toInstant(
  formatter: DateTimeFormatter = ISO_ZONED_DATE_TIME_FORMATTER,
  zone: TimeZone = TimeZone.system(),
): Instant = toDateTime(formatter).toInstant(zone)

/**
 * Take this [Long] as milliseconds and returns instant.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Long.asMilliInstant(): Instant =
  Instant.fromEpochMilliseconds(this)

/**
 * Take this [Long] as seconds and returns instant.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Long.asSecondInstant(nanoAdjustment: Long): Instant =
  Instant.fromEpochSeconds(this, nanoAdjustment)

/**
 * Formats the instant using the localized formatter.
 *
 * @param instant the instant to be formatted.
 * @param locale the locale of the formatter.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun formatInstant(
  instant: Instant = nowInstant,
  locale: Locale = defaultLocale(),
): String = instant.format(locale)

/**
 * Formats the instant using the given pattern and locale.
 *
 * @param pattern the pattern used to format date-time.
 * @param instant the instant to be formatted.
 * @param locale the locale of the formatter.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun formatInstant(
  pattern: String,
  instant: Instant = nowInstant,
  locale: Locale = defaultLocale(),
): String = instant.toDateTime().format(pattern, locale)
