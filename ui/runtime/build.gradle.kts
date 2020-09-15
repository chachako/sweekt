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
  apiProjects(":core:android")
  apiOf(
    AndroidX.dynamicAnimationKtx,
    Google.android.material
  )
}

publishToBintray(artifact = "toolkit-ui-runtime")