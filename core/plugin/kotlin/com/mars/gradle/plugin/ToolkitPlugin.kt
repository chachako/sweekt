package com.mars.gradle.plugin

import ToolkitOptions
import org.gradle.api.Plugin
import org.gradle.api.Project

class ToolkitPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.rootProject.extensions.add("toolkit", ToolkitOptions())
  }
}