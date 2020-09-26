plugins { android; `kotlin-android`; `kotlin-android-extensions` }

android {
  setupAndroidWithShares("com.rin.ui.samples.skeleton")
  packagingOptions {
    pickFirst("META-INF/jvm.kotlin_module")
  }
}

dependencies {
  importSharedDependencies(this)
  implementProjects(":ui:runtime", ":ui:animation")
}