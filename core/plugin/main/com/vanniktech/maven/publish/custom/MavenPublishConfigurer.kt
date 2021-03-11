/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package com.vanniktech.maven.publish.custom

import com.vanniktech.maven.publish.custom.tasks.AndroidJavadocs
import com.vanniktech.maven.publish.custom.tasks.AndroidJavadocsJar
import com.vanniktech.maven.publish.custom.tasks.AndroidSourcesJar
import com.vanniktech.maven.publish.custom.tasks.EmptySourcesJar
import com.vanniktech.maven.publish.custom.tasks.GroovydocsJar
import com.vanniktech.maven.publish.custom.tasks.JavadocsJar
import com.vanniktech.maven.publish.custom.tasks.SourcesJar
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import java.net.URI

@Suppress("TooManyFunctions")
internal class MavenPublishConfigurer(
  private val project: Project,
  private val publishPom: MavenPublishPom
) : Configurer {

  private fun configurePom(
    publication: MavenPublication,
    groupId: String = publishPom.groupId, // The plugin initially sets project.group to publishPom.groupId
    artifactId: String = publishPom.artifactId
  ) {
    publication.groupId = groupId
    publication.artifactId = artifactId
    publication.version = publishPom.version // The plugin initially sets project.version to publishPom.version
  }

  override fun configureTarget(target: MavenPublishTarget) {
    project.publishing.repositories.maven {
      name = target.taskName
      url = target.repositoryUrl()
    }

    // create task that depends on new publishing task for compatibility and easier switching
    project.tasks.register(target.taskName) {
      project.publishing.publications.all {
        val publishTaskName = "publish${name.capitalize()}Publication" +
          "To${target.taskName.capitalize()}Repository"
        this@register.dependsOn(project.tasks.named(publishTaskName))
      }
    }
  }

  private fun MavenPublishTarget.repositoryUrl(): URI {
    return URI.create(requireNotNull(releaseRepositoryUrl))
  }

  override fun configureGradlePluginProject(withDoc: Boolean) {
    val sourcesJar = project.tasks.register(SOURCES_TASK, SourcesJar::class.java)

    project.publishing.publications.withType(MavenPublication::class.java).all {
      if (name == "pluginMaven") {
        configurePom(this)
        artifact(sourcesJar)
        if (withDoc) artifact(project.tasks.register(JAVADOC_TASK, JavadocsJar::class.java))
      }

      project.extensions.getByType(GradlePluginDevelopmentExtension::class.java).plugins.forEach { plugin ->
        if (name == "${plugin.name}PluginMarkerMaven") {
          // keep the current group and artifact ids, they are based on the gradle plugin id
          configurePom(this, groupId = groupId, artifactId = artifactId)
        }
      }
    }
  }

  override fun configureKotlinMppProject(withDoc: Boolean) {

    project.publishing.publications.withType(MavenPublication::class.java).all {
      configurePom(this, artifactId = artifactId.replace(project.name, publishPom.artifactId))
      if (withDoc) artifact(project.tasks.register(JAVADOC_TASK, JavadocsJar::class.java))

      // Source jars are only created for platforms, not the common artifact.
      if (name == "kotlinMultiplatform") {
        val emptySourcesJar = project.tasks.register("emptySourcesJar", EmptySourcesJar::class.java)
        artifact(emptySourcesJar)
      }
    }
  }

  override fun configureAndroidArtifacts(withDoc: Boolean) {
    val publications = project.publishing.publications
    publications.create(PUBLICATION_NAME, MavenPublication::class.java) { configurePom(this) }

    val publication = project.publishing.publications.getByName(PUBLICATION_NAME) as MavenPublication

    publication.from(project.components.getByName("release"))

    val androidSourcesJar = project.tasks.register("androidSourcesJar", AndroidSourcesJar::class.java)
    publication.artifact(androidSourcesJar)

    if (withDoc) {
      project.tasks.register("androidJavadocs", AndroidJavadocs::class.java)
      val androidJavadocsJar = project.tasks.register("androidJavadocsJar", AndroidJavadocsJar::class.java)
      publication.artifact(androidJavadocsJar)
    }
  }

  override fun configureJavaArtifacts(withDoc: Boolean) {
    val publications = project.publishing.publications
    publications.create(PUBLICATION_NAME, MavenPublication::class.java) { configurePom(this) }

    val publication = project.publishing.publications.getByName(PUBLICATION_NAME) as MavenPublication

    publication.from(project.components.getByName("java"))

    val sourcesJar = project.tasks.register(SOURCES_TASK, SourcesJar::class.java)
    publication.artifact(sourcesJar)

    if (withDoc) {
      val javadocsJar = project.tasks.register(JAVADOC_TASK, JavadocsJar::class.java)
      publication.artifact(javadocsJar)
    }

    if (project.plugins.hasPlugin("groovy")) {
      val goovydocsJar = project.tasks.register("groovydocJar", GroovydocsJar::class.java)
      publication.artifact(goovydocsJar)
    }
  }

  companion object {
    const val PUBLICATION_NAME = "maven"
    const val JAVADOC_TASK = "javadocsJar"
    const val SOURCES_TASK = "sourcesJar"
  }
}