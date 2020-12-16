@file:Suppress("SpellCheckingInspection")

plugins { `android-library`; `kotlin-android` }

android {
  setupAndroidWithShares()
  sourceSets {
    main {
      manifest.srcFile("main/AndroidManifest.xml")
      java.srcDir("main/kotlin")
      res.srcDir("main/res")
    }
    test.java.srcDir("test/kotlin")
  }
}

dependencies {
  importSharedDependencies(this)
  implementationOf(
    AndroidX.viewPager2,
    AndroidX.recyclerView,
    Square.okHttp3.okHttp,
    Square.okio,
    Coil.base
  )
  apiOf(
    "com.orhanobut:logger:_",
    Deps.koin.androidX.ext
  )
  apiProjects(":core:jvm")
  compileOnlyProjects(":stubs")
}

publishToBintray(artifact = "toolkit-core-android")