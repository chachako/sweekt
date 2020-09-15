package com.vanniktech.maven.publish.custom.tasks

import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.jvm.tasks.Jar

@Suppress("UnstableApiUsage")
open class SourcesJar : Jar() {

  init {
    archiveClassifier.set("sources")

    val javaPlugin = project.convention.getPlugin(JavaPluginConvention::class.java)
    from(javaPlugin.sourceSets.getByName("main").allSource)
  }
}
