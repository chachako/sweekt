@file:Suppress("PackageDirectoryMismatch", "unused")

import org.gradle.api.artifacts.dsl.DependencyHandler

// 与自带的 Gradle 不同的是，可以一次性设置所有依赖，有益于代码简洁

fun DependencyHandler.kaptOf(vararg dependencies: Any) {
  dependencies.forEach { add("kapt", it) }
}

fun DependencyHandler.kaptTestOf(vararg dependencies: Any) {
  dependencies.forEach { add("kaptTest", it) }
}

fun DependencyHandler.kaptAndroidTestOf(vararg dependencies: Any) {
  dependencies.forEach { add("kaptAndroidTest", it) }
}


// 更便于导入多个 project 目录，免去键入 project(":xxx")

fun DependencyHandler.kaptProjects(vararg paths: Any) {
  paths.forEach { kaptOf(project(mapOf("path" to it))) }
}

fun DependencyHandler.kaptTestProjects(vararg paths: Any) {
  paths.forEach { kaptTestOf(project(mapOf("path" to it))) }
}

fun DependencyHandler.kaptAndroidTestProjects(vararg paths: Any) {
  paths.forEach { kaptAndroidTestOf(project(mapOf("path" to it))) }
}