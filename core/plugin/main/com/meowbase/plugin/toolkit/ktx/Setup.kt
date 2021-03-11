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

@file:Suppress(
  "PackageDirectoryMismatch", "SpellCheckingInspection",
  "unused", "UnstableApiUsage", "UNCHECKED_CAST",
  "NestedLambdaShadowedImplicitParameter"
)

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.maven
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File

/** 设置通用储存库 */
fun Project.setupRepositories(
  withSnapshots: Boolean = true,
  withKotlinEap: Boolean = false,
  withGradlePlugin: Boolean = false,
  withMeowbase: Boolean = false
) {
  repositories.apply {
    val projectRepoDir = rootDir.resolve(".repo")
    if (projectRepoDir.exists()) {
      maven(projectRepoDir.absolutePath)
    }
    google()
    jcenter()
    mavenCentral()
    maven("https://jitpack.io")
    if (withKotlinEap) maven("https://dl.bintray.com/kotlin/kotlin-eap")
    if (withMeowbase) maven("https://dl.bintray.com/oh-rin/meowbase")
    if (withGradlePlugin) gradlePluginPortal()
    if (withSnapshots) maven("https://oss.sonatype.org/content/repositories/snapshots/")
  }
}

/** 设置主项目与所有项目的通用储存库 */
fun Settings.setupRepositories(
  withSnapshots: Boolean = true,
  withKotlinEap: Boolean = false,
  withMeowbase: Boolean = false
) = gradle.allprojects {
  this.setupRepositories(withSnapshots, withKotlinEap, withMeowbase)
}

/**
 * 使用共享的通用配置来设置 Android 项目
 *
 * ```
 * android {
 *   setupAndroidWithShares()
 *   // 当然也可以调用其他原有的块内容
 *   sourceSets["main"].java.srcDirs("src/myKotlinSources")
 * }
 * ```
 *
 * @param applicationId 如果使用在 Android Application 的 build.gradle.kts 上
 * 则需要当前 app 的程序包名，当然也可以在 android {} 块中手动设置
 */
fun Project.setupAndroidWithShares(applicationId: String? = null) {
  val options = rootProject.extensions.findByType<ToolkitOptions>()
  val shared = options?.sharedAndroidConfig?.let { AndroidSharedConfig(this).apply(it) } ?: error(
    """
      
      使用 setupAndroidWithShares 前必须要在根目录的 settings.gradle.kts 中定义： 
      com.meowbase.plugin.toolkit.setupToolkit {
        // 定义想要让多个 Android 模块共用的配置
        shareAndroidConfig {
          ... 
        }
        ...
      }
      
    """.trimIndent()
  )

  extensions.findByType<BaseExtension>()?.apply {
    this as ExtensionAware
    compileSdkVersion(shared.compileSdkVersion)
    defaultConfig {
      applicationId?.apply(::applicationId)
      targetSdkVersion(shared.targetSdkVersion)
      minSdkVersion(shared.minSdkVersion)
      proguardFiles(*shared.appProguardFiles)
      consumerProguardFiles(*shared.libraryProguardFiles)
      testInstrumentationRunner = shared.testInstrumentationRunner
      shared.defaultConfig?.invoke(this)
    }

    val properties = project.localPropertiesOrNull ?: project.rootLocalPropertiesOrNull
    fun hasSignatureProperties() = properties?.let {
      it.contains("key.alias") && it.contains("key.file") && it.contains("key.password")
    } == true

    if (
      shared.debugSigningConfig != null ||
      shared.debugBuildType != null ||
      shared.releaseSigningConfig != null ||
      shared.releaseBuildType != null ||
      hasSignatureProperties()
    ) {
      buildTypes {
        getByName("debug") {
          shared.debugBuildType?.invoke(this)
          shared.debugSigningConfig?.also {
            signingConfig = signingConfigs.maybeCreate("debug").apply(it)
          } ?: apply {
            if (hasSignatureProperties()) signingConfig = signingConfigs.maybeCreate("debug").apply {
              storeFile = File(properties!!.getProperty("key.file"))
              keyAlias = properties.getProperty("key.alias")
              keyPassword = properties.getProperty("key.password")
              storePassword = properties.getProperty("key.password")
            }
          }
        }
        getByName("release") {
          shared.releaseBuildType?.invoke(this)
          (shared.releaseSigningConfig ?: shared.debugSigningConfig)?.also {
            signingConfig = signingConfigs.maybeCreate("release").apply(it)
          } ?: apply {
            signingConfigs.findByName("debug")?.also { signingConfig = it }
          }
        }
      }
    }

    sourceSets {
      main.java.srcDirs("src/main/kotlin")
      test.java.srcDirs("src/test/kotlin")
      androidTest.java.srcDirs("src/androidTest/kotlin")
    }

    this as MixinExtension

    buildFeatures {
      compose = shared.enableCompose
    }
    if (shared.enableCompose) {
      shared.composeOptions?.apply(::composeOptions) ?: composeOptions {
        shared.composeExtensionVersion?.also { kotlinCompilerExtensionVersion = it }
      }
    }
    shared.compileOptions?.apply(::compileOptions)
    options.kotlinJvmOptions?.invoke(extensions.getByName("kotlinOptions") as KotlinJvmOptions)
    when (this) {
      is LibraryExtension -> shared.libraryFullOptions?.invoke(this)
      is AppExtension -> shared.appFullOptions?.invoke(this)
    }
    shared.mixinFullOptions?.invoke(this)
  } ?: error("setupAndroidWithShares(...) 只能使用在 Android 工程的 build.gradle.kts 中！")
}

/** 为项目配置 JNI */
fun BaseExtension.jniConfig(
  ndkPath: File,
  ndkAbi: Array<String> = arrayOf("armeabi-v7a"),
  obfuscator: Array<String>? = null,
  cmakeLists: File = sourceSets["main"].jni.srcDirs.first().resolve("CMakeLists.txt")
) {
  this.ndkPath = ndkPath.absolutePath
  externalNativeBuild.cmake { path = cmakeLists }
  defaultConfig {
    ndk.abiFilters(*ndkAbi)
    externalNativeBuild.cmake.apply {
      if (obfuscator != null) {
        cFlags(*obfuscator, "-fvisibility=hidden")
        cppFlags(*obfuscator, "-fvisibility=hidden -fvisibility-inlines-hidden")
      }
    }
  }
}


@InternalMeowbaseApi
fun BaseExtension.jniMeowbaseConfig(
  ndkPath: File = meowbaseDir.resolve("ndk"),
  ndkAbi: Array<String> = arrayOf("armeabi-v7a"),
  obfuscator: Array<String>? = hikariFlags,
  cmakeLists: File = sourceSets["main"].jni.srcDirs.first().resolve("CMakeLists.txt")
) = jniConfig(ndkPath, ndkAbi, obfuscator, cmakeLists)