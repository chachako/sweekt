@file:Suppress("PackageDirectoryMismatch", "unused")

import org.gradle.api.artifacts.dsl.DependencyHandler

// 与自带的 Gradle 不同的是，可以一次性设置所有依赖，有益于代码简洁

fun DependencyHandler.implementationOf(vararg dependencies: Any) {
  dependencies.forEach { add("implementation", it) }
}

fun DependencyHandler.debugImplementationOf(vararg dependencies: Any) {
  dependencies.forEach { add("debugImplementation", it) }
}

fun DependencyHandler.releaseImplementationOf(vararg dependencies: Any) {
  dependencies.forEach { add("releaseImplementation", it) }
}

fun DependencyHandler.androidTestImplementationOf(vararg dependencies: Any) {
  dependencies.forEach { add("androidTestImplementation", it) }
}

fun DependencyHandler.testImplementationOf(vararg dependencies: Any) {
  dependencies.forEach { add("testImplementation", it) }
}


// 更便于导入多个 project 目录，免去键入 project(":xxx")

fun DependencyHandler.implementProjects(vararg paths: Any) {
  paths.forEach { implementationOf(project(mapOf("path" to it))) }
}

fun DependencyHandler.testImplementProjects(vararg paths: Any) {
  paths.forEach { testImplementationOf(project(mapOf("path" to it))) }
}


// 一次性导入多个 Meowbase 的 kapt 依赖

fun DependencyHandler.implementationMeowbaseLibraries(vararg artifacts: String) {
  artifacts.forEach { add("implementation", meowbaseLibrary(it)) }
}

fun DependencyHandler.debugImplementationMeowbaseLibraries(vararg artifacts: String) {
  artifacts.forEach { add("debugImplementation", meowbaseLibrary(it)) }
}

fun DependencyHandler.releaseImplementationMeowbaseLibraries(vararg artifacts: String) {
  artifacts.forEach { add("releaseImplementation", meowbaseLibrary(it)) }
}

fun DependencyHandler.androidTestImplementationMeowbaseLibraries(vararg artifacts: String) {
  artifacts.forEach { add("androidTestImplementation", meowbaseLibrary(it)) }
}

fun DependencyHandler.testImplementationMeowbaseLibraries(vararg artifacts: String) {
  artifacts.forEach { add("testImplementation", meowbaseLibrary(it)) }
}