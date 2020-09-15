package com.vanniktech.maven.publish.custom.tasks

import org.gradle.jvm.tasks.Jar

@Suppress("UnstableApiUsage")
open class EmptySourcesJar : Jar() {

  init {
    archiveClassifier.set("sources")
  }
}
