@file:Suppress("NO_ACTUAL_FOR_EXPECT", "NOTHING_TO_INLINE")

package com.meowool.sweekt.datetime

import com.meowool.sweekt.Locale
import com.meowool.sweekt.defaultLocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


/**
 * Returns the zoned date-time formed from current instant and the system zone.
 *
 * @see nowInstant
 */
inline val nowDateTime: LocalDateTime get() = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())

/**
 * Converts this date-time to the number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
 *
 * @see Instant.epochMillis
 */
inline val LocalDateTime.epochMillis: Long get() = this.toInstant(TimeZone.currentSystemDefault()).epochMillis

/**
 * Returns `true` if this zoned date-time belongs to this year.
 */
inline fun LocalDateTime.isCurrentYear(): Boolean = this.year == currentYear

/**
 * Returns `true` if this zoned date-time belongs to this month.
 */
fun LocalDateTime.isCurrentMonth(): Boolean = this.year == currentYear && this.monthNumber == currentMonth

/**
 * Returns `true` if this zoned date-time belongs to today.
 */
fun LocalDateTime.isToday(): Boolean = this.year == currentYear && this.dayOfYear == dayOfYear

/**
 * Formats this date-time using the localized formatter.
 *
 * @param locale the locale of the formatter.
 */
expect fun LocalDateTime.format(locale: Locale = defaultLocale()): String

/**
 * Formats the date-time using the given pattern and locale.
 *
 * @param pattern the pattern used to format date-time.
 * @param locale the locale of the formatter.
 */
expect fun LocalDateTime.format(pattern: String, locale: Locale = defaultLocale()): String

/**
 * Returns `true` if this [Instant] is between the given range.
 *
 * @param start the range start.
 * @param end the range end.
 */
fun LocalDateTime.inRange(
  start: LocalDateTime,
  end: LocalDateTime,
): Boolean = this in start..end

/**
 * Returns `true` if this [Instant] is between the given range.
 *
 * @param start the range start.
 * @param end the range end.
 */
fun LocalDateTime.inRange(
  start: Instant,
  end: Instant,
): Boolean = this in start.toDateTime()..end.toDateTime()

/**
 * Returns `true` if this [Instant] is between the given range.
 *
 * @param startTime the start time string of range.
 * @param endTime the stop time string of range.
 */
inline fun LocalDateTime.inRange(
  startTime: String,
  endTime: String,
): Boolean = this in startTime.toDateTime()..endTime.toDateTime()

/**
 * Parses this [CharSequence] to [LocalDateTime] using the given pattern and locale.
 *
 * @param pattern the pattern used to resolve time string.
 * @param locale the locale of the formatter.
 * @param zone the zone id of the formatter.
 */
expect fun CharSequence.toDateTime(
  pattern: String,
  locale: Locale = defaultLocale(),
  zone: TimeZone = TimeZone.system(),
): LocalDateTime

/**
 * Parses this [CharSequence] to [LocalDateTime] using the given [formatter].
 *
 * @param formatter the formatter to resolve time string.
 * @param zone the zone id of the formatter.
 */
expect fun CharSequence.toDateTime(
  formatter: DateTimeFormatter = ISO_ZONED_DATE_TIME_FORMATTER,
  zone: TimeZone = TimeZone.system(),
): LocalDateTime

/**
 * Formats the date-time using the localized formatter.
 *
 * @param dateTime the date-time to be formatted.
 * @param locale the locale of the formatter.
 */
inline fun formatDateTime(
  dateTime: LocalDateTime = nowDateTime,
  locale: Locale = defaultLocale(),
): String = dateTime.format(locale)

/**
 * Formats the date-time using the given pattern and locale.
 *
 * @param pattern the pattern used to format date-time.
 * @param dateTime the date-time to be formatted.
 * @param locale the locale of the formatter.
 */
inline fun formatDateTime(
  pattern: String,
  dateTime: LocalDateTime = nowDateTime,
  locale: Locale = defaultLocale(),
): String = dateTime.format(pattern, locale)
