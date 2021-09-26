@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

import java.util.Locale as JavaLocale

/**
 * Represents the locale of the current platform.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
actual typealias Locale = JavaLocale

/**
 * Returns the default locale of the platform.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
actual inline fun defaultLocale(): Locale = Locale.getDefault()