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

@file:OptIn(InternalMeowbaseApi::class)
@file:Suppress("SpellCheckingInspection")

plugins {
  kotlin; `java-gradle-plugin`
  id("org.gradle.kotlin.kotlin-dsl")
}

createMeowbasePlugin("toolkit")

sourceSets { main.java.srcDirs("main") }

repositories { gradlePluginPortal() }

dependencies {
  val agp = VersionsProperties.properties["version.com.android.tools.build..gradle"]
  compileOnlyApiOf(
    gradleKotlinDsl(),
    "org.jetbrains.kotlin:kotlin-gradle-plugin:_",
    "org.jetbrains.kotlin:kotlin-stdlib-jdk8:_",
    "de.fayard.refreshVersions:refreshVersions:_",
    "com.android.tools.build:gradle:$agp",
    "com.android.tools.build:builder:$agp",
    "com.android.tools.build:builder-model:$agp"
  )
  implementationOf(
    "org.jetbrains.dokka:dokka-gradle-plugin:_",
    "org.jetbrains.dokka:dokka-android-gradle-plugin:_",
    "com.jfrog.bintray.gradle:gradle-bintray-plugin:_",
    "com.jakewharton.android.repackaged:dalvik-dx:_",
    "com.google.guava:guava:_",
    "org.apache.commons:commons-compress:_"
  )
  apiOf(
    Meowbase.toolkit.core.jvm,
    Deps.asm.commons
  )
}

publishToBintray(
  group = "com.meowbase.plugin",
  artifact = "toolkit",
  packageName = "gradle-toolkit"
)