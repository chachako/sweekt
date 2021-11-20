/*
 * Copyright (c) 2021. The Meowool Organization Open Source Project
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
 * See the License for the specific language governing permissions and
 * limitations under the License.

 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
@file:Suppress("SpellCheckingInspection")

import com.meowool.sweekt.firstCharTitlecase

plugins { kotlin; id(Plugins.Intellij) }

publication {
  enabled = false
  data.version = data.version
    .removeSuffix("-LOCAL")
    .removeSuffix("-SNAPSHOT")
}

dependencies{
  compileOnly(Libs.Kotlin.Compiler)
  implementationProject(Projects.Plugin.Compiler.Hosted)
}

intellij {
  pluginName.set(rootProject.name.firstCharTitlecase())
  updateSinceUntilBuild.set(false)
  version.set(findPropertyOrEnv("intellij.version").toString())
  plugins.set(listOf("com.intellij.gradle", "org.jetbrains.kotlin"))
}

tasks {
  publishPlugin {
    findPropertyOrEnv("intellij.plugin.token")?.toString()?.let(token::set)
  }
  patchPluginXml {
    sinceBuild.set(findPropertyOrEnv("intellij.since.build").toString())
    // Don't restrict the new version of Intellij-IEDA
    untilBuild.set(provider { null })
    version.set(provider { publication.data.version })
    pluginDescription.set(
      """
        Provide corresponding IDE capabilities for <a href="https://github.com/RinOrz/sweekt">Sweekt</a> runtime.
        <h4>Currently supported <i>Kotlin</i> sugar enhancements include:</h4>
        <ul>
          <li><b>@Info</b> <sup>(instead of <b>data class</b>)</sup></li>
          <li><b>@LazyInit</b> <sup>(instead of <b>by lazy {...}</b>)</sup></li>
        </ul>
      """.trimIndent()
    )
    changeNotes.set(
      """
        Initialize the intellij-idea plugin of <a href="https://github.com/RinOrz/sweekt">Sweekt</a>.
      """.trimIndent()
    )
  }
}
