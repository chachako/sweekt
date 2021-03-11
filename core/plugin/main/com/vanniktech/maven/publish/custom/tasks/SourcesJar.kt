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

package com.vanniktech.maven.publish.custom.tasks

import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.jvm.tasks.Jar

@Suppress("UnstableApiUsage")
open class SourcesJar : Jar() {

  init {
    archiveClassifier.set("sources")

    val javaPlugin = project.convention.getPlugin(JavaPluginConvention::class.java)
    from(javaPlugin.sourceSets.getByName("main").allSource)
  }
}
