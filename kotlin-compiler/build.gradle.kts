@file:Suppress("SpellCheckingInspection")

plugins { kotlin }

sourceSets.main.java.srcDirs("main/kotlin")

dependencies {
  importSharedDependencies(this, "kotlin")
  apiOf(
    kotlin("compiler-embeddable", "_"),
    Square.okio
  )
}

publishToBintray(artifact = "toolkit-ktcompiler")