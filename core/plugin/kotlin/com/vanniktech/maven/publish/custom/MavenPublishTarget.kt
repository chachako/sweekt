package com.vanniktech.maven.publish.custom

data class MavenPublishTarget(
  /**
   * The release repository url this should be published to.
   * @since 0.7.0
   */
  var releaseRepositoryUrl: String? = null
) {

  val taskName: String = "local"
}
