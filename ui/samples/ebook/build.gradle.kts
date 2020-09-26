plugins { android; `kotlin-android`; `kotlin-android-extensions`; `kotlin-kapt` }

android {
  setupAndroidWithShares("com.rin.ui.samples.ebook")
  packagingOptions {
    pickFirst("META-INF/jvm.kotlin_module")
  }
}

dependencies {
  importSharedDependencies(this)
  implementProjects(":ui:runtime", ":ui:animation", ":ui:extensions:coil")
}