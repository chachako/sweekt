package com.vanniktech.maven.publish.custom.tasks

import org.gradle.jvm.tasks.Jar

@Suppress("UnstableApiUsage")
open class EmptyJavadocsJar : Jar() {

  init {
    archiveClassifier.set("javadoc")
  }
}
