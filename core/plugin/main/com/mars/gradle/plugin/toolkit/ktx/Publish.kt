@file:Suppress("PackageDirectoryMismatch", "SpellCheckingInspection", "unused", "FunctionName")

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import com.vanniktech.maven.publish.custom.MavenPublishConfigurer
import com.vanniktech.maven.publish.custom.MavenPublishPom
import com.vanniktech.maven.publish.custom.findOptionalProperty
import com.vanniktech.maven.publish.custom.publishing
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaTask

/* 打包并发布 aar/jar (含源码) 到 https://bintray.com/ */
fun Project.publishToBintray(
  group: String = findOptionalProperty("publish.group") ?: this.group.toString(),
  artifact: String = findOptionalProperty("publish.artifact") ?: this.name,
  packageName: String = artifact,
  withJavadoc: Boolean = false,
  withDokka: Boolean = false,
  saveAsLocal: Boolean = true
) {
  if (!plugins.hasPlugin(BintrayPlugin::class.java)) plugins.apply(BintrayPlugin::class.java)
  fun fetchProperty(key: String) = findOptionalProperty(key)
    ?: error("请在 gradle.properties 中定义 $key")

  publishArchive(
    group = group,
    artifact = artifact,
    version = fetchProperty("publish.version"),
    withDokka = withDokka,
    withJavadoc = withJavadoc,
    repositories = { if (saveAsLocal) maven(rootDir.resolve(".repo")) }
  )

  afterEvaluate {
    extensions.configure<BintrayExtension> {
      key = findOptionalProperty("bintray.api.key")
        ?: localPropertiesOrNull?.get("bintray.api.key") as? String
          ?: rootLocalPropertiesOrNull?.get("bintray.api.key") as? String
          ?: error("请在 gradle.properties 或 local.properties 中定义 bintray.api.key, 否则无法发布到 bintray 仓库！")
      user = fetchProperty("publish.author")
      pkg.apply {
        repo = fetchProperty("bintray.repository")
        name = packageName
        version.name = fetchProperty("publish.version")
        websiteUrl = fetchProperty("bintray.websiteUrl")
        issueTrackerUrl = fetchProperty("bintray.issueTrackerUrl")
        vcsUrl = fetchProperty("bintray.vcsUrl")
        desc = fetchProperty("bintray.description")
        setLicenses(*fetchProperty("bintray.licenses").split(" ").toTypedArray())
      }
      publish = true

      val publicationNames = mutableListOf<String>()
      extensions.findByType<PublishingExtension>()?.publications?.configureEach {
        publicationNames.add(name)
      }
      setPublications(*publicationNames.toTypedArray())
    }
  }
}

/** 打包并发布 aar/jar (含源码) 到 [repositories] */
fun Project.publishArchive(
  version: String,
  group: String,
  artifact: String = project.name,
  withJavadoc: Boolean = false,
  withDokka: Boolean = false,
  // 发布的仓库
  repositories: RepositoryHandler.() -> Unit
) {
  if (!plugins.hasPlugin(MavenPublishPlugin::class.java))
    plugins.apply(MavenPublishPlugin::class.java)

  val pom = MavenPublishPom(
    groupId = group,
    artifactId = artifact,
    version = version
  )

  // configure Javadoc
  if (withJavadoc) tasks.withType<Javadoc> {
    val options = options as StandardJavadocDocletOptions
    if (JavaVersion.current().isJava9Compatible) {
      options.addBooleanOption("html5", true)
    }
    if (JavaVersion.current().isJava8Compatible) {
      options.addStringOption("Xdoclint:none", "-quiet")
    }
  }

  // configure Dokka
  if (withDokka) {
    if (plugins.hasPlugin("org.jetbrains.kotlin.jvm")
      || plugins.hasPlugin("org.jetbrains.kotlin.android")
      && !plugins.hasPlugin("org.jetbrains.dokka")
    ) project.plugins.apply(PLUGIN_DOKKA)

    plugins.withId(PLUGIN_DOKKA) {
      tasks.withType<DokkaTask> {
        if (outputDirectory.orNull == null) {
          val javaConvention = convention.getPlugin(JavaPluginConvention::class.java)
          outputDirectory.set(
            javaConvention.docsDir
              .resolve("dokka")
              .relativeTo(projectDir)
          )
        }
      }
    }
  }

  afterEvaluate {
    publishing.repositories.apply(repositories)
    MavenPublishConfigurer(this, pom).apply {
      if (plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
        configureKotlinMppProject()
      } else if (plugins.hasPlugin("java-gradle-plugin")) {
        configureGradlePluginProject()
      } else if (plugins.hasPlugin("com.android.library")) {
        configureAndroidArtifacts()
      } else if (plugins.hasPlugin("java")
        || plugins.hasPlugin("java-library")
        || plugins.hasPlugin("kotlin")
      ) {
        configureJavaArtifacts(withJavadoc)
      } else {
        logger.warn("No compatible plugin found in project $name for publishing")
      }
    }
  }
}

@InternalMarsProjectApi
fun Project.publishMarsArchiveToLocal(
  version: String = "1.0",
  group: String = Mars.group,
  artifact: String = project.name,
  withJavadoc: Boolean = false,
  withDokka: Boolean = false
) = publishArchive(
  version = version,
  group = group,
  artifact = artifact,
  withJavadoc = withJavadoc,
  withDokka = withDokka
) {
  maven(marsLocalInternalRepoPath)
}