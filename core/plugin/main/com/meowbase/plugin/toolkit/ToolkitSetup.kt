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
  "PackageDirectoryMismatch",
  "FunctionName",
  "UnstableApiUsage",
  "unused",
  "UNCHECKED_CAST", "SpellCheckingInspection"
)

import com.meowbase.plugin.toolkit.ToolkitPlugin
import de.fayard.refreshVersions.bootstrapRefreshVersions
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


/* Setup gradle toolkit core and https://github.com/jmfayard/refreshVersions */
fun Settings.setupToolkit(
  withAndroid: Boolean = true,
  withRepoInit: Boolean = true,
  withOptions: (ToolkitOptions.() -> Unit)? = null
) {
  val global = ToolkitOptions().apply {
    withOptions?.invoke(this)
//    kotlinOptions {
//      /** @see fixOldCompilerWarn */
//      freeCompilerArgs = freeCompilerArgs + arrayOf(
//        "-Xallow-jvm-ir-dependencies",
//        "-Xskip-prerelease-check"
//      )
//    }
  }
  VersionsProperties.file = global.versionsPropertiesFile
    ?: this@setupToolkit.rootDir.resolve("versions.properties")
  // initialize refreshVersions
  bootstrapRefreshVersions(versionsPropertiesFile = VersionsProperties.file)
  gradle.rootProject {
    apply<ToolkitPlugin>()
    extensions.configure<ToolkitOptions> {
      kotlinJvmOptions = global.kotlinJvmOptions
      sharedDependencies.putAll(global.sharedDependencies)
      if (sharedAndroidConfig == null) {
        sharedAndroidConfig = global.sharedAndroidConfig
      }
    }
    allprojects {
      afterEvaluate {
        if (withRepoInit) setupRepositories()
        extensions.findByType<SourceSetContainer>()?.apply {
          findByName("main")?.java?.srcDirs("src/main/kotlin")
          findByName("test")?.java?.srcDirs("src/test/kotlin")
        }
        if (withAndroid) extensions.findByType<MixinExtension>()?.apply {
          sourceSets {
            findByName("main")?.java?.srcDirs("src/main/kotlin")
            findByName("test")?.java?.srcDirs("src/test/kotlin")
            findByName("androidTest")?.java?.srcDirs("src/androidTest/kotlin")
          }
        }
        global.kotlinJvmOptions?.also { kotlinOptions ->
          tasks.withType<KotlinCompile>().configureEach {
            kotlinOptions(kotlinOptions)
          }
        }
      }
    }

    fixDependenciesLost()
  }
}

@InternalMeowbaseApi
fun Settings.setupMeowbaseToolkit(
  withAndroid: Boolean = true,
  withOptions: (ToolkitOptions.() -> Unit)? = null
) {
  setupToolkit(withAndroid) {
    // TODO 当所有项目共用 versions.properties 文件时
    // TODO 并不容易将此文件一并与当前项目 Commit 到 Git 仓库
    // TODO 这不利于多人协作，但可能我会在以后想到一个好的点子来解决这个问题
//    versionsPropertiesFile = meowbaseProjectDir.resolve("versions.properties")
    kotlinOptions {
      useIR = true
      jvmTarget = BestJvmTargetVersion
      apiVersion = BestKotlinVersion
      languageVersion = BestKotlinVersion
      freeCompilerArgs = freeCompilerArgs + CommonSuppressionArgs
    }
    withOptions?.invoke(this)
  }
  gradle.rootProject {
    allprojects {
      repositories.apply {
        maven(meowbaseInternalLocalRepoPath)
      }
      setupRepositories(
        withGradlePlugin = true,
        withSnapshots = true,
        withKotlinEap = true,
        withMeowbase = true
      )
    }
  }
}

/**
 * Implementation Kotlin 插件以解决导入插件时的报错：
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
