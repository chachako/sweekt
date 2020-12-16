@file:Suppress(
  "MemberVisibilityCanBePrivate", "UnstableApiUsage",
  "SpellCheckingInspection", "FunctionName", "PackageDirectoryMismatch"
)

import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File

/*
 * author: 凛
 * date: 2020/9/3 11:06 下午
 * github: https://github.com/RinOrz
 * description: 亿点点提高生产力的配置
 */
class ToolkitOptions {
  /* https://github.com/jmfayard/refreshVersions 的文件路径 */
  var versionsPropertiesPath: String? = null

  /* https://github.com/jmfayard/refreshVersions 的文件实例 */
  var versionsPropertiesFile: File? = null
    get() = field ?: versionsPropertiesPath?.let(::File)

  internal val sharedDependencies = mutableMapOf<String, DependencyHandler.() -> Unit>()
  internal var sharedAndroidConfig: (AndroidSharedConfig.() -> Unit)? = null
  internal var kotlinJvmOptions: (KotlinJvmOptions.() -> Unit)? = null

  /**
   * 共享一个通用的依赖代码块
   * NOTE: 这在多模块开发时，且每个模块都拥有多个相同的依赖时非常有用，能够大幅度提升代码简洁性
   * 🌰:
   * ```
   * shareDependencies {
   *   // 定义通用的依赖
   *   implementationOf(
   *     Kotlin.stdlib.jdk8,
   *     AndroidX.appCompat,
   *   )
   * }
   * ```
   * @see importSharedDependencies
   */
  fun shareDependencies(block: DependencyHandler.() -> Unit) {
    shareDependencies("_main", block)
  }

  /**
   * 共享一个通用范围的依赖代码块
   * NOTE: 与 [shareDependencies] 不同的是，它可以定义多个不同区域的依赖块，并在其他位置调用任意的依赖块
   * 🌰:
   * ```
   * shareDependencies("kt") {
   *   implementationOf(
   *     Kotlin.stdlib.jdk8,
   *     KotlinX.reflect.lite,
   *     KotlinX.coroutines.jdk8
   *   )
   * }
   * shareDependencies("core-app") {
   *   implementationOf(
   *     Kotlin.stdlib.jdk8,
   *     AndroidX.appCompat,
   *   )
   * }
   * ```
   * @param scope 区域 ID
   * @see shareDependencies
   * @see importSharedDependencies
   */
  fun shareDependencies(scope: String, block: DependencyHandler.() -> Unit) {
    sharedDependencies[scope] = block
  }

  /**
   * 共享一个通用的 Android 项目配置块
   * NOTE: 多模块开发时，模块都需要一个 android { ... }, 但其实大部分都是通用的
   * 解耦这些配置到全局中能够大幅度提升代码简洁性
   * 🌰:
   * ```
   * android {
   *   setupAndroidWithShares()
   *   // 当然也可以调用原有的块内容
   *   sourceSets["main"].java.srcDirs("src/myKotlinSources")
   * }
   * ```
   * @see setupAndroidWithShares
   */
  fun shareAndroidConfig(block: AndroidSharedConfig.() -> Unit) {
    sharedAndroidConfig = block
  }

  /**
   * 设置所有项目的 Kotlin 配置
   * @see KotlinJvmOptions
   */
  fun kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    val old = kotlinJvmOptions
    kotlinJvmOptions = {
      old?.invoke(this)
      block()
    }
  }

  /**
   * 启用最新的 Kotlin 版本
   * @see kotlinOptions
   */
  fun enableBestKotlinVersion() {
    val old = kotlinJvmOptions
    kotlinJvmOptions = {
      old?.invoke(this)
      jvmTarget = BestJvmTargetVersion
      apiVersion = BestKotlinVersion
      languageVersion = BestKotlinVersion
    }
  }

  @InternalMeowbaseApi
  fun shareMeowbaseAndroidConfig(
    ndkPath: File = meowbaseDir.resolve("ndk"),
    ndkAbi: Array<String> = arrayOf("armeabi-v7a"),
    obfuscator: Array<String>? = null,
    fullConfig: (AndroidSharedConfig.() -> Unit)? = null
  ) {
    shareAndroidConfig {
      baseExtensionOptions {
        this.ndkPath = ndkPath.absolutePath
        appProguard(
          getDefaultProguardFile("proguard-android-optimize.txt"),
          project.rootDir.resolve("proguard-rules.pro")
        )
        libraryProguard(project.rootDir.resolve("consumer-rules.pro"))
        packagingOptions {
          pickFirst("META-INF/kotlinx-io.kotlin_module")
          pickFirst("META-INF/kotlinx-coroutines-io.kotlin_module")
        }
        sourceSets["main"].jni.srcDirs.forEach {
          val cmakeLists = it.resolve("CMakeLists.txt")
          if (cmakeLists.exists()) {
            jniMeowbaseConfig(ndkPath, ndkAbi, obfuscator, cmakeLists)
          }
        }
      }
      debugSignature {
        project.keyProperties.also {
          keyAlias = it.getProperty("key.alias")
          keyPassword = it.getProperty("key.password")
          storePassword = it.getProperty("key.password")
          storeFile = File(
            it.getProperty("key.file")
              .replace("\${MEOW_BASE}", meowbasePath)
          )
        }
      }
      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
      }
      fullConfig?.invoke(this)
    }
  }
}