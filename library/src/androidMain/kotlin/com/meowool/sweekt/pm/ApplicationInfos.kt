@file:Suppress("NOTHING_TO_INLINE")
@file:JvmMultifileClass
@file:JvmName("PackageManager")

package com.meowool.sweekt.pm

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager


/**
 * Bridges [PackageManager.getApplicationInfo] to make it more convenient to use.
 */
inline fun Context.getApplicationInfo(packageName: String, flags: Int = 0): ApplicationInfo =
  this.packageManager.getApplicationInfo(packageName, flags)

/**
 * Bridges [PackageManager.getApplicationInfo] to make it more convenient to use.
 */
inline fun PackageManager.getApplicationInfo(packageName: String): ApplicationInfo =
  this.getApplicationInfo(packageName, 0)