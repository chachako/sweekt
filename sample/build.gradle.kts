plugins {
  id("com.android.application")
  kotlin("android")
}

group = "com.meowool.toolkit"
version = "1.0-SNAPSHOT"

dependencies {
  implementation(project(":library"))
  implementation("androidx.appcompat:appcompat:1.2.0")
  implementation("com.google.android.material:material:1.3.0")
  implementation("androidx.constraintlayout:constraintlayout:2.0.4")
}

android {
  compileSdkVersion(29)
  defaultConfig {
    applicationId = "com.meowool.toolkit.sample"
    minSdkVersion(24)
    targetSdkVersion(29)
    versionCode = 1
    versionName = "1.0"
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
}