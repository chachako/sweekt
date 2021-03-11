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

@file:Suppress("unused", "SpellCheckingInspection", "UnstableApiUsage", "PackageDirectoryMismatch")

import com.android.build.api.dsl.*
import org.gradle.api.Project
import java.io.File
import java.util.*

/** 混合的 extension，适用于 app/aar */
internal typealias MixinExtension = CommonExtension<AndroidSourceSet,
  BuildFeatures,
  BuildType,
  DefaultConfig,
  ProductFlavor,
  ApkSigningConfig,
  *, // FIXME 等待 android gradle plugin 4.2 stable 发布
  *>

/** 读取此项目的 local.properties */
val Project.localPropertiesOrNull: Properties?
  get() = File(projectDir, "local.properties").propertiesOrNull
val Project.localProperties: Properties
  get() = localPropertiesOrNull!!

/** 读取根项目的 local.properties */
val Project.rootLocalPropertiesOrNull: Properties?
  get() = rootDir.resolve("local.properties").propertiesOrNull
val Project.rootLocalProperties: Properties
  get() = rootLocalPropertiesOrNull!!

/**
 * 从 local.properties 中读取 key 信息
 * NOTE: 如果是内部开发会先获取环境变量中的 key.properties
 */
@InternalMeowbaseApi
internal val Project.keyProperties
  get() = File(meowbaseInternalLocalPath, ".key/key.properties").propertiesOrNull
    ?: localPropertiesOrNull
    ?: rootLocalProperties


private val File.propertiesOrNull: Properties?
  get() = if (exists()) Properties().apply {
    load(this@propertiesOrNull.bufferedReader())
  } else null