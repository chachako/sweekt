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

@file:Suppress("PackageDirectoryMismatch", "unused")

import org.gradle.api.artifacts.dsl.DependencyHandler

// 与自带的 Gradle 不同的是，可以一次性设置所有依赖，有益于代码简洁

fun DependencyHandler.compileOnlyApiOf(vararg dependencies: Any) {
  dependencies.forEach { add("compileOnlyApi", it) }
}


// 更便于导入多个 project 目录，免去键入 project(":xxx")

fun DependencyHandler.compileOnlyApiProjects(vararg paths: Any) {
  paths.forEach { compileOnlyApiOf(project(mapOf("path" to it))) }
}


// 一次性导入多个 Meowbase 的 kapt 依赖

fun DependencyHandler.compileOnlyApiMeowbaseLibraries(vararg artifacts: String) {
  artifacts.forEach { add("compileOnlyApi", meowbaseLibrary(it)) }
}