@file:[Suppress(
  "UNCHECKED_CAST", "GradleDynamicVersion", "UnstableApiUsage",
  "SpellCheckingInspection", "SafeCastWithReturn",
  "NestedLambdaShadowedImplicitParameter"
) OptIn(InternalMarsProjectApi::class)]

buildscript {
  // parse versions.properties file and collect to Map<String, String>
  val versions = File(System.getenv("MARS_PROJECT_ROOT"), "versions.properties").readLines()
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
    // FIXME 当 gradle 内置 1.4 版本的 kotlin 后我们才能够更改这里的版本号
    "com.mars.gradle.plugin:toolkit:0.3.38",
    "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5",
    "de.fayard.refreshVersions:refreshVersions:0.9.5",
    dep("org.jetbrains.kotlin", "kotlin-gradle-plugin"),
    dep("com.android.tools.build", "gradle", "plugin.android")
  ).forEach { dependencies.classpath(it) }
}

setupMarsToolkit(addCleanTask = false)