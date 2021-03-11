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

package com.vanniktech.maven.publish.custom

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.dokka.gradle.DokkaTask
import java.util.concurrent.Callable

internal fun Project.findMandatoryProperty(propertyName: String): String {
  val value = this.findOptionalProperty(propertyName)
  return requireNotNull(value) { "Please define \"$propertyName\" in your gradle.properties file" }
}

internal fun Project.findOptionalProperty(propertyName: String) =
  findProperty(propertyName)?.toString() ?: rootProject.findProperty(propertyName)?.toString()

internal inline val Project.signing: SigningExtension
  get() = extensions.getByType(SigningExtension::class.java)

internal inline val Project.publishing: PublishingExtension
  get() = extensions.getByType(PublishingExtension::class.java)

internal inline val Project.isSigningRequired: Callable<Boolean>
  get() = Callable { !project.version.toString().contains("SNAPSHOT") }

internal fun Project.findDokkaTask(): DokkaTask {
  val tasks = project.tasks.withType(DokkaTask::class.java)
  return if (tasks.size == 1) {
    tasks.first()
  } else {
    tasks.findByName("dokkaHtml") ?: tasks.getByName("dokka")
  }
}