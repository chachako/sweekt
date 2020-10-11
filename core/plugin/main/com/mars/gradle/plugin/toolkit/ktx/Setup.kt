@file:Suppress(
  "PackageDirectoryMismatch", "SpellCheckingInspection",
  "unused", "UnstableApiUsage", "UNCHECKED_CAST",
  "NestedLambdaShadowedImplicitParameter"
)

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
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
  withMarsRepo: Boolean = false
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
    if (withMarsRepo) maven("https://dl.bintray.com/oh-rin/Mars")
    if (withSnapshots) maven("https://oss.sonatype.org/content/repositories/snapshots/")
  }
}

/** 设置主项目与所有项目的通用储存库 */
fun Settings.setupRepositories(
  withSnapshots: Boolean = true,
  withKotlinEap: Boolean = false,
  withMarsRepo: Boolean = false
) {
  gradle.allprojects {
    setupRepositories(withSnapshots, withKotlinEap, withMarsRepo)
  }
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
      com.mars.gradle.plugin.toolkit.setupToolkit {
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
    buildTypes {
      getByName("debug") {
        shared.debugBuildType?.invoke(this)
        signingConfig = shared.debugSigningConfig?.let {
          signingConfigs["debug"].apply(it)
        } ?: (project.localPropertiesOrNull ?: project.rootLocalPropertiesOrNull)?.let {
          it.getProperty("key.alias") ?: return@let null
          signingConfigs.getByName("debug") {
            storeFile = File(it.getProperty("key.file"))
            keyAlias = it.getProperty("key.alias")
            keyPassword = it.getProperty("key.password")
            storePassword = it.getProperty("key.password")
          }
        }
      }
      getByName("release") {
        shared.releaseBuildType?.invoke(this)
        signingConfig = shared.releaseSigningConfig?.let {
          signingConfigs["release"].apply(it)
        } ?: signingConfigs.findByName("debug")
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
    shared.compileOptions?.apply(::compileOptions)
    shared.composeOptions?.apply(::composeOptions) ?: composeOptions {
      shared.composeCompilerVersion?.also { kotlinCompilerVersion = it }
      shared.composeExtensionVersion?.also { kotlinCompilerExtensionVersion = it }
    }
    options.kotlinJvmOptions?.invoke(extensions.getByName("kotlinOptions") as KotlinJvmOptions)
    when (this) {
      is LibraryExtension -> shared.libraryFullOptions?.invoke(this)
      is BaseAppModuleExtension -> shared.appFullOptions?.invoke(this)
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


@InternalMarsProjectApi
fun BaseExtension.jniMarsConfig(
  ndkPath: File = marsProjectDir.resolve("ndk"),
  ndkAbi: Array<String> = arrayOf("armeabi-v7a"),
  obfuscator: Array<String>? = hikariFlags,
  cmakeLists: File = sourceSets["main"].jni.srcDirs.first().resolve("CMakeLists.txt")
) = jniConfig(ndkPath, ndkAbi, obfuscator, cmakeLists)