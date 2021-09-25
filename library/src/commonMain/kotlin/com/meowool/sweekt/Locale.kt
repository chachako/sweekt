@file:Suppress("NO_ACTUAL_FOR_EXPECT")

package com.meowool.sweekt

/**
 * Represents the locale of the current platform.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
expect class Locale

/**
 * Returns the default locale of the platform.
 */
expect inline fun defaultLocale(): Locale