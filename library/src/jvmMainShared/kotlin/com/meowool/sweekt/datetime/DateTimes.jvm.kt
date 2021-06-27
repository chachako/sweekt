@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.datetime

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


/**
 * Returns the current instant from the system clock.
 */
inline val nowInstant: Instant get() = Instant.now()

/**
 * Returns the zoned date-time formed from current instant and the system zone.
 *
 * @see com.meowool.sweekt.datetime.nowInstant
 */
inline val nowDateTime: ZonedDateTime get() = ZonedDateTime.now()

/**
 * Returns current year's number, such as `2021/03/01` is `2021`.
 */
inline val currentYear: Int get() = nowDateTime.year

/**
 * Returns current month's number in the year, such as `2021/03/01` is `3`.
 */
inline val currentMonth: Int get() = nowDateTime.monthValue

/**
 * Returns today's number in month, such as `2021/03/01` is `60`.
 */
inline val todayOfMonth: Int get() = nowDateTime.dayOfMonth

/**
 * Returns today's number in the year, such as `2021/03/01` is `1`.
 */
inline val todayOfYear: Int get() = nowDateTime.dayOfYear

/**
 * Returns today's number in the week, such as `2021/03/01` is [DayOfWeek.MONDAY].
 */
inline val todayOfWeek: DayOfWeek get() = nowDateTime.dayOfWeek

/**
 * Returns current hour's number in today, such as `23:59:00` is `23`.
 */
inline val currentHour: Int get() = nowDateTime.hour

/**
 * Returns current minute's number in today, such as `23:59:00` is `59`.
 */
inline val currentMinute: Int get() = nowDateTime.minute

/**
 * Returns current minute's number in today, such as `23:59:00` is `00`.
 */
inline val currentSecond: Int get() = nowDateTime.second

/**
 * Converts this instant to the number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
 */
inline val Instant.epochMillis: Long get() = this.toEpochMilli()

/**
 * Converts this date-time to the number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
 *
 * @see Instant.epochMillis
 */
inline val ZonedDateTime.epochMillis: Long get() = this.toInstant().epochMillis

/**
 * Converts the instant to zoned date-time.
 *
 * @param zone the time-zone to create zoned date-time.
 */
inline fun Instant.toDateTime(
  zone: ZoneId = ZoneId.systemDefault(),
): ZonedDateTime = ZonedDateTime.ofInstant(this, zone)

/**
 * Converts the local date to zoned date-time of the start of the day.
 *
 * @param zone the time-zone to create zoned date-time.
 */
inline fun LocalDate.toDateTime(
  zone: ZoneId = ZoneId.systemDefault(),
): ZonedDateTime = this.atStartOfDay(zone)

/**
 * Converts the local time to zoned date-time.
 *
 * @param date the date of create zoned date-time.
 * @param zone the time-zone to create zoned date-time.
 */
inline fun LocalTime.toDateTime(
  date: LocalDate = LocalDate.of(1970, 1, 1), // LocalDate.EPOCH
  zone: ZoneId = ZoneId.systemDefault(),
): ZonedDateTime = this.atDate(date).atZone(zone)

/**
 * Converts the local date to zoned date-time of the start of the day.
 *
 * @param zone the time-zone to create zoned date-time.
 */
inline fun LocalDateTime.zoned(
  zone: ZoneId = ZoneId.systemDefault(),
): ZonedDateTime = this.atZone(zone)

/**
 * Formats this date-time using the localized formatter.
 *
 * @param locale the locale of the formatter.
 */
fun ZonedDateTime.format(locale: Locale = Locale.getDefault()): String =
  this.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(locale))

/**
 * Formats the date-time using the given pattern and locale.
 *
 * @param pattern the pattern used to format date-time.
 * @param locale the locale of the formatter.
 */
fun ZonedDateTime.format(
  pattern: String,
  locale: Locale = Locale.getDefault(),
): String = this.format(DateTimeFormatter.ofPattern(pattern, locale))

/**
 * Formats the date-time using the localized formatter.
 *
 * @param dateTime the date-time to be formatted.
 * @param locale the locale of the formatter.
 */
