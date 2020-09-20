@file:OptIn(InternalMarsProjectApi::class)
@file:Suppress("SpellCheckingInspection")

plugins {
  kotlin; `java-gradle-plugin`
  id("org.gradle.kotlin.kotlin-dsl")
}

createMarsPlugin("toolkit")

sourceSets { main.java.srcDirs("main") }

repositories { gradlePluginPortal() }

dependencies {
  compileOnlyApiOf(
    gradleKotlinDsl(),
    "org.jetbrains.kotlin:kotlin-gradle-plugin:_",
    "org.jetbrains.kotlin:kotlin-stdlib-jdk8:_",
    VersionsProperties.resolveDependency("com.android.tools.build", "gradle")
  )
  implementationOf(
    "org.jetbrains.dokka:dokka-gradle-plugin:_",
    "org.jetbrains.dokka:dokka-android-gradle-plugin:_",
    "com.jfrog.bintray.gradle:gradle-bintray-plugin:_",
    "de.fayard.refreshVersions:refreshVersions:_"
  )
  apiOf(Mars.toolkit.core.jvm)
}

publishToBintray(
  group = "com.mars.gradle.plugin",
  artifact = "toolkit",
  packageName = "gradle-toolkit"
)