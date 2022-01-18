@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED", "NOTHING_TO_INLINE", "ClassName")

package com.meowool.sweekt

/**
 * Used to mark APIs that are not available externally.
 *
 * @author 凛 (RinOrz)
 */
@RequiresOptIn(
  level = RequiresOptIn.Level.ERROR,
  message = "This is an internal 'com.meowool.sweekt' API that should not be used from outside."
)
annotation class InternalSweektApi

/**
 * Represents APIs are only available for compiler.
 *
 * @author 凛 (RinOrz)
 */
@PublishedApi
internal const val InternalSweektCompilerApi = "This is an internal compiler API of 'com.meowool.sweekt' that should not be used from outside."

@PublishedApi
internal fun compilerImplementation(): Nothing = throw UnsupportedOperationException(
  "Implemented by sweekt compiler plugin, " +
    "please make sure you have applied the plugin of https://github.com/RinOrz/sweekt"
)