@file:Suppress("NO_ACTUAL_FOR_EXPECT")

package com.meowool.sweekt.datetime

/**
 * Formatter for printing and parsing date-time objects.
 *
 * @author 凛 (RinOrz)
 */
expect class DateTimeFormatter

/**
 * The ISO time formatter that formats or parses a time with an offset, such as '10:15+01:00' or '10:15:30+01:00'.
 *
 * @author 凛 (RinOrz)
 */
@PublishedApi
internal expect val ISO_ZONED_DATE_TIME_FORMATTER: DateTimeFormatter
