@file:Suppress("NOTHING_TO_INLINE")
@file:JvmMultifileClass
@file:JvmName("PackageManager")

package com.meowool.sweekt.pm

import android.content.Context
import android.content.Intent.ACTION_DELETE
import android.content.pm.PackageManager
import android.net.Uri
import com.meowool.sweekt.Intent
import com.meowool.sweekt.newTask

/**
 * Returns `true` if the app of [packageName] has been installed.
 *
 * @author 凛 (RinOrz)
 */
inline fun Context.isPackageInstalled(packageName: String): Boolean =
  this.packageManager.isPackageInstalled(packageName)

/**
 * Returns `true` if the app of [packageName] has been installed.
 *
 * @author 凛 (RinOrz)
 */
fun PackageManager.isPackageInstalled(packageName: String): Boolean = try {
  this.getPackageInfo(packageName).applicationInfo.enabled
} catch (e: Exception) {
  false
}

/**
 * Uninstall the given [packageName] package.
 *
 * @param packageName the name of package to uninstalled.
 * @author 凛 (RinOrz)
 */
fun Context.uninstallPackage(packageName: String) = Intent {
  action = ACTION_DELETE
  data = Uri.parse("package:$packageName")
  newTask()
}.run(this::startActivity)