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

plugins {
  kotlin
  id("com.github.johnrengelman.shadow")
}

publication.data.artifactId = "sweekt-compiler"

dependencies.compileOnlyProject(Projects.Plugin.Compiler.Hosted)

tasks {
  shadowJar {
    configurations = listOf(project.configurations.compileClasspath.get())
    dependencies {
      exclude(Libs.Kotlin.Stdlib.Jdk7)
      exclude(Libs.Kotlin.Stdlib.Jdk8)
      exclude(project(Projects.Library))
    }
    // Don't use the embedded Kotlin Compiler APIs, redirect them to use the Intellij internal
    relocate("com.intellij", "org.jetbrains.kotlin.com.intellij")
  }
  // Replace the standard jar with the one built by 'shadowJar' in both api and runtime variants
  configurations {
    apiElements.get().outgoing {
      artifacts.clear()
      artifact(shadowJar.flatMap { it.archiveFile })
    }
    runtimeElements.get().outgoing {
      artifacts.clear()
      artifact(shadowJar.flatMap { it.archiveFile })
    }
  }
}