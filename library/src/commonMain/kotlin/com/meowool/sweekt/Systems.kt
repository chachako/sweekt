@file:Suppress("NOTHING_TO_INLINE", "NO_ACTUAL_FOR_EXPECT")

package com.meowool.sweekt

/**
 * Determine whether the currently running system is android.
 *
 * [For more details](https://developer.android.com/reference/java/lang/System#getProperties())
 *
 * @author 凛 (https://github.com/RinOrz)
 */
expect val isAndroidSystem: Boolean

/**
 * Determine whether the currently running system is windows.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
expect val isWindowsSystem: Boolean

/**
 * Determine whether the currently running system is mac.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
expect val isMacSystem: Boolean

/**
 * Determine whether the currently running system is linux.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
expect val isLinuxSystem: Boolean