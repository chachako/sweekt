package com.meowbase.gradle.plugin.uikit

import com.meowbase.gradle.plugin.toolkit.booster.transform.asm.registerAsmTransforms
import org.gradle.api.Plugin
import org.gradle.api.Project

class UikitPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.registerAsmTransforms(
      ViewTransformer()
    )
  }
}