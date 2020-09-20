@file:Suppress(
  "PackageDirectoryMismatch",
  "FunctionName",
  "UnstableApiUsage",
  "unused",
  "UNCHECKED_CAST", "SpellCheckingInspection"
)

import com.mars.gradle.plugin.toolkit.ToolkitPlugin
import de.fayard.refreshVersions.core.bootstrapRefreshVersionsCore
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/* Setup gradle toolkit core and https://github.com/jmfayard/refreshVersions */
fun Settings.setupToolkit(
  withRepoInit: Boolean = false,
  withOptions: (ToolkitOptions.() -> Unit)? = null
) {
  val global = ToolkitOptions().apply { withOptions?.invoke(this) }
  VersionsProperties.file = global.versionsPropertiesFile
    ?: this@setupToolkit.rootDir.resolve("versions.properties")
  // initialize refreshVersions
  bootstrapRefreshVersionsCore(versionsPropertiesFile = VersionsProperties.file)
  gradle.rootProject {
    apply<ToolkitPlugin>()
    extensions.configure<ToolkitOptions> {
      kotlinOptions {
        global.kotlinJvmOptions?.invoke(this)
        freeCompilerArgs = freeCompilerArgs + listOf(
          "-Xallow-jvm-ir-dependencies",
          "-Xskip-prerelease-check"
        )
      }
      sharedDependencies.putAll(global.sharedDependencies)
      if (sharedAndroidConfig == null) {
        sharedAndroidConfig = global.sharedAndroidConfig
      }
    }
    allprojects.forEach {
      it.afterEvaluate {
        if (withRepoInit) setupRepositories()
        extensions.findByType<SourceSetContainer>()?.apply {
          findByName("main")?.java?.srcDirs("src/main/kotlin")
          findByName("test")?.java?.srcDirs("src/test/kotlin")
        }
        extensions.findByType<MixinExtension>()?.apply {
          sourceSets {
            findByName("main")?.java?.srcDirs("src/main/kotlin")
            findByName("test")?.java?.srcDirs("src/test/kotlin")
            findByName("androidTest")?.java?.srcDirs("src/androidTest/kotlin")
          }
        }
        global.kotlinJvmOptions?.apply {
          (tasks.findByName("compileKotlin") as? KotlinCompile)?.kotlinOptions(this)
          (tasks.findByName("compileTestKotlin") as? KotlinCompile)?.kotlinOptions(this)
        }
      }
    }

    fixDependenciesLost()
  }
}

@InternalMarsProjectApi
fun Settings.setupMarsToolkit(withOptions: (ToolkitOptions.() -> Unit)? = null) {
  setupToolkit(false) {
    versionsPropertiesFile = marsProjectDir.resolve("versions.properties")
    kotlinOptions {
      useIR = true
      jvmTarget = "1.8"
      apiVersion = "1.4"
      languageVersion = "1.4"
      freeCompilerArgs = freeCompilerArgs + commonSuppressionArgs
    }
    withOptions?.invoke(this)
  }
  gradle.rootProject {
    allprojects {
      repositories.apply {
        maven(marsLocalInternalRepoPath)
        maven("https://dl.bintray.com/umsdk/release")
      }
      setupRepositories(
        withSnapshots = true,
        withKotlinEap = true,
        withMarsRepo = true
      )
    }
  }
}

/**
 * 加载 Kotlin 代码以解决导入插件时的报错：
 * Class '**' is compiled by a new Kotlin compiler backend and cannot be loaded by the old compiler
 * FIXME 等什么时候 Gradle 内置了 1.4 版本的 Kotlin Script 后才能够删除这个方法（该死的 Gradle）
 */
fun Project.fixOldCompilerWarn() {
  dependencies.add("implementation", "org.jetbrains.kotlin:kotlin-gradle-plugin")
}

/* Fixed: https://github.com/jmfayard/refreshVersions/issues/244 */
private fun Project.fixDependenciesLost() {
  tasks.findByName("refreshVersions")?.apply {
    doFirst { VersionsProperties.last = VersionsProperties.current }
    doLast {
      val old = VersionsProperties.last
      val new = VersionsProperties.current.toMutableList()
      var locked = true
      // 对比旧的与新的 versions.properties 中旧文件多出来的依赖
      old.forEach { line ->
        // 如果到达了下一个依赖组
        if (!line.startsWith("#") && line.contains("=")) {
          // 代表依赖组后面的 "available" 数据不能够记录了
          locked = true
        }
        // 如果依赖组丢失则记录，并且开锁
        if (!new.contains(line)) {
          new.add("\n" + line)
          // 代表依赖组后面的 "available" 数据可以被记录
          locked = false
        }
        // 允许记录当前依赖组的 "available" 数据
        if (!locked && line.contains("# available=")) new.add(line)
      }
      // 将丢失的依赖放置到新文件中
      VersionsProperties.file.writeText(new.joinToString("\n"))
      VersionsProperties.last = new
    }
  }
}
