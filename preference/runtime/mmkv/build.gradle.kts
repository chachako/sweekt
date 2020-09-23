@file:OptIn(InternalMarsProjectApi::class)

plugins { `android-library`; `kotlin-android`; `kotlin-kapt` }

android {
  setupAndroidWithShares()
  sourceSets.main {
    java.srcDirs("native/sources")
    manifest.srcFile("native/AndroidManifest.xml")
  }
}

dependencies {
  importSharedDependencies(this)
  apiOf(
    Tencent.mmkv.static,
    project(":preference:runtime:base")
  )
}

publishToBintray(artifact = "toolkit-preference-mmkv")