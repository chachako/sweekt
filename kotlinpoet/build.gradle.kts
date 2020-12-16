@file:Suppress("SpellCheckingInspection")

plugins { kotlin }

sourceSets.main.java.srcDirs("main/kotlin")

dependencies {
  importSharedDependencies(this, "kotlin")
  compileOnly(Deps.kspApi)
  apiOf(
    Square.kotlinPoet,
    Meowbase.toolkit.core.jvm
  )
}

publishToBintray(artifact = "toolkit-kotlinpoet")