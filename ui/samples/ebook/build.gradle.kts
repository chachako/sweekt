plugins { android; `kotlin-android`; `kotlin-android-extensions`; `kotlin-kapt` }

android { setupAndroidWithShares("com.rin.ui.samples.ebook") }

dependencies {
  importSharedDependencies(this)
  implementProjects(":ui:runtime", ":ui:extensions:coil")
}