@file:Suppress("SpellCheckingInspection")

package com.mars.gradle.plugin.toolkit.ktx

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register
import kotlin.reflect.KClass

/**
 * 获取 [BaseExtension]
 * WRAN: 仅适用于 Android 项目
 */
val Project.baseExtensionOrNull: BaseExtension? get() = extensions.findByType()
val Project.baseExtension: BaseExtension get() = baseExtensionOrNull!!

/**
 * 获取 [AppExtension]
 * WRAN: 仅适用于 Android 项目
 */
val Project.appExtensionOrNull: AppExtension? get() = extensions.findByType()
val Project.appExtension: AppExtension get() = appExtensionOrNull!!

/**
 * 获取 [LibraryExtension]
 * WRAN: 仅适用于 Android 项目
 */
val Project.libraryExtensionOrNull: LibraryExtension? get() = extensions.findByType()
val Project.libraryExtension: LibraryExtension get() = libraryExtensionOrNull!!


inline fun <reified T : BaseExtension> Project.getAndroid(): T =
  extensions.getByName("android") as T

/** 从 gradle.properties 中获取属性 */
inline fun <reified T> Project.getProperty(name: String, defaultValue: T? = null): T {
  val value = findProperty(name) ?: defaultValue ?: error("在当前项目中找不到名为 $name 的属性！")
  return when (defaultValue) {
    is Boolean -> if (value is Boolean) value as T else value.toString().toBoolean() as T
    is Byte -> if (value is Byte) value as T else value.toString().toByte() as T
    is Short -> if (value is Short) value as T else value.toString().toShort() as T
    is Int -> if (value is Int) value as T else value.toString().toInt() as T
    is Float -> if (value is Float) value as T else value.toString().toFloat() as T
    is Long -> if (value is Long) value as T else value.toString().toLong() as T
    is Double -> if (value is Double) value as T else value.toString().toDouble() as T
    is String -> if (value is String) value as T else value.toString() as T
    else -> value as T
  }
}

/** 从 gradle.properties 或系统环境变量中获取属性 */
inline fun <reified T> Project.propertyOrEnv(key: String): T? =
  getProperty(key) ?: System.getenv(key) as? T

/** 跳过给定的任务，让项目编译时不会执行它们 */
fun Project.skipTasks(vararg taskNames: String) = taskNames.forEach {
  tasks.findByName(it)?.enabled = false
}

/**
 * 跳过给定的 Android 任务，让项目编译时不会执行它们
 * NOTE: 可以使用符号 * 来解析任务
 * @see findAndroidTask
 */
fun Project.skipAndroidTasks(vararg taskNames: String) = taskNames.forEach {
  findAndroidTask(it)?.enabled = false
}

/** 根据名称查找项目上的对应任务 */
fun Project.findTask(name: String): Task? = tasks.findByName(name)

/**
 * 查找 Android 项目上实际变体的对应任务
 * 根据符号 * 来解析不同变体时的实际任务名称
 * ```
 * // 大写
 * findAndroidTask("assemble**") -> task: assembleDebug / assembleRelease
 *
 * // 小写
 * findAndroidTask("assemble*") -> task: assembledebug / assemblerelease
 * ```
 */
fun Project.findAndroidTask(name: String): Task? =
  tasks.findByName(name.replace("**", "Release").replace("*", "release"))
    ?: tasks.findByName(name.replace("**", "Debug").replace("*", "debug"))

/** 注册多个插件 */
fun Project.registerTasks(vararg tasksWithNames: Pair<String, KClass<out Task>>) = tasksWithNames.forEach {
  this.tasks.register(it.first, it.second)
}

/** 注册多个插件，以 [Task] 类名作为任务名称 */
fun Project.registerTasks(vararg tasks: KClass<out Task>) = tasks.forEach {
  this.tasks.register(it.java.simpleName, it)
}