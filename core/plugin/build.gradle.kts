@file:Suppress("SpellCheckingInspection", "GradleDependency", "GradlePluginVersion")

import java.util.Properties

group = "com.mars.gradle.plugin"
val projectName = "toolkit"

plugins {
  kotlin; `kotlin-dsl`; java; `java-gradle-plugin`; `maven-publish`
  id("com.jfrog.bintray")
}

gradlePlugin {
  plugins.create(project.name) {
    id = "$group.$projectName"
    implementationClass = "$group.ToolkitPlugin"
  }
}

sourceSets["main"].java.srcDirs("kotlin")

dependencies {
  repositories { gradlePluginPortal() }
  compileOnly(gradleKotlinDsl())
  compileOnly(kotlin("gradle-plugin", version = "_"))
  compileOnly(kotlin("stdlib-jdk8", version = "_"))
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:_")
  implementation("org.jetbrains.dokka:dokka-android-gradle-plugin:_")
  implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:_")
  implementation("de.fayard.refreshVersions:refreshVersions:_")
  api("com.android.tools.build:gradle:_")
  api("org.koin:koin-core:_")
  api("org.koin:koin-core-ext:_")
  apiProjects(":core:jvm")
}

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("maven") {
        artifactId = projectName
        groupId = group.toString()
        version = fetchProperty("publish.version")
        from(project.components["java"])
        artifact(tasks.register<Jar>("sourcesJar") {
          from(sourceSets.main.allSource)
          archiveClassifier.set("sources")
        }.get())
      }
    }
  }

  bintray {
    key = Properties().run {
      load(rootProject.file("local.properties").reader())
      get("bintray.api.key")
    } as String
    user = fetchProperty("publish.author")
    pkg.apply {
      name = "gradle-$projectName"
      repo = fetchProperty("bintray.repository")
      version.name = fetchProperty("publish.version")
      websiteUrl = fetchProperty("bintray.websiteUrl")
      issueTrackerUrl = fetchProperty("bintray.issueTrackerUrl")
      vcsUrl = fetchProperty("bintray.vcsUrl")
      desc = fetchProperty("bintray.description")
      setLicenses(*fetchProperty("bintray.licenses").split(" ").toTypedArray())
    }
    publish = true
    setPublications("maven")
  }
}

fun fetchProperty(key: String): String = (findProperty(key) ?: rootProject.findProperty(key)).toString()