@file:OptIn(InternalMarsProjectApi::class)

plugins { kotlin; `java-gradle-plugin`; `kotlin-kapt` }

createMarsPlugin("uikit")

sourceSets["main"].java.srcDirs("main")

dependencies {
  testImplementationOf(Testing.junit4)
  implementationOf(
    Kotlin.stdlib.jdk8,
    KotlinX.coroutines.core,
    Mars.toolkit.core.plugin
  )
}

publishToBintray(artifact = "toolkit-ui-plugin")