inline fun formatDateTime(
  dateTime: ZonedDateTime = nowDateTime,
  locale: Locale = Locale.getDefault(),
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
  dateTime: ZonedDateTime = nowDateTime,
  locale: Locale = Locale.getDefault(),
): String = dateTime.format(pattern, locale)

/**
 * Formats this instant using the localized formatter.
 *
 * @param locale the locale of the formatter.
 */
inline fun Instant.format(locale: Locale = Locale.getDefault()): String =
  this.toDateTime().format(locale)

/**
 * Formats the instant using the given pattern and locale.
 *
 * @param pattern the pattern used to format date-time.
 * @param locale the locale of the formatter.
 */
inline fun Instant.format(
  pattern: String,
  locale: Locale = Locale.getDefault(),
): String = this.toDateTime().format(pattern, locale)

/**
 * Formats the instant using the localized formatter.
 *
 * @param instant the instant to be formatted.
 * @param locale the locale of the formatter.
 */
inline fun formatInstant(
  instant: Instant = nowInstant,
  locale: Locale = Locale.getDefault(),
): String = instant.format(locale)

/**
 * Formats the instant using the given pattern and locale.
 *
 * @param pattern the pattern used to format date-time.
 * @param instant the instant to be formatted.
 * @param locale the locale of the formatter.
 */
inline fun formatInstant(
  pattern: String,
  instant: Instant = nowInstant,
  locale: Locale = Locale.getDefault(),
): String = instant.toDateTime().format(pattern, locale)

/**
 * Take this [Long] as milliseconds and returns instant.
 */
inline fun Long.asMilliInstant(): Instant =
  Instant.ofEpochMilli(this)

/**
 * Take this [Long] as seconds and returns instant.
 */
inline fun Long.asSecondInstant(nanoAdjustment: Long): Instant =
  Instant.ofEpochSecond(this, nanoAdjustment)

/**
 * Returns `true` if this zoned date-time belongs to this year.
 */
inline fun ZonedDateTime.isCurrentYear(): Boolean =
  this.year == currentYear

/**
 * Returns `true` if this instant belongs to this year.
 */
inline fun Instant.isCurrentYear(zone: ZoneId = ZoneId.systemDefault()): Boolean =
  this.toDateTime(zone).isCurrentYear()

/**
 * Returns `true` if this zoned date-time belongs to this month.
 */
fun ZonedDateTime.isCurrentMonth(): Boolean =
  this.year == currentYear && this.monthValue == currentMonth

/**
 * Returns `true` if this instant belongs to this month.
 */
inline fun Instant.isCurrentMonth(zone: ZoneId = ZoneId.systemDefault()): Boolean =
  this.toDateTime(zone).isCurrentMonth()

/**
 * Returns `true` if this zoned date-time belongs to today.
 */
fun ZonedDateTime.isToday(): Boolean =
  this.year == currentYear && this.dayOfYear == dayOfYear

/**
 * Returns `true` if this instant belongs to today.
 */
inline fun Instant.isToday(zone: ZoneId = ZoneId.systemDefault()): Boolean =
  this.toDateTime(zone).isToday()

/**
 * Resolves this [CharSequence] to [ZonedDateTime] using the given pattern and locale.
 *
 * @param pattern the pattern used to resolve time string.
 * @param locale the locale of the formatter.
 * @param zone the zone id of the formatter.
 */
fun CharSequence.resolveToTime(
  pattern: String,
  locale: Locale = Locale.getDefault(),
  zone: ZoneId = ZoneId.systemDefault(),
): ZonedDateTime = runCatching {
  ZonedDateTime.parse(this, DateTimeFormatter.ofPattern(pattern, locale).withZone(zone))
}.getOrElse {
  runCatching {
    // try to get time only
    LocalTime.parse(this, DateTimeFormatter.ofPattern(pattern, locale).withZone(zone)).toDateTime(zone = zone)
  }.getOrElse {
    // try to get date only
    resolveToDate(pattern, locale).toDateTime(zone)
  }
}

/**
 * Resolves this [CharSequence] to [ZonedDateTime] using the given [formatter].
 *
 * @param formatter the formatter to resolve time string.
 */
inline fun CharSequence.resolveToTime(
  formatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME,
): ZonedDateTime = ZonedDateTime.parse(this, formatter)

