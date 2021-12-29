@file:Suppress("NO_ACTUAL_FOR_EXPECT", "UNSUPPORTED_FEATURE", "NOTHING_TO_INLINE")

package com.meowool.sweekt.datetime

import com.meowool.sweekt.Locale
import com.meowool.sweekt.defaultLocale
import com.meowool.sweekt.run
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.number

/**
 * Returns the current instant from the system clock.
 *
 * @author 凛 (RinOrz)
 */
val nowMilliseconds: Long get() = run(catching = { nowCompatMilliseconds }) {
  nowInstant.toEpochMilliseconds()
}

/**
 * Returns current year's number, such as `2021/03/01` is `2021`.
 *
 * @author 凛 (RinOrz)
 */
inline val currentYear: Int get() = nowDateTime.year

/**
 * Returns current month's number in the year, such as `2021/03/01` is `3`.
 *
 * @author 凛 (RinOrz)
 */
inline val currentMonth: Int get() = nowDateTime.month.number

/**
 * Returns today's number in month, such as `2021/03/01` is `60`.
 *
 * @author 凛 (RinOrz)
 */
inline val todayOfMonth: Int get() = nowDateTime.dayOfMonth

/**
 * Returns today's number in the year, such as `2021/03/01` is `1`.
 *
 * @author 凛 (RinOrz)
 */
inline val todayOfYear: Int get() = nowDateTime.dayOfYear

/**
 * Returns today's number in the week, such as `2021/03/01` is [DayOfWeek.MONDAY].
 *
 * @author 凛 (RinOrz)
 */
inline val todayOfWeek: DayOfWeek get() = nowDateTime.dayOfWeek

/**
 * Returns current hour's number in today, such as `23:59:00` is `23`.
 *
 * @author 凛 (RinOrz)
 */
inline val currentHour: Int get() = nowDateTime.hour

/**
 * Returns current minute's number in today, such as `23:59:00` is `59`.
 *
 * @author 凛 (RinOrz)
 */
inline val currentMinute: Int get() = nowDateTime.minute

/**
 * Returns current minute's number in today, such as `23:59:00` is `00`.
 *
 * @author 凛 (RinOrz)
 */
inline val currentSecond: Int get() = nowDateTime.second

/**
 * Resolves this [CharSequence] to epoch milliseconds using the given pattern and locale.
 *
 * @param pattern the pattern used to resolve time string.
 * @param locale the locale of the formatter.
 *
 * @author 凛 (RinOrz)
 */
inline fun CharSequence.toEpochMillis(
  pattern: String,
  locale: Locale = defaultLocale(),
): Long = toInstant(pattern, locale).epochMillis

/**
 * Resolves this [CharSequence] to epoch milliseconds using the given [formatter].
 *
 * @param formatter the formatter to resolve time string.
 *
 * @author 凛 (RinOrz)
 */
inline fun CharSequence.toEpochMillis(
  formatter: DateTimeFormatter = ISO_ZONED_DATE_TIME_FORMATTER,
): Long = toInstant(formatter).epochMillis

internal expect val nowCompatMilliseconds: Long