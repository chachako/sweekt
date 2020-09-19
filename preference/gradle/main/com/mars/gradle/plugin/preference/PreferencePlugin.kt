package com.mars.gradle.plugin.preference

import com.mars.gradle.plugin.preference.data.VagueOptions
import com.mars.gradle.plugin.preference.data.tempDir
import com.mars.gradle.plugin.preference.hooks.VaguePreferences
import com.mars.gradle.plugin.toolkit.hooker.registerHooks
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class PreferencePlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.extensions.add("vaguePreference", VagueOptions())
    target.gradle.buildFinished {
      // 恢复所有源文件
      sourcesFileBackup.apply {
        forEach {
          File(it.absolutePath.replace(target.tempDir.absolutePath, "")).also { original ->
            original.delete()
            it.renameTo(original)
          }
        }
        if (isNotEmpty()) clear()
      }
    }
    target.registerHooks(VaguePreferences::class)
  }

  companion object {
    // 备份源码，在编译完成后需要恢复这些源码
    val sourcesFileBackup = mutableListOf<File>()
  }
}