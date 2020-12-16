package com.vanniktech.maven.publish.custom

internal interface Configurer {
  /**
   * Needs to be called for all targets before `addComponent` and
   * `addTaskOutput`.
   */
  fun configureTarget(target: MavenPublishTarget)

  fun configureKotlinMppProject(withDoc: Boolean)

  fun configureGradlePluginProject(withDoc: Boolean)

  fun configureAndroidArtifacts(withDoc: Boolean)

  fun configureJavaArtifacts(withDoc: Boolean)
}
