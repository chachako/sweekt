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

@file:Suppress("PackageDirectoryMismatch")

import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.io.File

/* 记录一些与 https://github.com/jmfayard/refreshVersions 有关的数据 */
object VersionsProperties {
  lateinit var file: File
  lateinit var last: List<String>
  val current: List<String> get() = file.readLines()
  val properties: Properties get() = loadProperties(file.absolutePath)

  fun resolveDependency(group: String, artifact: String) =
    "$group:$artifact:" + properties["version.$group..$artifact"]
}