package com.mars.toolkit

import java.text.SimpleDateFormat
import java.util.*

/**
 * 返回今年的字符串
 * 例如: 2020/9/4 返回 "2020"
 */
inline val thisYear: String get() = Calendar.getInstance().get(Calendar.YEAR).toString()

/**
 * 返回本月月数的字符串
 * 例如: 2020/9/4 返回 "09"
 */
inline val thisMonth get() = formatTime(pattern = "MM")

/**
 * 返回今天在这个月的天数的字符串
 * 例如: 2020/9/4 返回 "04"
 */
inline val today get() = formatTime(pattern = "dd")

/** 判断时间戳日期 [Long] 是否为今天 */
inline val Long.isToday get(): Boolean = isThisDate("yyyy-MM-dd")

/** 判断时间戳日期 [Long] 是否在本月 */
inline val Long.isThisMonth get(): Boolean = isThisDate("yyyy-MM")

/**
 * 格式化时间戳 [Long] 并对比日期
 * @param pattern 需要对比的日期格式
 * @sample isToday
 */
fun Long.isThisDate(pattern: String): Boolean =
  SimpleDateFormat(pattern, Locale.getDefault()).run {
    format(Date(this@isThisDate)) == format(Date())
  }

/** 将时间戳转为 [Calendar] 实例 */
fun Long.toCalendar(): Calendar = Calendar.getInstance().also { it.time = Date(this) }


/**
 * 格式化时间戳
 * @param time 需要格式化的时间戳（默认为当前时间）
 * @param pattern 需要格式化的日期格式
 * @param isSecond 决定传入的 [time] 时间戳是否要以秒数形式格式化，默认为毫秒
 */
fun formatTime(
  time: Long = System.currentTimeMillis(),
  pattern: String = "yyyy/dd/MM HH:mm:ss",
  isSecond: Boolean = false,
  locale: Locale = Locale.getDefault()
): String = SimpleDateFormat(pattern, locale).format(Date(if (isSecond) time * 1000 else time))


/**
 * 判断时间是否在时间段内
 * ```
 * // 如果当前时间是在早上 10:20 到明天 00:40
 * isDateRange("10:20", "00:40")
 * ```
 */
fun isTimeRange(beginTime: String, endTime: String): Boolean =
  SimpleDateFormat("HH:mm", Locale.getDefault()).run {
    isDateRange(parse(beginTime), parse(endTime))
  }

/**
 * 判断时间是否在时间段内
 * ```
 * // 如果当前时间是在早上 10:20 到明天 00:40
 * isDateRange(10, 20, 0, 40)
 * ```
 * @param beginHour 开始的小时
 * @param beginMin 开始小时的分钟数
 * @param endHour 结束小时
 * @param endMin 结束小时的分钟数
 */
fun isTimeRange(beginHour: Int, beginMin: Int, endHour: Int, endMin: Int): Boolean =
  SimpleDateFormat("HH:mm", Locale.getDefault()).run {
    isDateRange(parse("$beginHour:$beginMin"), parse("$endHour:$endMin"))
  }

/** 判断当前日期是否在日期范围内 */
fun isDateRange(begin: Date, end: Date): Boolean =
  Date(System.currentTimeMillis()).let { nowTime ->
    nowTime.time >= begin.time && nowTime.time <= end.time
  }