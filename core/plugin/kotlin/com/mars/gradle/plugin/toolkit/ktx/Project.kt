package com.mars.gradle.plugin.toolkit.ktx

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

inline fun <reified T : BaseExtension> Project.getAndroid(): T = extensions.getByName("android") as T

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
