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