package com.meowbase.gradle.plugin.toolkit

import ToolkitOptions
import org.gradle.api.Plugin
import org.gradle.api.Project

class ToolkitPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.rootProject.extensions.add("toolkit", ToolkitOptions())
  }
}