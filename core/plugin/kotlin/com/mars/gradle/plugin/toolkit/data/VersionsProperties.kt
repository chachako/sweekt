@file:Suppress("PackageDirectoryMismatch")

import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties
import java.io.File

/* 记录一些与 https://github.com/jmfayard/refreshVersions 有关的数据 */
internal object VersionsProperties {
  lateinit var file: File
  lateinit var last: List<String>
  val current: List<String> get() = file.readLines()
  val properties: Properties get() = loadProperties(file.absolutePath)
}