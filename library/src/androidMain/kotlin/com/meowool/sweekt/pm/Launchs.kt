@file:JvmMultifileClass
@file:JvmName("PackageManager")

package com.meowool.sweekt.pm

import android.content.Context


/**
 * Launch the given [packageName] of package.
 *
 * @param packageName the name of package to be launched.
 * @author 凛 (RinOrz)
 */
fun Context.launchPackage(packageName: String) =
  this.packageManager.getLaunchIntentForPackage(packageName)?.run(this::startActivity)