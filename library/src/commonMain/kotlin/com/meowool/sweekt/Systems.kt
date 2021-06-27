@file:Suppress("NOTHING_TO_INLINE", "NO_ACTUAL_FOR_EXPECT")

package com.meowool.sweekt

/**
 * Determine whether the currently running system is android.
 *
 * [for more details](https://developer.android.com/reference/java/lang/System#getProperties())
 */
expect val isAndroidSystem: Boolean

/**
 * Determine whether the currently running system is windows.
 */
expect val isWindowsSystem: Boolean

/**
 * Determine whether the currently running system is mac.
 */
expect val isMacSystem: Boolean

/**
 * Determine whether the currently running system is linux.
 */
expect val isLinuxSystem: Boolean