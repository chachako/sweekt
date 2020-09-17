package com.vanniktech.maven.publish.custom

internal interface Configurer {
  /**
   * Needs to be called for all targets before `addComponent` and
   * `addTaskOutput`.
   */
  fun configureTarget(target: MavenPublishTarget)

  fun configureKotlinMppProject()

  fun configureGradlePluginProject()

  fun configureAndroidArtifacts()

  fun configureJavaArtifacts(withDoc: Boolean)
}
