@file:Suppress("NewApi")

package com.meowool.sweekt.datetime

import java.time.format.DateTimeFormatter

/**
 * Formatter for printing and parsing date-time objects.
 *
 * @author 凛 (RinOrz)
 */
actual typealias DateTimeFormatter = DateTimeFormatter

/**
 * The ISO time formatter that formats or parses a time with an offset, such as '10:15+01:00' or '10:15:30+01:00'.
 *
 * @author 凛 (RinOrz)
 */
@PublishedApi
internal actual val ISO_ZONED_DATE_TIME_FORMATTER: DateTimeFormatter get() = DateTimeFormatter.ISO_ZONED_DATE_TIME