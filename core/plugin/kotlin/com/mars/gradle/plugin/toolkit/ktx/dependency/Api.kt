@file:Suppress("PackageDirectoryMismatch", "unused")

import org.gradle.api.artifacts.dsl.DependencyHandler

// 与自带的 Gradle 不同的是，可以一次性设置所有依赖，有益于代码简洁

fun DependencyHandler.apiOf(vararg dependencies: Any) {
  dependencies.forEach { add("api", it) }
}

fun DependencyHandler.testApiOf(vararg dependencies: Any) {
  dependencies.forEach { add("testApi", it) }
}


// 更便于导入多个 project 目录，免去键入 project(":xxx")

fun DependencyHandler.apiProjects(vararg paths: Any) {
  paths.forEach { apiOf(project(mapOf("path" to it))) }
}
