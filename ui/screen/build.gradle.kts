plugins { `android-library`; `kotlin-android` }

android {
  setupAndroidWithShares()
  sourceSets {
    main {
      res.srcDirs("res")
      java.srcDirs("main")
      manifest.srcFile("AndroidManifest.xml")
    }
    test.java.srcDirs("test")
  }
}

dependencies {
  importSharedDependencies(this)
  apiProjects(":ui:runtime")
  apiOf(Mars.conductor)
}

publishToBintray(artifact = "toolkit-ui-screen")