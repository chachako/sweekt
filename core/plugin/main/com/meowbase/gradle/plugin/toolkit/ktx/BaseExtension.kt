package com.meowbase.gradle.plugin.toolkit.ktx

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import java.io.File

/** 根据当前使用的 [BaseExtension.compileSdkVersion] 返回 android.jar */
val BaseExtension.androidJar get() = File(sdkDirectory, "platforms/$compileSdkVersion/android.jar")

/** 返回 android sdk 中的 platforms-tools 文件夹 */
val BaseExtension.platformToolsDir get() = File(sdkDirectory, "platforms-tools")

/** 返回 android sdk 中的正在使用的 build-tools 文件夹 */
val BaseExtension.buildToolsDir get() = File(sdkDirectory, "build-tools/$buildToolsVersion")

/** 返回 android sdk 中的 d8.jar */
val BaseExtension.d8Jar get() = File(buildToolsDir, "lib/d8.jar")

/** 返回 android sdk 中的 dx.jar */
val BaseExtension.dxJar get() = File(buildToolsDir, "lib/dx.jar")

/** 返回 android sdk 中的 apksigner.jar */
val BaseExtension.signerJar get() = File(buildToolsDir, "lib/apksigner.jar")

/** 确保 android sdk 文件夹中存在 d8.jar */
val BaseExtension.hasD8Jar get() = d8Jar.exists()

/** 返回 d8 或 dx jar 文件 */
val BaseExtension.d8OrDx get() = if (hasD8Jar) d8Jar else dxJar

/** 返回变体 */
val BaseExtension.applicationVariants get() = (this as? AppExtension)?.applicationVariants
val BaseExtension.libraryVariants get() = (this as? LibraryExtension)?.libraryVariants
val BaseExtension.testVariants get() = (this as? TestedExtension)?.testVariants