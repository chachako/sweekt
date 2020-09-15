package com.vanniktech.maven.publish.custom.tasks

import com.vanniktech.maven.publish.custom.findDokkaTask
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar

@Suppress("UnstableApiUsage")
open class AndroidJavadocsJar : Jar() {

  init {
    archiveClassifier.set("javadoc")

    if (project.plugins.hasPlugin("org.jetbrains.dokka")) {
      val dokkaTask = project.findDokkaTask()
      dependsOn(dokkaTask)
      from(dokkaTask.outputDirectory)
    } else {
      val javadocTask = project.tasks.getByName("androidJavadocs") as Javadoc
      dependsOn(javadocTask)
      from(javadocTask.destinationDir)
    }
  }
}