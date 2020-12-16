@file:OptIn(InternalMeowbaseApi::class)

plugins { kotlin; `java-gradle-plugin` }

createMeowbasePlugin("uikit")

sourceSets.main.java.srcDirs("main")

dependencies {
  testImplementationOf(Testing.junit4)
  implementationOf(
    Kotlin.stdlib.jdk8,
    KotlinX.coroutines.core,
    Meowbase.toolkit.core.plugin
  )
}

publishToBintray(
  artifact = "toolkit-ui-plugin",
  group = "com.meowbase.gradle.plugin"
)