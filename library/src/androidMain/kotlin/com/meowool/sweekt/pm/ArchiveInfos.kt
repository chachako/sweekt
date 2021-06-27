@file:Suppress("NOTHING_TO_INLINE")
@file:JvmMultifileClass
@file:JvmName("PackageManager")

package com.meowool.sweekt.pm

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

/**
 * Resolves the package archive file and return its information.
 *
 * @param archivePath the path of the package to be resolved.
 * @param flags additional option flags to modify the info returned.
 */
fun PackageManager.resolvePackageArchive(archivePath: String, flags: Int = 0): PackageInfo? =
  this.getPackageArchiveInfo(archivePath, flags)?.also {
    it.applicationInfo?.apply {
      publicSourceDir = archivePath
      sourceDir = archivePath
    }
  }

/**
 * Resolves the package archive file and return its information.
 *
 * @param archivePath the path of the package to be resolved.
 * @param flags additional option flags to modify the info returned.
 */
inline fun Context.resolvePackageArchive(archivePath: String, flags: Int = 0): PackageInfo? =
  this.packageManager.resolvePackageArchive(archivePath, flags)

///**
// * Converts the path-site to package archive info by given [context] and returns it.
// *
// * @param flags additional option flags to modify the data returned.
// */
//inline fun PathSite.toPackageInfo(context: Context, flags: Int = 0): PackageInfo? =
//  context.resolvePackageArchive(this.absolutePathString, flags)
//
///**
// * Converts the path-site to package archive info by given [packageManager] and returns it.
// *
// * @param flags additional option flags to modify the data returned.
// */
//inline fun PathSite.toPackageInfo(packageManager: PackageManager, flags: Int = 0): PackageInfo? =
//  packageManager.resolvePackageArchive(this.absolutePathString, flags)

/**
 * Resolves the application package file and return its information.
 *
 * @param apkPath the path of the apk to be resolved.
 * @param flags additional option flags to modify the info returned.
 */
inline fun PackageManager.resolveApk(apkPath: String, flags: Int = 0): ApplicationInfo? =
  this.resolvePackageArchive(apkPath, flags)?.applicationInfo

/**
 * Resolves the application package file and return its information.
 *
 * @param apkPath the path of the apk to be resolved.
 * @param flags additional option flags to modify the info returned.
 */
inline fun Context.resolveApk(apkPath: String, flags: Int = 0): ApplicationInfo? =
  this.resolvePackageArchive(apkPath, flags)?.applicationInfo

///**
// * Converts the path-site to application info by given [context] and returns it.
// *
// * @param flags additional option flags to modify the data returned.
// */
//inline fun PathSite.toApplicationInfo(context: Context, flags: Int = 0): ApplicationInfo? =
//  context.resolveApk(this.absolutePathString, flags)
//
///**
// * Converts the path-site to application info by given [packageManager] and returns it.
// *
// * @param flags additional option flags to modify the data returned.
// */
//inline fun PathSite.toApplicationInfo(
//  packageManager: PackageManager,
//  flags: Int = 0
//): ApplicationInfo? = packageManager.resolveApk(this.absolutePathString, flags)