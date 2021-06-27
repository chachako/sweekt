package com.meowool.sweekt

private val OSName: String by lazy {
  System.getProperty("os.name")
}

/**
 * Determine whether the currently running system is android.
 *
 * [for more details](https://developer.android.com/reference/java/lang/System#getProperties())
 */
actual val isAndroidSystem: Boolean by lazy {
  System.getProperty("java.specification.vendor") == "The Android Project"
}

/**
 * Determine whether the currently running system is windows.
 */
actual val isWindowsSystem: Boolean by lazy {
  OSName.startsWith("Windows", ignoreCase = true)
}

/**
 * Determine whether the currently running system is mac.
 */
actual val isMacSystem: Boolean by lazy {
  OSName.startsWith("Mac", ignoreCase = true)
}

/**
 * Determine whether the currently running system is linux.
 */
actual val isLinuxSystem: Boolean by lazy {
  OSName.startsWith("Linux", ignoreCase = true) && !isAndroidSystem
}