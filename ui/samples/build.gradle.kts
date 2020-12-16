plugins {
  android; `kotlin-android`; `kotlin-android-extensions`
  id("com.meowbase.gradle.plugin.uikit")
}

android { setupAndroidWithShares("com.rin.ui.samples") }

dependencies {
  importSharedDependencies(this)
  implementProjects(
    ":ui:runtime",
    ":ui:skeleton",
    ":ui:extensions:coil"
  )
}