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

package com.meowbase.toolkit

import java.io.File
import java.io.IOException

/** 判断此文件夹是否可以读取子文件 */
inline val File.canReadSubFiles: Boolean get() = canRead() and isDirectory

/** 返回整个目录树的总大小 */
inline val File.totalSize: Long get() = tree().sumOf { it.length() }

/** 返回文件 & 文件夹（包含所有子文件）的总大小的格式化字符串 */
inline val File.formatSize: String get() = totalSize.formatSize()

/** 第一个文件存在则返回第一个文件，否则直接返回第二个文件 */
infix fun File.existOr(existFile: File): File = if (exists()) this else existFile

/** 第一个文件存在则返回第一个文件，否则直接 null */
fun File.existOrNull(): File? = if (exists()) this else null

/**
 * 从文件夹中获取子文件夹，文件夹不存在则创建
 * @param directory 需要获取或创建的目录名称
 * @param force 即使目录或者其父目录都不存在，都会强制创建一个新目录
 * @throws IOException
 */
fun File.existOrMkdir(directory: String, force: Boolean = false): File =
  File(this, directory).apply {
    if (exists()) return@apply
    if (parentFile?.exists() == false) {
      if (force) {
        parentFile?.mkdirs()
      } else {
        noSuchFile("无法创建目录，因为父目录不存在，请手动创建父目录或将 force 参数设置为 true.", parentFile)
      }
    }
    mkdir()
  }

/**
 * 返回并确保当前文件夹是否存在，不存在则创建
 * @param force 即使目录或者其父目录都不存在，都会强制创建一个新目录
 * @throws IOException
 */
fun File.existOrMkdir(force: Boolean = false): File = apply {
  if (exists()) return@apply
  if (parentFile?.exists() == false) {
    if (force) {
      parentFile?.mkdirs()
    } else {
      noSuchFile("无法创建目录，因为父目录不存在，请手动创建父目录或将 force 参数设置为 true.", parentFile)
    }
  }
  mkdir()
}

/**
 * 获取当前文件夹，文件夹不存在则创建（父目录不存在也会自动创建）
 * @see existOrMkdir
 */
fun File.existOrMkdirs(): File = existOrMkdir(true)

/**
 * 从文件夹中获取子文件夹，文件夹不存在则创建（父目录不存在也会自动创建）
 * @see existOrMkdir
 */
fun File.existOrMkdirs(directory: String): File = existOrMkdir(directory, true)

/**
 * 从文件夹中获取子文件，文件不存在则创建一个新文件
 * @param file 需要获取或创建的文件名称
 * @param force 即使文件或者其父目录都不存在，都要强制创建文件
 * @throws IOException
 */
fun File.existOrNewFile(file: String, force: Boolean = false): File = File(this, file).apply {
  if (exists()) return@apply
  if (parentFile?.exists() == false) {
    if (force) {
      parentFile.mkdirs()
    } else {
      noSuchFile("无法创建文件，因为父目录不存在，请手动创建父目录或将 force 参数设置为 true.", parentFile)
    }
  }
  createNewFile()
}

/**
 * 获取当前文件，如果不存在则创建一个新文件
 * @param force 即使文件或者其父目录都不存在，都要强制创建文件
 * @throws IOException
 */
fun File.existOrNewFile(force: Boolean = false): File = apply {
  if (exists()) return@apply
  if (parentFile?.exists() == false) {
    if (force) {
      parentFile.mkdirs()
    } else {
      noSuchFile("无法创建文件，因为父目录不存在，请手动创建父目录或将 force 参数设置为 true.", parentFile)
    }
  }
  createNewFile()
}

/**
 * 从文件夹中获取子文件
 * WARN: 需要注意的是，返回的 [File] 可能是不存在的，需要你手动 [File.mkdir] 或者 [File.createNewFile]
 * @see existOrMkdir
 */
operator fun File.get(file: String): File = File(this, file).apply {
  if (parentFile?.exists() == false) noSuchFile("无法获取文件，因为父目录不存在", parentFile)
}

/**
 * 将文件改名
 * @param newName 新的文件名称
 * @param force 当目标名称的文件已经存在时，覆盖它
 */
fun File.rename(newName: String, force: Boolean = false): File {
  val dest = parentFile.resolve(newName)
  if (dest.exists()) {
    if (force)
      dest.delete()
    else
      dest.alreadyExists("你不能够将 $absolutePath 重命名为 $newName, 因为目标文件已经存在，请开启 force 或先删除目标文件。")
  }
  renameTo(dest)
  return dest
}

