@file:Suppress("DEPRECATION", "PrivateApi", "DiscouragedPrivateApi", "unused")

package com.mars.toolkit

import android.app.ActivityThread
import android.app.Application
import android.app.LoadedApk
import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Point
import com.mars.toolkit.data.AppData
import com.mars.toolkit.data.asAppData
import com.mars.toolkit.content.displayPixels
import com.mars.toolkit.content.versionCode
import com.mars.toolkit.content.windowManager
import org.koin.core.Koin
import org.koin.core.context.KoinContext
import org.koin.core.context.KoinContextHandler


/**
 * Fetch current global android [Context]
 * @throws IllegalStateException Unknown accident, current app may be crashed
 */
inline val appContext: Context
  get() = koin.get()
    ?: currentApplication
    ?: currentActivityThread?.application
    ?: currentActivityThread?.systemContext
    ?: createContext()
    ?: error("Without an application context, it not found at all! May should initialized 'appContext' property on Application")

/** Fetch current global application version */
var appVersionCode: Long = -999L
  get() = when (field) {
    -999L -> appContext.versionCode
    else -> field
  }

/**
 * Fetch the current application scree size with [appContext]
 * @return width: [Pair.first], height: [Pair.second]
 */
val appScreenSize: Pair<Int, Int>
  get() = Point().let {
    if (windowManager.defaultDisplay == null) return@let null
    windowManager.defaultDisplay.getRealSize(it)
    it.x to it.y
  } ?: appContext.displayPixels

/** Fetch the current application scree width with [appContext] */
val appScreenWidth: Int by lazy { appScreenSize.first }

/** Fetch the current application scree height with [appContext] */
val appScreenHeight: Int by lazy { appScreenSize.second }

/** Fetch the current application data with [appContext] */
inline val appData: AppData get() = appContext.packageName.asAppData

/** Fetch the current application package name with [appContext] */
inline val appPackageName: String get() = appContext.packageName

/** Fetch current global [ClassLoader] */
inline val appClassLoader: ClassLoader get() = appContext.classLoader

/** Fetch current [ActivityThread] of hidden-api */
inline val currentActivityThread: ActivityThread? get() = ActivityThread.currentActivityThread()

/** Fetch the current global [Application] instance of hidden-api */
inline val currentApplication: Application get() = ActivityThread.currentApplication()

/**
 * Fetch the current global Koin instance
 * but before get this property, [KoinContext] must ensure cannot be null
 * @throws KoinContextHandler.getContext
 */
inline val koin: Koin get() = KoinContextHandler.get()

/** Create a [Context] by hidden-api, which will talk the system global */
fun createContext(): Context? {
  val boundApplication = ActivityThread::class.java
    .getDeclaredField("mBoundApplication")
    .apply { isAccessible = true }
    .get(currentActivityThread)

  val loadedApkInfo = boundApplication.javaClass
    .getDeclaredField("info").apply { isAccessible = true }
    .get(boundApplication)

  val ctx = Class.forName("android.app.ContextImpl")
    .getDeclaredMethod("createAppContext", ActivityThread::class.java, LoadedApk::class.java)
    .apply { isAccessible = true }
    .invoke(null, currentActivityThread, loadedApkInfo)

  return ctx as? Context
}

fun getPackageInfo(packageName: String, flags: Int = 0): PackageInfo =
  appContext.packageManager.getPackageInfo(packageName, flags)