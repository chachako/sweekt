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

plugins {
  kotlin
  id(Plugins.Intellij)
  id("com.github.johnrengelman.shadow")
}

intellij {
  pluginName.set(rootProject.name.firstCharTitlecase())
  updateSinceUntilBuild.set(false)
  plugins.add("Kotlin")
  version.set(findPropertyOrEnv("intellij.version").toString())
}

dependencies.implementationProject(Projects.Plugin.Compiler)

tasks {
  publishPlugin {
    findPropertyOrEnv("intellij.plugin.token")?.toString()?.let(token::set)
  }
  shadowJar {
    minimize {
      // Don't minimize compiler
      exclude(project(Projects.Plugin.Compiler))
    }
    dependencies {
      exclude(dependency(Libs.Kotlin.Stdlib.Jdk8))
    }
    // Don't use the embedded Kotlin Compiler APIs, redirect them to use the Intellij internal
    relocate("org.jetbrains.kotlin.com.intellij", "com.intellij")
  }
  // Replace the output jar with output of 'shadowJar' task
  jar {
    archiveBaseName.set("${rootProject.name}-$name")
    dependsOn(shadowJar)
    doLast {
      val originJar = archiveFile.get().asFile
      val shadowJar = shadowJar.get().archiveFile.get().asFile
      shadowJar.copyTo(originJar, overwrite = true)
      shadowJar.delete()
    }
  }
  patchPluginXml {
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
    version.set(provider { publication.data.versionInCI })
    sinceBuild.set(findPropertyOrEnv("intellij.since.build").toString())
    // Don't restrict the new version of Intellij-IEDA
    untilBuild.set(provider { null })
  }
}
