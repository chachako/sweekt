/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

@file:Suppress("DEPRECATION", "PrivateApi", "DiscouragedPrivateApi", "unused")
@file:JvmName("AppGlobal")

package com.meowbase.toolkit

import android.app.ActivityThread
import android.app.Application
import android.app.LoadedApk
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.Point
import com.meowbase.toolkit.data.AppData
import com.meowbase.toolkit.data.asAppData
import com.meowbase.toolkit.content.displayPixels
import com.meowbase.toolkit.content.versionCode
import com.meowbase.toolkit.content.windowManager
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.context.KoinContext
import org.koin.core.context.startKoin

/**
 * Fetch current global android [Context]
 * @throws IllegalStateException Unknown accident, current app may be crashed
 */
val appContext: Context
  get() = appContextOrNull
    ?: error("Application context not found! You must be initialize context with koin `startKoin { androidContext(..) }`")

/**
 * Fetch current global android [Context] if available
 * @return context instance or null
 */
inline val appContextOrNull: Context?
  get() = koinOrNull?.getOrNull()
    ?: currentApplication
    ?: currentActivityThread?.application

/** Fetch variant of current application */
val appDebugging: Boolean by lazy {
  appContext.applicationInfo.flags.hasFlags(ApplicationInfo.FLAG_DEBUGGABLE)
}

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

/** Fetch the current global [Application] instance of hidden-api, if available */
inline val currentApplication: Application? get() = ActivityThread.currentApplication()

/**
 * Fetch the current global Koin instance
 * but before getting this property, [startKoin] must ensure called
 * @throws GlobalContext.get
 * @see koinOrNull
 */
inline val koin: Koin get() = GlobalContext.get()

/**
 * Fetch the current global Koin instance
 * it is always null before the [startKoin] called
 * @see koin
 */
inline val koinOrNull: Koin? get() = GlobalContext.getOrNull()

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