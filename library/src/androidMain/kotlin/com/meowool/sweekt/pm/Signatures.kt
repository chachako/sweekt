@file:Suppress("NOTHING_TO_INLINE")
@file:JvmMultifileClass
@file:JvmName("PackageManager")

package com.meowool.sweekt.pm

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build

/**
 * Returns the signature array of this package manager.
 *
 * @param packageName the name of the package that needs to get signatures.
 * @author 凛 (RinOrz)
 */
@Suppress("DEPRECATION")
@SuppressLint("PackageManagerGetSignatures")
fun PackageManager.getSignatures(packageName: String): Array<Signature> = when {
  Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> this.getPackageInfo(
    packageName,
    PackageManager.GET_SIGNING_CERTIFICATES
  ).signingInfo.let {
    when {
      it.hasMultipleSigners() -> it.apkContentsSigners
      else -> it.signingCertificateHistory
    }
  }
  else -> this.getPackageInfo(
    packageName,
    PackageManager.GET_SIGNATURES
  ).signatures
}

/**
 * Returns the signature array from package manager by this context.
 *
 * @param packageName the name of the package that needs to get signatures.
 *
 * @see Context.getPackageManager for more details
 *
 * @author 凛 (RinOrz)
 */
inline fun Context.getSignatures(packageName: String): Array<Signature> =
  this.packageManager.getSignatures(packageName)