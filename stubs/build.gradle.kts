plugins { `android-library`; `kotlin-android` }

android {
  setupAndroidWithShares()
  sourceSets.main {
    java.srcDir("sources/kotlin")
    manifest.srcFile("sources/AndroidManifest.xml")
  }
}

dependencies {
  importSharedDependencies(this)
  importLibs("api", this)
}

publishToBintray(artifact = "toolkit-stubs")