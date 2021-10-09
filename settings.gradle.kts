rootProject.name = "sweekt"

pluginManagement {
  repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  id("com.meowool.gradle.toolkit") version "0.1.0-SNAPSHOT"
}

buildscript {
  configurations.all {
    // Check for updates every build
    resolutionStrategy {
      force("com.android.tools.build:gradle:4.2.1")
      cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
  }
}

gradleToolkit {
  rootProject.buildscript { repositories { mavenCentral() }}
  useMeowoolSpec()
  allprojects {
    optIn("com.meowool.sweekt.InternalSweektApi")
    dokka(DokkaFormat.Html) {
      outputDirectory.set(rootDir.resolve("docs/apis"))
    }
  }
  publications {
    data {
      val baseVersion = "0.1.0"
      version = "$baseVersion-LOCAL"
      // Used to publish non-local versions of artifacts in CI environment
      versionInCI = "$baseVersion-SNAPSHOT"

      displayName = "Sweekt"
      artifactId = "sweekt"
      groupId = "com.meowool.toolkit"
      description = "A common toolkit (utils) built to help you further reduce Kotlin boilerplate code and improve development efficiency."
      url = "https://github.com/meowool-toolkit/${rootProject.name}"
      vcs = "$url.git"
      developer {
        id = "rin"
        name = "Rin Orz"
        url = "https://github.com/RinOrz/"
      }
    }
  }
}

importProjects(rootDir)

// Only set in the CI environment, waiting the issue to be fixed:
// https://youtrack.jetbrains.com/issue/KT-48291
if (isCiEnvironment) extra["kotlin.mpp.enableGranularSourceSetsMetadata"] = true