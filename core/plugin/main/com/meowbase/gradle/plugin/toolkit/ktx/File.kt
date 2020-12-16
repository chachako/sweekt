package com.meowbase.gradle.plugin.toolkit.ktx

import com.meowbase.toolkit.subtree
import java.io.File

val Set<File>.allFiles
  get() = mutableListOf<File>().also { list ->
    forEach { list.addAll(it.subtree()) }
  }

/** 遍历所有文件 */
inline fun Set<File>.forEachRecursive(
  filter: File.() -> Boolean = { true },
  action: (File) -> Unit
) {
  val files = mutableListOf<File>()
  forEach { files.addAll(it.subtree()) }
  files.forEach {
    if (it.filter() && filter.invoke(it)) action(it)
  }
}