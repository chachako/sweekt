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

@file:Suppress("SuspiciousVarProperty", "MemberVisibilityCanBePrivate", "NAME_SHADOWING", "unused")

package com.meowbase.toolkit.zip

import com.meowbase.toolkit.iterations.replaceAll
import com.meowbase.toolkit.iterations.toList
import com.meowbase.toolkit.iterations.toMutableList
import com.meowbase.toolkit.okio.readBytes
import okio.source
import java.io.File
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * 定义 Zip 压缩文件的数据源
 * 用于打开一个压缩包
 *
 * @author 凛
 * @date 2020/9/10 下午7:22
 * @github https://github.com/RinOrz
 * @property source Zip 的源文件实例
 * @property comment Zip 源文件注释
 * @see File.openZip
 */
open class Zip internal constructor(
  protected val source: File?,
  val comment: String?,
  elements: Collection<ZipEntry>
) : ArrayList<ZipEntry>(elements) {
  protected val defaultBufferSize: Int
    get() = when {
      source == null -> DEFAULT_BUFFER_SIZE
      // 大于 50m 则缓冲区每次读取 50kb 的字节
      source.length() > 52428800 -> 51200
      // 大于 100m 则缓冲区每次读取 100kb 的字节
      source.length() > 104857600 -> 102400
      else -> DEFAULT_BUFFER_SIZE
    }

  /**
   * 将当前 Zip 数据源压缩到文件 [target]
   *
   * @param target 目标 Zip 文件
   * @param overwrite 覆盖已经存在的 Zip 文件，否则将会追加压缩条目到 Zip 中
   * @param bufferSize 文件压缩时的缓冲区大小
   * @param comment Zip 注释
   * @param compressMode 压缩模式
   * @param compressLevel 压缩等级，压缩力度越大速度越慢。[CompressLevel.level] 需要在 0-9
   */
  fun buildTo(
    target: File,
    overwrite: Boolean = false,
    bufferSize: Int = defaultBufferSize,
    compressMode: CompressMode = CompressMode.Deflated,
    compressLevel: CompressLevel = CompressLevel.Default,
    comment: String? = null,
  ) {
    val compressLevel = compressLevel.level
    check(compressLevel <= 9 && compressLevel >= -1) { "压缩等级只能在 -1 到 9 之间，-1 为默认压缩" }
    if (target.exists()) when {
      overwrite -> target.delete()
      else -> target.openZip().plus(this).buildTo(
        target = target,
        bufferSize = bufferSize,
        comment = comment,
        compressMode = compressMode,
        overwrite = true,
      )
    }
    target.outputStream().buffered(bufferSize).use { buffered ->
      ZipOutputStream(buffered).use { zos ->
        zos.setLevel(compressLevel)
        zos.setMethod(compressMode.native)
        comment?.apply(zos::setComment)
        // 压缩文件到 Zip 中
        this.forEach {
          zos.putNextEntry(it.native)
          zos.write(it.data.readBytes())
          zos.closeEntry()
        }
      }
    }
  }

  /**
   * 将当前 Zip 数据源扁平的压缩到文件 [target]
   * 所有条目都将会扁平的压缩到 Zip 的根目录 "/"
   *
   * @param target 目标 Zip 文件
   * @param overwrite 覆盖已经存在的 Zip 文件，否则将会追加压缩条目到 Zip 中
   * @param bufferSize 文件压缩时的缓冲区大小
   * @param comment Zip 注释
   * @param compressMode 压缩模式
   * @param compressLevel 压缩等级，压缩力度越大速度越慢。[CompressLevel.level] 需要在 0-9
   */
  fun flatBuildTo(
    target: File,
    overwrite: Boolean = false,
    bufferSize: Int = defaultBufferSize,
    compressMode: CompressMode = CompressMode.Deflated,
    compressLevel: CompressLevel = CompressLevel.Default,
    comment: String? = null,
  ) {
    replaceAll(map {
      it.apply {
        name = name.substringAfterLast("/")
      }
    })
    buildTo(
      target = target,
      overwrite = overwrite,
      bufferSize = bufferSize,
      compressMode = compressMode,
      compressLevel = compressLevel,
      comment = comment,
    )
  }

  /**
   * 将 Zip 数据源压缩并覆盖到此 Zip 的源文件
   *
   * @param bufferSize 文件压缩时的缓冲区大小
   * @param comment Zip 注释
   * @param compressMode 压缩模式
   * @param compressLevel 压缩等级，压缩力度越大速度越慢。[CompressLevel.level] 需要在 0-9
   * @see source 源 Zip 文件
   */
  open fun rebuild(
    bufferSize: Int = defaultBufferSize,
    compressMode: CompressMode = CompressMode.Deflated,
    compressLevel: CompressLevel = CompressLevel.Default,
    comment: String? = null,
  ) = buildTo(
    target = source!!,
    overwrite = true,
    bufferSize = bufferSize,
    compressMode = compressMode,
    compressLevel = compressLevel,
    comment = comment,
  )


  /** 追加其他压缩包中的所有条目 */
  operator fun plus(elements: Zip): Zip = apply {
    addAll(elements)
  }

  /** 追加压缩条目 */
  operator fun plus(element: ZipEntry): Zip = apply {
    add(element)
  }

  /** 追加压缩文件与其子文件 / 目录 */
  operator fun plus(element: File): Zip = apply {
    addAll(element.asZipEntries)
  }

  fun add(element: File): Boolean {
    return addAll(element.asZipEntries)
  }

  fun add(index: Int, element: File) {
    addAll(index, element.asZipEntries.toList())
  }

  /** 追加多个压缩条目 */
  fun plus(vararg elements: File): Zip =
    apply { addAll(elements.flatMap { it.asZipEntries }) }

  fun plus(vararg elements: ZipEntry): Zip =
    apply { addAll(elements) }

  operator fun plus(elements: Iterable<ZipEntry>): Zip =
    apply { addAll(elements) }

  operator fun plus(elements: Sequence<ZipEntry>): Zip =
    apply { addAll(elements) }

  fun addAll(elements: Iterable<ZipEntry>): Boolean =
    (this as MutableCollection<ZipEntry>).addAll(elements)

  fun addAll(elements: Sequence<ZipEntry>): Boolean =
    (this as MutableCollection<ZipEntry>).addAll(elements)

  fun addAll(index: Int, elements: Iterable<ZipEntry>): Boolean =
    super.addAll(index, elements.toList())

  fun addAll(index: Int, elements: Sequence<ZipEntry>): Boolean =
    super.addAll(index, elements.toList())

}

/** 将 [File] 作为 Zip 文件并打开 */
fun File.openZip(): Zip = ZipFile(this).let { zip ->
  Zip(
    source = this,
    comment = zip.comment,
    elements = zip.entries().toList().toMutableList().map {
      ZipEntry(
        name = it.name,
        size = it.size,
        time = it.time,
        crc = it.crc,
        extra = it.extra,
        comment = it.comment,
        compressedSize = it.compressedSize,
        mode = CompressMode(it.method),
        data = zip.getInputStream(it).source(),
      )
    }
  )
}