/**
 * Resolves this [CharSequence] to [ZonedDateTime] using the given pattern and locale.
 *
 * @param pattern the pattern used to resolve date string.
 * @param locale the locale of the formatter.
 */
fun CharSequence.resolveToDate(
  pattern: String,
  locale: Locale = Locale.getDefault(),
): LocalDate = LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern, locale))

/**
 * Resolves this [CharSequence] to [ZonedDateTime] using the given [formatter].
 *
 * @param formatter the formatter to resolve date string.
 */
inline fun CharSequence.resolveToDate(
  formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE,
): LocalDate = LocalDate.parse(this, formatter)

/**
 * Resolves this [CharSequence] to [Instant] using the given pattern and locale.
 *
 * @param pattern the pattern used to resolve time string.
 * @param locale the locale of the formatter.
 * @param zoneId the zone id of the formatter.
 */
inline fun CharSequence.resolveToInstant(
  pattern: String,
  locale: Locale = Locale.getDefault(),
  zoneId: ZoneId = ZoneId.systemDefault(),
): Instant = resolveToTime(pattern, locale, zoneId).toInstant()

/**
 * Resolves this [CharSequence] to [Instant] using the given [formatter].
 *
 * @param formatter the formatter to resolve time string.
 */
inline fun CharSequence.resolveToInstant(
  formatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME,
): Instant = resolveToTime(formatter).toInstant()

/**
 * Resolves this [CharSequence] to epoch milliseconds using the given pattern and locale.
 *
 * @param pattern the pattern used to resolve time string.
 * @param locale the locale of the formatter.
 */
inline fun CharSequence.resolveToEpochMillis(
  pattern: String,
  locale: Locale = Locale.getDefault(),
): Long = resolveToInstant(pattern, locale).epochMillis

/**
 * Resolves this [CharSequence] to epoch milliseconds using the given [formatter].
 *
 * @param formatter the formatter to resolve time string.
 */
inline fun CharSequence.resolveToEpochMillis(
  formatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME,
): Long = resolveToInstant(formatter).epochMillis

/**
 * Tests whether the [time] is between the given range.
 *
 * @param start the range start.
 * @param stop the range stop.
 * @param time the time to test.
 *
 * @see isNowTimeInRange
 */
fun isTimeInRange(
  time: Instant,
  start: Instant,
  stop: Instant,
): Boolean = time.isBefore(start).not() && time.isBefore(stop)

/**
 * Tests whether the [time] is between the given range.
 *
 * @param start the range start.
 * @param stop the range stop.
 * @param time the time to test.
 *
 * @see isNowTimeInRange
 */
fun isTimeInRange(
  time: ZonedDateTime,
  start: ZonedDateTime,
  stop: ZonedDateTime,
): Boolean = time.isBefore(start).not() && time.isBefore(stop)

/**
 * Tests whether the [time] is between the given range.
 *
 * @param startTime the start time string of range.
 * @param stopTime the stop time string of range.
 * @param time the time to test.
 *
 * @see isNowTimeInRange
 */
inline fun isTimeInRange(
  time: ZonedDateTime,
  startTime: String,
  stopTime: String,
): Boolean = isTimeInRange(startTime.resolveToTime(), stopTime.resolveToTime(), time)

/**
 * Tests whether the current time is between the given range.
 *
 * @param startTime the start time string of range.
 * @param stopTime the stop time string of range.
 *
 * @see isTimeInRange
 */
inline fun isNowTimeInRange(
  startTime: Instant,
  stopTime: Instant,
): Boolean = isTimeInRange(nowInstant, startTime, stopTime)

/**
 * Tests whether the current time is between the given range.
 *
 * @param startTime the start time string of range.
 * @param stopTime the stop time string of range.
 *
 * @see isTimeInRange
 */
inline fun isNowTimeInRange(
  startTime: ZonedDateTime,
  stopTime: ZonedDateTime,
): Boolean = isTimeInRange(nowDateTime, startTime, stopTime)

/**
 * Tests whether the current time is between the given range.
 *
 * @param startTime the start time string of range.
 * @param stopTime the stop time string of range.
 *
 * @see isTimeInRange
 */
inline fun isNowTimeInRange(
  startTime: String,
  stopTime: String,
): Boolean = isTimeInRange(nowDateTime, startTime, stopTime)