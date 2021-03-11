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

@file:[Suppress(
  "UNCHECKED_CAST", "GradleDynamicVersion", "UnstableApiUsage",
  "SpellCheckingInspection", "SafeCastWithReturn",
  "NestedLambdaShadowedImplicitParameter"
) OptIn(InternalMeowbaseApi::class)]

pluginManagement {
  repositories {
    gradlePluginPortal()
    jcenter()
    google()
    maven(rootDir.resolve(".repo").absolutePath)
  }
}

buildscript {
  // parse versions.properties file and collect to Map<String, String>
  val versions = rootDir.resolve("versions.properties").readLines()
    .filter { it.contains("=") && !it.startsWith("#") }
    .map { it.substringBeforeLast("=") to it.substringAfterLast("=") }
    .toMap()

  // find newest dependency from the versions.properties file
  fun dep(group: String, artifact: String, versionKey: String? = null) =
    "$group:$artifact:" + versions[versionKey ?: "version.$group..$artifact"]

  repositories {
    gradlePluginPortal()
    jcenter()
    google()
    maven("https://dl.bintray.com/oh-rin/meowbase")
    maven(rootDir.resolve(".repo").absolutePath)
  }

  listOf(
    "com.meowbase.plugin:toolkit:0.1.8",
    "com.meowbase.plugin:toolkit-ui-plugin:0.1.4",
    "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5",
    dep("de.fayard.refreshVersions", "refreshVersions"),
    dep("org.jetbrains.kotlin", "kotlin-gradle-plugin"),
    dep("com.android.tools.build", "gradle"),
  ).forEach { dependencies.classpath(it) }
}

setupMeowbaseToolkit {
  shareMeowbaseAndroidConfig {
    compileSdkVersion = 30
    targetSdkVersion = 30
    minSdkVersion = 21
  }
  shareDependencies {
    implementationOf(
      Kotlin.stdlib.jdk8,
      KotlinX.coroutines.core,
      KotlinX.coroutines.android,
      AndroidX.appCompat,
      AndroidX.core,
      AndroidX.core.ktx,
      AndroidX.lifecycle.commonJava8,
      AndroidX.lifecycle.runtimeKtx,
      AndroidX.lifecycle.viewModelKtx,
      AndroidX.lifecycle.liveDataCoreKtx
    )
    testImplementationOf(
      Testing.junit4,
      AndroidX.test.runner,
      AndroidX.test.coreKtx,
      AndroidX.test.ext.truth,
      AndroidX.test.ext.junitKtx
    )
  }
  shareDependencies("kotlin") {
    implementationOf(
      Kotlin.stdlib.jdk8,
      KotlinX.coroutines.core,
      KotlinX.coroutines.jdk8
    )
    testImplementationOf(
      Testing.junit4
    )
  }
}

importProject(rootDir, "")