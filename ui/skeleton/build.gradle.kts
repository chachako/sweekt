plugins { `android-library`; `kotlin-android` }

android {
  setupAndroidWithShares()
  sourceSets.main {
    res.srcDirs("res")
    java.srcDirs("sources")
    manifest.srcFile("AndroidManifest.xml")
  }
}

dependencies {
  importSharedDependencies(this)
  apiProjects(":ui:runtime")
}

publishToBintray(artifact = "toolkit-ui-skeleton")