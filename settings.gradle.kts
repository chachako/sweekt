@file:[Suppress(
  "UNCHECKED_CAST", "GradleDynamicVersion", "UnstableApiUsage",
  "SpellCheckingInspection", "SafeCastWithReturn",
  "NestedLambdaShadowedImplicitParameter"
) OptIn(InternalMarsProjectApi::class)]

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
    maven("https://dl.bintray.com/oh-rin/Mars/")
    maven("https://dl.bintray.com/kotlin/kotlin-eap/")
  }

  listOf(
    "com.mars.gradle.plugin:toolkit:0.6.2",
    "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5",
    "de.fayard.refreshVersions:refreshVersions:0.9.5",
    dep("org.jetbrains.kotlin", "kotlin-gradle-plugin"),
    dep("com.android.tools.build", "gradle")
  ).forEach { dependencies.classpath(it) }
}

setupMarsToolkit {
  shareMarsAndroidConfig {
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
      KotlinX.coroutines.core
    )
    testImplementationOf(
      Testing.junit4
    )
  }
}

importProject(rootDir, "")