plugins {
  android; `kotlin-android`; `kotlin-android-extensions`
  id("com.mars.gradle.plugin.uikit")
}

android {
  setupAndroidWithShares("com.rin.ui.samples")
  packagingOptions {
    pickFirst("META-INF/jvm.kotlin_module")
  }
}

dependencies {
  importSharedDependencies(this)
  implementProjects(
    ":ui:runtime",
    ":ui:skeleton",
    ":ui:extensions:coil"
  )
}