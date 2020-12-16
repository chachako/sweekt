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
    "de.fayard.refreshVersions:refreshVersions:_"
  )
  implementationOf(
    "org.jetbrains.dokka:dokka-gradle-plugin:_",
    "org.jetbrains.dokka:dokka-android-gradle-plugin:_",
    "com.jfrog.bintray.gradle:gradle-bintray-plugin:_",
    "com.jakewharton.android.repackaged:dalvik-dx:_",
    "com.google.guava:guava:_",
    "org.apache.commons:commons-compress:_",
    "com.android.tools.build:gradle:$agp",
    "com.android.tools.build:builder:$agp",
    "com.android.tools.build:builder-model:$agp"
  )
  apiOf(
    Meowbase.toolkit.core.jvm,
    Deps.asm.commons
  )
}

publishToBintray(
  group = "com.meowbase.gradle.plugin",
  artifact = "toolkit",
  packageName = "gradle-toolkit"
)

tasks.compileKotlin {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + "-Xallow-kotlin-package"
  }
}