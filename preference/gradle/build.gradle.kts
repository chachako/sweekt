plugins { kotlin; java; `java-gradle-plugin` }

createPlugin("preference")

sourceSets {
  main.java.srcDir("main")
  test.java.srcDir("test")
}

dependencies {
  apiProjects(":kotlin-compiler")
  implementationOf(
    Kotlin.stdlib.jdk8,
    KotlinX.coroutines.core,
    Mars.toolkit.core.plugin,
    Deps.javassist
  )
  testImplementationOf(Testing.junit4)
}

publishToBintray(
  artifact = "toolkit-preference-plugin",
  group = "com.mars.gradle.plugin"
)