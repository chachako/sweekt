@file:OptIn(InternalMarsProjectApi::class)
@file:Suppress("SpellCheckingInspection")

plugins { kotlin; `kotlin-dsl`; java; `java-gradle-plugin` }

createMarsPlugin("toolkit")

sourceSets { main.java.srcDirs("main") }

repositories { gradlePluginPortal() }

dependencies {
  implementationOf(
    "org.jetbrains.dokka:dokka-gradle-plugin:_",
    "org.jetbrains.dokka:dokka-android-gradle-plugin:_",
    "com.jfrog.bintray.gradle:gradle-bintray-plugin:_",
    "de.fayard.refreshVersions:refreshVersions:_"
  )
  apiOf(
    gradleKotlinDsl(),
    kotlin("gradle-plugin", "_"),
    kotlin("stdlib-jdk8", "_"),
    "com.android.tools.build:gradle:_",
    Mars.toolkit.core.jvm
  )
}

publishToBintray(
  group = "com.mars.gradle.plugin",
  artifact = "toolkit",
  packageName = "gradle-toolkit"
)