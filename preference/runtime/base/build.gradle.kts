@file:OptIn(InternalMarsProjectApi::class)

plugins { `android-library`; `kotlin-android`; `kotlin-kapt` }

android {
  setupAndroidWithShares()
  sourceSets {
    main {
      java.srcDirs("native/main/sources")
      manifest.srcFile("native/main/AndroidManifest.xml")
    }
    test {
      java.srcDirs("native/test/sources")
      resources.srcDirs("native/test/resources")
    }
  }
}

dependencies {
  importSharedDependencies(this)
  compileOnlyProjects(":stubs")
  apiProjects(":core:android")
}

publishToBintray(artifact = "toolkit-preference-runtime-base")