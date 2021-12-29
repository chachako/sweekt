@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt.datetime

import kotlinx.datetime.TimeZone

/**
 * Queries the current system time zone.
 *
 * If the current system time zone changes, this function can reflect this change on the next invocation.
 *
 * @author 凛 (RinOrz)
 */
inline fun TimeZone.Companion.system() = currentSystemDefault()