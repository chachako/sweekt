rootProject.name = "sweekt"

plugins {
  id("com.meowool.toolkit.gradle-dsl-x") version "2.1"
}

buildscript {
  repositories {
    mavenCentral()
    google()
  }
  configurations.all {
    resolutionStrategy.eachDependency {
      if (requested.group.startsWith("androidx.compose")) {
        useVersion("1.0.0-beta08")
      }
    }
  }
}

rootGradleDslX {
  useMeowoolSpec()
  allprojects {
    useExperimentalAnnotations("com.meowool.sweekt.InternalSweektApi")
    afterEvaluate {
      dokka(DokkaFormat.Html) {
        outputDirectory.set(rootDir.resolve("docs/apis"))
      }
    }
  }
}

importProjects(rootDir)