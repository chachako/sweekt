package com.meowool.sweekt

private val OSName: String by lazy {
  System.getProperty("os.name")
}

/**
 * Determine whether the currently running system is android.
 *
 * [For more details](https://developer.android.com/reference/java/lang/System#getProperties())
 *
 * @author 凛 (https://github.com/RinOrz)
 */
actual val isAndroidSystem: Boolean by lazy {
  System.getProperty("java.specification.vendor") == "The Android Project"
}

/**
 * Determine whether the currently running system is windows.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
actual val isWindowsSystem: Boolean by lazy {
  OSName.startsWith("Windows", ignoreCase = true)
}

/**
 * Determine whether the currently running system is mac.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
actual val isMacSystem: Boolean by lazy {
  OSName.startsWith("Mac", ignoreCase = true)
}

/**
 * Determine whether the currently running system is linux.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
actual val isLinuxSystem: Boolean by lazy {
  OSName.startsWith("Linux", ignoreCase = true) && !isAndroidSystem
}