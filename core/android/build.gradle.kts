/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

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