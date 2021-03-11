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

@file:Suppress("UnstableApiUsage", "DEPRECATION")

package com.meowbase.plugin.toolkit.ktx

import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Project
import java.io.File

/* Copy from: https://github.com/didi/booster */

/**
 * Represents the booster transform for
 *
 * @author johnsonlee
 */
val TransformInvocation.project: Project
  get() = context.task.project

/**
 * Returns the corresponding variant of this transform invocation
 *
 * @author johnsonlee
 */
val TransformInvocation.variant: BaseVariant
  get() = project.getAndroid<BaseExtension>().let { android ->
    this.context.variantName.let { variant ->
      when (android) {
        is AppExtension -> when {
          variant.endsWith("AndroidTest") -> android.testVariants.single { it.name == variant }
          variant.endsWith("UnitTest") -> android.unitTestVariants.single { it.name == variant }
          else -> android.applicationVariants.single { it.name == variant }
        }
        is LibraryExtension -> android.libraryVariants.single { it.name == variant }
        else -> TODO("variant not found")
      }
    }
  }

val TransformInvocation.bootClasspath: Collection<File>
  get() = project.getAndroid<BaseExtension>().bootClasspath

val TransformInvocation.isDataBindingEnabled: Boolean
  get() = project.getAndroid<BaseExtension>().run {
    buildFeatures.viewBinding == true
  }

/**
 * Returns the compile classpath of this transform invocation
 *
 * @author johnsonlee
 */
val TransformInvocation.compileClasspath: Collection<File>
  get() = listOf(inputs, referencedInputs).flatten().map {
    it.jarInputs + it.directoryInputs
  }.flatten().map {
    it.file
  }

/**
 * Returns the runtime classpath of this transform invocation
 *
 * @author johnsonlee
 */
val TransformInvocation.runtimeClasspath: Collection<File>
  get() = compileClasspath + bootClasspath

/**
 * Returns the application id
 */
val TransformInvocation.applicationId: String
  get() = variant.applicationId
