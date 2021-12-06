@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED", "NOTHING_TO_INLINE", "ClassName")

package com.meowool.sweekt

/**
 * Used to mark APIs that are not available externally.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
@RequiresOptIn(
  level = RequiresOptIn.Level.ERROR,
  message = "This is an internal 'com.meowool.sweekt' API that should not be used from outside."
)
annotation class InternalSweektApi

/**
 * Used to mark APIs that are only available for compiler.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
@RequiresOptIn(
  level = RequiresOptIn.Level.ERROR,
  message = "This is an internal compiler API of 'com.meowool.sweekt' that should not be used from outside."
)
annotation class InternalSweektCompilerApi

@PublishedApi
internal fun compilerImplementation(): Nothing = throw UnsupportedOperationException(
  "Implemented by sweekt compiler plugin, " +
    "please make sure you have applied the plugin of https://github.com/RinOrz/sweekt."
)