plugins { kotlin }

sourceSets {
  main.java.srcDir("main/kotlin")
  test {
    java.srcDir("test/kotlin")
    resources.srcDir("test/resources")
  }
}

dependencies {
  importSharedDependencies(this, "kotlin")
  api(Square.okio)
}

publishToBintray(artifact = "toolkit-core-jvm")