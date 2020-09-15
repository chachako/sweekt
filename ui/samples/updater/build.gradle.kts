plugins { android; `kotlin-android`; `kotlin-android-extensions`; `kotlin-kapt` }

android {
  setupAndroidWithShares("com.dfkkextra.updater")
//  buildTypes["release"].isMinifyEnabled = true
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerVersion = "1.4.0"
    kotlinCompilerExtensionVersion = "1.0.0-alpha02"
  }
  kotlinOptions {
    jvmTarget = "1.8"
    useIR = true
  }
}

dependencies {
  importSharedDependencies(this)
  implementationOf(
    Square.moshi,
    Square.okHttp3.okHttp,
    Square.retrofit2.retrofit,
    Square.retrofit2.converter.moshi,
    AndroidX.compose.ui,
    AndroidX.compose.foundation,
    AndroidX.compose.material,
    AndroidX.ui.tooling,
    Google.android.material,
    Mars.toolkit.core.android
  )
  kaptOf(Square.moshi.kotlinCodegen)
}