package com.vanniktech.maven.publish.custom

import com.vanniktech.maven.publish.custom.tasks.*
import org.gradle.api.Project
import org.gradle.api.publish.Publication
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.AbstractArchiveTask
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

  override fun configureGradlePluginProject() {
    val sourcesJar = project.tasks.register(SOURCES_TASK, SourcesJar::class.java)
    val javadocsJar = project.tasks.register(JAVADOC_TASK, JavadocsJar::class.java)

    project.publishing.publications.withType(MavenPublication::class.java).all {
      if (name == "pluginMaven") {
        configurePom(this)
        addTaskOutput(javadocsJar)
        addTaskOutput(sourcesJar)
      }

      project.extensions.getByType(GradlePluginDevelopmentExtension::class.java).plugins.forEach { plugin ->
        if (name == "${plugin.name}PluginMarkerMaven") {
          // keep the current group and artifact ids, they are based on the gradle plugin id
          configurePom(this, groupId = groupId, artifactId = artifactId)
        }
      }
    }
  }

  override fun configureTarget(target: MavenPublishTarget) {
    project.publishing.repositories.maven {
      name = target.taskName
      url = target.repositoryUrl()
    }

    // create task that depends on new publishing task for compatibility and easier switching
    project.tasks.register(target.taskName) {
      project.publishing.publications.all {
        val publishTaskName = publishTaskName(this, target.taskName)
        this@register.dependsOn(project.tasks.named(publishTaskName))
      }
    }
  }

  override fun configureKotlinMppProject() {
    val javadocsJar = project.tasks.register(JAVADOC_TASK, JavadocsJar::class.java)

    project.publishing.publications.withType(MavenPublication::class.java).all {
      configurePom(this, artifactId = artifactId.replace(project.name, publishPom.artifactId))
      addTaskOutput(javadocsJar)

      // Source jars are only created for platforms, not the common artifact.
      if (name == "kotlinMultiplatform") {
        val emptySourcesJar = project.tasks.register("emptySourcesJar", EmptySourcesJar::class.java)
        addTaskOutput(emptySourcesJar)
      }
    }
  }

  override fun configureAndroidArtifacts() {
    val publications = project.publishing.publications
    publications.create(PUBLICATION_NAME, MavenPublication::class.java) { configurePom(this) }

    val publication = project.publishing.publications.getByName(PUBLICATION_NAME) as MavenPublication

    publication.from(project.components.getByName("release"))

    val androidSourcesJar = project.tasks.register(
      "androidSourcesJar",
      AndroidSourcesJar::class.java
    )
    publication.addTaskOutput(androidSourcesJar)

    project.tasks.register("androidJavadocs", AndroidJavadocs::class.java)
    val androidJavadocsJar = project.tasks.register(
      "androidJavadocsJar",
      AndroidJavadocsJar::class.java
    )
    publication.addTaskOutput(androidJavadocsJar)
  }

  override fun configureJavaArtifacts(withDoc: Boolean) {
    val publications = project.publishing.publications
    publications.create(PUBLICATION_NAME, MavenPublication::class.java) { configurePom(this) }

    val publication = project.publishing.publications.getByName(PUBLICATION_NAME) as MavenPublication

    publication.from(project.components.getByName("java"))

    val sourcesJar = project.tasks.register(SOURCES_TASK, SourcesJar::class.java)
    publication.addTaskOutput(sourcesJar)

    if (withDoc) {
      val javadocsJar = project.tasks.register(JAVADOC_TASK, JavadocsJar::class.java)
      publication.addTaskOutput(javadocsJar)

      if (project.plugins.hasPlugin("groovy")) {
        val goovydocsJar = project.tasks.register("groovydocJar", GroovydocsJar::class.java)
        publication.addTaskOutput(goovydocsJar)
      }
    }

  }

  private fun MavenPublication.addTaskOutput(taskProvider: TaskProvider<out AbstractArchiveTask>) {
    artifact(taskProvider.get())
  }

  private fun MavenPublishTarget.repositoryUrl(): URI {
    return URI.create(requireNotNull(releaseRepositoryUrl))
  }

  private fun publishTaskName(publication: Publication, repository: String) =
    "publish${publication.name.capitalize()}PublicationTo${repository.capitalize()}Repository"

  companion object {
    const val PUBLICATION_NAME = "maven"
    const val JAVADOC_TASK = "javadocsJar"
    const val SOURCES_TASK = "sourcesJar"
  }
}
