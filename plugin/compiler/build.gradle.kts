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
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
@file:Suppress("SpellCheckingInspection")

plugins { kotlin; id("com.github.johnrengelman.shadow") }

publication.data.artifactId = "sweekt-compiler"

dependencies {
  compileOnlyProject(Projects.Plugin.Compiler.Hosted)

  testImplementationOf(
    project(Projects.Library),
    Libs.KotlinCompileTesting,
    Libs.Kotlin.Stdlib.Common,
    Libs.Kotlin.Compiler.Embeddable,
    Libs.Kotest.Runner.Junit5.Jvm,
  )
  testRuntimeOnly(Libs.Kotlin.Compiler)
  testCompileOnlyProject(Projects.Plugin.Compiler.Hosted)
}

tasks {
  shadowJar {
    configurations = listOf(project.configurations.compileClasspath.get())
    dependencies {
      exclude(project(Projects.Library))
    }
    // Don't use intellij internal APIs, redirect them to embedded Kotlin Compiler APIs
    relocate("com.intellij", "org.jetbrains.kotlin.com.intellij")
  }
  // Replace the standard jar with the one built by 'shadowJar' in both api and runtime variants
  configurations {
    fun NamedDomainObjectProvider<Configuration>.replaceShadowJar() = get().outgoing {
      artifacts.clear()
      artifact(shadowJar.flatMap { it.archiveFile })
    }
    apiElements.replaceShadowJar()
    runtimeElements.replaceShadowJar()
  }
  // Add the jar built by 'shadowJar' to classpath
  test {
    useJUnitPlatform()
    dependsOn(shadowJar)
    classpath += shadowJar.get().outputs.files
  }
}
