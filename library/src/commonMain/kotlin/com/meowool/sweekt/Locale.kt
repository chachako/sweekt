@file:Suppress("NO_ACTUAL_FOR_EXPECT")

package com.meowool.sweekt

/**
 * Represents the locale of the current platform.
 *
 * @author 凛 (RinOrz)
 */
expect class Locale

/**
 * Returns the default locale of the platform.
 *
 * @author 凛 (RinOrz)
 */
expect inline fun defaultLocale(): Locale