/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package com.meowbase.plugin.preference

import com.meowbase.plugin.preference.data.VagueOptions
import com.meowbase.plugin.preference.data.tempDir
import com.meowbase.plugin.preference.hooks.VaguePreferences
import com.meowbase.plugin.toolkit.hooker.registerHooks
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