/**
 * 获取当前目录树
 *
 * @param depth 获取的目录层级深度:
 * 如果为 1 时，只获取原始目录及其所有的直接子目录/文件
 * 如果为 2 时，则获取两层目录与它们的文件
 * 以此类推
 * ...
 * 如果为 [Int.MAX_VALUE] 时，则代表无限深度，用于获取所有目录以及其子文件
 * @return 返回一个包含自身的目录树
 */
fun File.tree(depth: Int = Int.MAX_VALUE): Sequence<File> = walk().maxDepth(depth)

/**
 * 获取当前目录下的子目录树
 *
 * @param depth 获取的目录层级深度:
 * 如果为 1 时，只获取当前目录下的所有直接子目录 & 文件
 * 如果为 2 时，则获取两层目录与它们的文件
 * 以此类推
 * ...
 * 如果为 [Int.MAX_VALUE] 时，则代表无限深度，用于获取所有目录以及其子文件
 * @return 返回一个不包含自身的目录树，如果需要，请使用 [tree]
 */
fun File.subtree(depth: Int = Int.MAX_VALUE): Sequence<File> = tree(depth).filter { this != it }

/**
 * 获取当前目录下的所有直接子目录 & 文件
 * @return 返回一个不包含自身的文件列表，它不会遍历子目录内的文件，请看 [tree]
 */
fun File.children(): Sequence<File> = subtree(1)

/**
 * 遍历当前目录下的目录 & 文件
 *
 * @param depth 递归的层级深度：
 * 如果为 1 时，只获取当前目录下的所有直接子目录/文件
 * 如果为 2 时，则获取两层目录与它们的文件
 * 以此类推
 * 如果为 [Int.MAX_VALUE] 时，则代表无限深度，用于获取所有目录以及其子文件
 * @param action 遍历到的文件 & 文件夹操作
 */
inline fun File.forEach(
  depth: Int = 1,
  action: (File) -> Unit
) {
  onEach(depth, action)
}

/**
 * 遍历当前目录下的目录 & 文件
 *
 * @param recursively 是否递归所有子目录
 * @param action 遍历到的文件 & 文件夹操作
 */
inline fun File.forEach(
  recursively: Boolean = false,
  action: (File) -> Unit
) {
  onEach(recursively, action)
}

/**
 * 遍历当前目录下的目录 & 文件，并返回当前的子目录树
 *
 * @param depth 递归的层级深度：
 * 如果为 1 时，只获取当前目录下的所有直接子目录/文件
 * 如果为 2 时，则获取两层目录与它们的文件
 * 以此类推
 * 如果为 [Int.MAX_VALUE] 时，则代表无限深度，用于获取所有目录以及其子文件
 * @param action 遍历到的文件 & 文件夹操作
 */
inline fun File.onEach(
  depth: Int = 1,
  action: (File) -> Unit
): Sequence<File> = subtree(depth).apply { forEach(action) }

/**
 * 遍历当前目录下的目录 & 文件，并返回当前的子目录树
 *
 * @param recursively 是否递归所有子目录
 * @param action 遍历到的文件 & 文件夹操作
 */
inline fun File.onEach(
  recursively: Boolean = false,
  action: (File) -> Unit
): Sequence<File> = subtree(if (recursively) Int.MAX_VALUE else 1).apply { forEach(action) }


/** Throws an [IOException] with the given [message] */
fun errorIO(message: Any): Nothing = throw IOException(message.toString())

/**
 * Throws an [IllegalArgumentException]
 * @receiver no such [File]
 * @param other file involved in the operation, if any (for example, the target of a copy or move)
 * @param message reason of error
 */
fun File.noSuchFile(message: String? = null, other: File? = null): Nothing =
  throw NoSuchFileException(this, other, message)

/**
 * Throws an [FileAlreadyExistsException]
 * @receiver existing [File]
 * @param other file involved in the operation, if any (for example, the target of a copy or move)
 * @param message reason of error
 */
fun File.alreadyExists(message: String? = null, other: File? = null): Nothing =
  throw FileAlreadyExistsException(this, other, message)

/**
 * Throws an [FileSystemException]
 * @receiver the file on which the failed operation was performed
 * @param other file involved in the operation, if any (for example, the target of a copy or move)
 * @param message reason of error
 */
fun File.error(message: String? = null, other: File? = null): Nothing =
  throw FileSystemException(this, other, message)