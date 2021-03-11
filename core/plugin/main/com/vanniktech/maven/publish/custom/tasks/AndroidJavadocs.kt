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

import com.android.build.gradle.LibraryExtension
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import java.io.File

open class AndroidJavadocs : Javadoc() {

  init {
    val androidExtension = project.extensions.getByType(LibraryExtension::class.java)

    // Append also the classpath and files for release library variants. This fixes the javadoc warnings.
    // Got it from here - https://github.com/novoda/bintray-release/pull/39/files
    val releaseVariantCompileProvider = androidExtension.libraryVariants.toList()
      .last().javaCompileProvider
    dependsOn(androidExtension.libraryVariants.toList().last().javaCompileProvider)
    if (!project.plugins.hasPlugin("org.jetbrains.kotlin.android")) {
      setSource(androidExtension.sourceSets.getByName("main").java.srcDirs)
    }

    isFailOnError = true
    classpath += project.files(androidExtension.bootClasspath.joinToString(File.pathSeparator))
    // Safe to call get() here because we'ved marked this as dependent on the TaskProvider
    classpath += releaseVariantCompileProvider.get().classpath
    classpath += releaseVariantCompileProvider.get().outputs.files

    // We don't need javadoc for internals.
    exclude("**/internal/*")

    // Append Java 8 and Android references
    val options = options as StandardJavadocDocletOptions
    options.links("https://developer.android.com/reference")
    options.links("https://docs.oracle.com/javase/8/docs/api/")
  }
}
