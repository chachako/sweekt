package com.mars.gradle.plugin.uikit

import com.mars.gradle.plugin.toolkit.booster.transform.asm.registerAsmTransforms
import org.gradle.api.Plugin
import org.gradle.api.Project

class UiKitPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.registerAsmTransforms(
      ViewTransformer()
    )
  }
}