@file:Suppress(
  "MemberVisibilityCanBePrivate",
  "HasPlatformType",
  "DEPRECATION",
  "PackageManagerGetSignatures"
)

package com.mars.toolkit.data

import android.content.Intent
import android.content.pm.PackageManager.GET_SIGNATURES
import android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES
import android.content.pm.Signature
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.P
import com.mars.toolkit.appContext
import com.mars.toolkit.getPackageInfo

/*
 * author: 凛
 * date: 2020/9/8 下午12:55
 * github: https://github.com/oh-Rin
 * description: 获取 App 的一些数据
 */
inline class AppData(val packageName: String) {
  val versionName get() = getPackageInfo(packageName).versionName

  val versionCode
    get() = with(getPackageInfo(packageName)) {
      if (SDK_INT < P) {
        versionCode.toLong()
      } else {
        longVersionCode
      }
    }

  /** 判断 App:[packageName] 是否已经安装 */
  val isInstalled: Boolean
    get() = try {
      getPackageInfo(packageName).applicationInfo != null
    } catch (e: Exception) {
      false
    }

  /** 获取 App:[packageName] 的签名 */
  val signatures: Array<Signature>
    get() = when {
      SDK_INT < P -> getPackageInfo(packageName, GET_SIGNATURES).signatures
      else -> getPackageInfo(packageName, GET_SIGNING_CERTIFICATES).signingInfo.apkContentsSigners
    }

  /** 获取 App:[packageName] 的简单签名 */
  val signature get() = signatures[0]

  /** 启动 App:[packageName] */
  fun launchApp() = appContext.packageManager
    .getLaunchIntentForPackage(packageName)
    ?.run(appContext::startActivity)


  /** 卸载 App:[packageName] */
  fun uninstallApp() = Intent(
    Intent.ACTION_DELETE,
    Uri.parse("package:$packageName")
  ).run(appContext::startActivity)
}

/** Example: "com.android.sample.app".asAppData */
inline val String.asAppData get() = AppData(this)