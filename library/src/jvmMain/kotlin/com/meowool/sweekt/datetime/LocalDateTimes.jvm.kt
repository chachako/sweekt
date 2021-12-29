package com.meowool.sweekt.datetime

import com.meowool.sweekt.Locale
import com.meowool.sweekt.ifNull
import com.meowool.sweekt.runOrNull
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaZoneId
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


/**
 * Formats this date-time using the localized formatter.
 *
 * @param locale the locale of the formatter.
 * @author 凛 (RinOrz)
 */
actual fun LocalDateTime.format(locale: Locale): String =
  this.toJavaLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(locale))

/**
 * Formats the date-time using the given pattern and locale.
 *
 * @param pattern the pattern used to format date-time.
 * @param locale the locale of the formatter.
 *
 * @author 凛 (RinOrz)
 */
actual fun LocalDateTime.format(pattern: String, locale: Locale): String =
  this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern(pattern, locale))

/**
 * Parses this [CharSequence] to [LocalDateTime] using the given pattern and locale.
 *
 * @param pattern the pattern used to resolve time string.
 * @param locale the locale of the formatter.
 * @param zone the zone id of the formatter.
 *
 * @author 凛 (RinOrz)
 */
actual fun CharSequence.toDateTime(pattern: String, locale: Locale, zone: TimeZone): LocalDateTime {
  val zoneId = zone.toJavaZoneId()
  return runOrNull {
    ZonedDateTime.parse(this, DateTimeFormatter.ofPattern(pattern, locale).withZone(zoneId))
  }.ifNull {
    runOrNull {
      // try to get time only
      LocalTime.parse(this, DateTimeFormatter.ofPattern(pattern, locale).withZone(zoneId))
        .atDate(LocalDate.of(1970, 1, 1)) // LocalDate.EPOCH
        .atZone(zoneId)
    }
  }.ifNull {
    // try to get date only
    LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern, locale)).atStartOfDay(zoneId)
  }.toLocalDateTime().toKotlinLocalDateTime()
}

/**
 * Parses this [CharSequence] to [LocalDateTime] using the given [formatter].
 *
 * @param formatter the formatter to resolve time string.
 * @author 凛 (RinOrz)
 */
actual fun CharSequence.toDateTime(formatter: DateTimeFormatter, zone: TimeZone): LocalDateTime =
  ZonedDateTime.parse(this, formatter.withZone(zone.toJavaZoneId())).toLocalDateTime().toKotlinLocalDateTime()