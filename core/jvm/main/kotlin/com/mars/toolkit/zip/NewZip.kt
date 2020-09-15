package com.mars.toolkit.zip

import java.io.File

/**
 * 用于创建一个新的 [Zip] 实例
 *
 * @author 凛
 * @date 2020/9/11 上午10:52
 * @github https://github.com/oh-Rin
 */
class NewZip internal constructor(elements: Collection<ZipEntry>) : Zip(null, null, elements) {
  @Deprecated(
    message = "不能使用此方法，因为当前 Zip 没有源文件！请使用 buildTo 手动指定新的压缩文件路径",
    level = DeprecationLevel.HIDDEN
  )
  override fun rebuild(
    bufferSize: Int,
    compressMode: CompressMode,
    compressLevel: CompressLevel,
    comment: String?
  ) = error("这是一个新的 Zip，你无法重新覆盖压缩它，因为它没有源文件！请使用 buildTo 手动指定新的压缩文件路径")
}

/** 根据 [File] 及其子文件 & 目录创建实例，且所有文件都将添加到 [Zip] */
fun File.toZip() = NewZip(this.asZipEntries.toList())

/** 根据多个 [File] 及其子文件 & 目录创建实例，且所有文件都将添加到 [Zip] */
fun Array<File>.toZip() = NewZip(this.flatMap { it.asZipEntries })
fun Iterable<File>.toZip() = NewZip(this.flatMap { it.asZipEntries })
fun Sequence<File>.toZip() = NewZip(this.flatMap { it.asZipEntries }.toList())

/** 根据多个 [File] 及其子文件 & 目录创建实例，且所有文件都将添加到 [Zip] */
fun zipOf(vararg files: File): NewZip = NewZip(files.flatMap { it.asZipEntries })
fun zipOf(files: Iterable<File>): NewZip = NewZip(files.flatMap { it.asZipEntries })
fun zipOf(files: Sequence<File>): NewZip = NewZip(files.flatMap { it.asZipEntries }.toList())

/** 创建一个空的压缩实例 */
fun emptyZip(): NewZip = NewZip(emptyList())