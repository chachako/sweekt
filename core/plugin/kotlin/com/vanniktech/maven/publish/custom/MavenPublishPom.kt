package com.vanniktech.maven.publish.custom

internal data class MavenPublishPom(
  val groupId: String,
  val artifactId: String,
  val version: String
)