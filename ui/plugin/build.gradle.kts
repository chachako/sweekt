@file:OptIn(InternalMarsProjectApi::class)

plugins { kotlin; `java-gradle-plugin` }

createMarsPlugin("uikit")

sourceSets.main.java.srcDirs("main")

dependencies {
  testImplementationOf(Testing.junit4)
  implementationOf(
    Kotlin.stdlib.jdk8,
    KotlinX.coroutines.core,
    Mars.toolkit.core.plugin
  )
}

publishToBintray(
  artifact = "toolkit-ui-plugin",
  group = "com.mars.gradle.plugin"
)