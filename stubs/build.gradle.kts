plugins { `android-library`; `kotlin-android` }

android {
  setupAndroidWithShares()
  sourceSets.main {
    java.srcDirs("sources/kotlin")
    manifest.srcFile("sources/AndroidManifest.xml")
  }
}

dependencies {
  importSharedDependencies(this)
  importLibs("api", this)
  implementation(AndroidX.annotation)
}

publishToBintray(artifact = "toolkit-stubs")