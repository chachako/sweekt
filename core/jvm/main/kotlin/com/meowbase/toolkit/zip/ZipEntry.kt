@file:Suppress("MemberVisibilityCanBePrivate", "FunctionName")

package com.meowbase.toolkit.zip

import com.meowbase.toolkit.existOrMkdir
import com.meowbase.toolkit.okio.readText
import com.meowbase.toolkit.okio.writeSource
import com.meowbase.toolkit.tree
import okio.Source
import okio.source
import java.io.File
import java.util.zip.ZipEntry as NativeZipEntry

/** 将当前文件与其所有子文件 / 文件夹作为 [ZipEntry] 返回 */
val File.asZipEntries: Sequence<ZipEntry>
  get() = when {
    isFile -> sequenceOf(ZipEntry(name, source()))
    else -> {
      val parent = parent
      tree().mapNotNull {
        if (it.isFile) {
          ZipEntry(
            data = it.source(),
            name = it.absolutePath
              .replaceFirst(parent, "")
              .removePrefix(File.separator),
          )
        } else null
      }
    }
  }

/**
 * 定义 Zip 的条目
 *
 * @property name 条目的名称或路径
 * @property data 未压缩的条目数据源
 * @property size 未压缩条目数据的大小
 * @property crc 未压缩条目数据的 CRC-32 校验和
 * @property time 条目的最后修改时间
 * @property compressedSize 压缩后的条目数据大小
 * @property extra 条目的附加数据
 * @property comment 条目的注释字符串
 * @property mode 条目的压缩模式
 */
class ZipEntry(
  var name: String,
  val data: Source,
  var size: Long? = null,
  var crc: Long? = null,
  var time: Long? = null,
  var compressedSize: Long? = null,
  var extra: ByteArray? = null,
  var comment: String? = null,
  var mode: CompressMode = CompressMode.Deflated,
) {
  val native: NativeZipEntry
    get() = NativeZipEntry(name).also {
      time?.apply(it::setTime)
      size?.apply(it::setSize)
      crc?.apply(it::setCrc)
      extra?.apply(it::setExtra)
      comment?.apply(it::setComment)
      compressedSize?.apply(it::setCompressedSize)
      mode.native.apply(it::setMethod)
    }

  /**
   * 将 Zip 条目写出到文件夹中
   *
   * @param targetDirectory 将所有 Zip 条目解压到此目标目录
   * @param overwrite 当目标路径上存在同名文件或文件夹时直接覆盖解压
   * @note 当目标文件已存在并且 [overwrite] 为 false 时，跳过此操作
   */
  fun writeTo(targetDirectory: File, overwrite: Boolean = false) =
    targetDirectory.resolve(name)._write(overwrite)

  /**
   * 将 Zip 条目扁平的写出到文件夹中
   * 所有条目都将会扁平的解压到 [targetDirectory] 直接目录中
   *
   * @param targetDirectory 将所有 Zip 条目解压到此目标目录
   * @param overwrite 当目标路径上存在同名文件或文件夹时直接覆盖解压
   * @note 当目标文件已存在并且 [overwrite] 为 false 时，跳过此操作
   */
  fun flatWriteTo(targetDirectory: File, overwrite: Boolean = false) {
    var name = name
    if (name.last() == File.separatorChar) name = name.substringBeforeLast(File.separatorChar)
    targetDirectory.resolve(name.substringAfterLast(File.separatorChar))._write(overwrite)
  }

  private fun File._write(overwrite: Boolean = false) {
    parentFile.existOrMkdir()
    if (exists()) {
      if (overwrite) delete()
      else return
    }
    writeSource(data)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ZipEntry

    if (name != other.name) return false
    if (size != other.size) return false
    if (crc != other.crc) return false
    if (time != other.time) return false
    if (compressedSize != other.compressedSize) return false
    if (extra != null) {
      if (other.extra == null) return false
      if (!extra!!.contentEquals(other.extra!!)) return false
    } else if (other.extra != null) return false
    if (comment != other.comment) return false
    if (mode != other.mode) return false
    if (data != other.data) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + (size?.hashCode() ?: 0)
    result = 31 * result + (crc?.hashCode() ?: 0)
    result = 31 * result + (time?.hashCode() ?: 0)
    result = 31 * result + (compressedSize?.hashCode() ?: 0)
    result = 31 * result + (extra?.contentHashCode() ?: 0)
    result = 31 * result + (comment?.hashCode() ?: 0)
    result = 31 * result + mode.hashCode()
    result = 31 * result + data.hashCode()
    return result
  }

  override fun toString(): String {
    return "ZipEntry(name='$name', size=$size, crc=$crc, time=$time, compressedSize=$compressedSize, extra=${extra?.contentToString()}, comment=$comment, mode=$mode, data=${data.readText()})"
  }

}

/**
 * 将所有 Zip 条目写出到文件夹中
 *
 * @param targetDirectory 将所有 Zip 条目解压到此目标目录
 * @param overwrite 当目标路径上存在同名文件或文件夹时直接覆盖解压
 * @see ZipEntry.writeTo
 */
fun Iterable<ZipEntry>.writeTo(targetDirectory: File, overwrite: Boolean = false) =
  forEach { it.writeTo(targetDirectory, overwrite) }

fun Sequence<ZipEntry>.writeTo(targetDirectory: File, overwrite: Boolean = false) =
  forEach { it.writeTo(targetDirectory, overwrite) }

fun Array<ZipEntry>.writeTo(targetDirectory: File, overwrite: Boolean = false) =
  forEach { it.writeTo(targetDirectory, overwrite) }

/**
 * 将所有 Zip 条目扁平的写出到文件夹中
 * 所有条目都将会扁平的解压到 [targetDirectory] 直接目录中
 *
 * @param targetDirectory 将所有 Zip 条目解压到此目标目录
 * @param overwrite 当目标路径上存在同名文件或文件夹时直接覆盖解压
 * @see ZipEntry.flatWriteTo
 */
fun Iterable<ZipEntry>.flatWriteTo(targetDirectory: File, overwrite: Boolean = false) =
  forEach { it.flatWriteTo(targetDirectory, overwrite) }

fun Sequence<ZipEntry>.flatWriteTo(targetDirectory: File, overwrite: Boolean = false) =
  forEach { it.flatWriteTo(targetDirectory, overwrite) }

fun Array<ZipEntry>.flatWriteTo(targetDirectory: File, overwrite: Boolean = false) =
  forEach { it.flatWriteTo(targetDirectory, overwrite) }