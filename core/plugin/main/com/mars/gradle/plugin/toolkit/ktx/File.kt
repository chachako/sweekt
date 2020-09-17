package com.mars.gradle.plugin.toolkit.ktx

import com.android.utils.FileUtils
import java.io.File

val Set<File>.allFiles
  get() = mutableListOf<File>().also { list ->
    forEach { list.addAll(FileUtils.getAllFiles(it)) }
  }

/** 遍历所有文件 */
inline fun Set<File>.forEachRecursive(
  filter: File.() -> Boolean = { true },
  action: (File) -> Unit
) {
  val files = mutableListOf<File>()
  forEach { files.addAll(FileUtils.getAllFiles(it)) }
  files.forEach {
    if (it.filter() && filter.invoke(it)) action(it)
  }
}