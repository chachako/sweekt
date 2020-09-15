package com.mars.toolkit.okio

import com.mars.toolkit.alreadyExists
import com.mars.toolkit.error
import com.mars.toolkit.noSuchFile
import okio.*
import java.io.*
import java.io.IOException
import java.nio.charset.Charset

/**
 * 以字节数组的形式获取文件的所有内容
 * @see kotlin.io.readBytes
 */
fun File.readBytes(): ByteArray = bufferedSource().use { it.readByteArray() }

/**
 * 将字节数组 [array] 写入并覆盖到文件
 *
 * @param array 新的文件的内容
 * @see kotlin.io.writeBytes
 */
fun File.writeBytes(array: ByteArray): Unit = bufferedSink().use { it.write(array) }

/**
 * 将闭包内的字节数组写入并覆盖到文件
 *
 * @param block 返回一个新的字节数组
 * @see writeBytes
 */
fun File.writeBytes(block: (origin: ByteArray) -> ByteArray): Unit = writeBytes(block(readBytes()))

/**
 * 将字节数组 [array] 追加到文件的内容中
 * @see kotlin.io.appendBytes
 */
fun File.appendBytes(array: ByteArray): Unit = bufferedAppendingSink().use { it.write(array) }

/**
 * 将闭包内的字节数组追加到文件的内容中
 *
 * @param block 返回一个新的字节数组
 * @see appendBytes
 */
fun File.appendBytes(block: (origin: ByteArray) -> ByteArray): Unit = appendBytes(block(readBytes()))

/**
 * 将数据 [source] 写入并覆盖到文件
 *
 * @param source 新的文件的内容的源
 */
fun File.writeSource(source: Source): Unit = bufferedSink().use { it.writeAll(source) }

/** 将数据 [source] 追加到文件的内容中 */
fun File.appendAll(source: Source): Unit = bufferedAppendingSink().use { it.writeAll(source) }

/**
 * 使用 UTF-8 字符集并获取文件的全部内容
 *
 * @return 文件的所有内容作为字符串
 * @see kotlin.io.readText
 */
fun File.readText(): String = bufferedSource().use { it.readText() }

/**
 * 将文本 [text] 写入并覆盖到文件
 *
 * @param text 要写入到文件的文本
 * @param charset 要使用的字符集
 * @see kotlin.io.writeText
 */
fun File.writeText(
  text: String,
  charset: Charset = Charsets.UTF_8
): Unit = writeBytes(text.toByteArray(charset))

/**
 * 将闭包内返回的文本写入并覆盖到文件
 *
 * @param charset 要使用的字符集
 * @param textBlock 返回一个新的文本
 * @see writeText
 */
fun File.writeText(
  charset: Charset = Charsets.UTF_8,
  textBlock: (origin: String) -> String
): Unit = writeText(textBlock(readText()), charset)

/**
 * 将文本 [text] 追加到文件的内容中
 *
 * @param text 要追加到文件的文本
 * @param charset 要使用的字符集
 * @see kotlin.io.appendText
 */
fun File.appendText(
  text: String,
  charset: Charset = Charsets.UTF_8
): Unit = appendBytes(text.toByteArray(charset))

/**
 * 将闭包内返回的文本追加到文件的内容中
 *
 * @param charset 要使用的字符集
 * @param textBlock 返回一个新的文本
 * @see appendText
 */
fun File.appendText(
  charset: Charset = Charsets.UTF_8,
  textBlock: (origin: String) -> String
): Unit = appendText(textBlock(readText()), charset)

/**
 * 按字节块读取文件，并为每个读取的块调用操作
 *
 * @param blockSize 每次读取的字节块的大小，比如文件大小为 7409 字节，那么就会调用两次 [action]:
 * 第一次是在读取 4096 字节时，第二次是在读取余下的 3313 字节
 * @param action 处理文件块
 * @see kotlin.io.forEachBlock
 */
fun File.forEachBlock(blockSize: Int = 4096, action: (buffer: ByteArray, bytesRead: Int) -> Unit) =
  bufferedSource().forEachBlock(blockSize, action)

/**
 * 使用 UTF8 字符集逐行读取此文件
 * @see kotlin.io.forEachLine
 */
fun File.forEachLine(action: (line: String) -> Unit) = bufferedSource().forEachLine(action)

/**
 * 使用 UTF8 字符集并以行列表的形式读取文件内容
 * @see kotlin.io.readLines
 */
fun File.readLines(): List<String> = bufferedSource().readLines()

/**
 * 提供文件中所有行的序列，并在 [block] 处理完成后关闭 [BufferedSource]
 * @see kotlin.io.useLines
 */
inline fun <T> File.useLines(block: (Sequence<String>) -> T): T = bufferedSource().useLines(block)

/**
 * 将此文件复制到目标文件
 *
 * @param target 需要复制到的目标文件
 * @param overwrite 如果目标文件已存在，是否覆盖目标文件
 * @throws NoSuchFileException 如果源文件不存在
 * @throws FileAlreadyExistsException 如果目标文件存在，并且 [overwrite] 为 false
 * @throws IOException 其他的复制错误
 * @see kotlin.io.copyTo
 */
fun File.copyTo(target: File, overwrite: Boolean = false): File {
  if (!exists()) noSuchFile("The source file doesn't exist.")

  if (target.exists()) {
    if (!overwrite) alreadyExists("The destination file already exists.", target)
    else if (!target.delete()) alreadyExists(
      "Tried to overwrite the destination, but failed to delete it.",
      target
    )
  }

  if (this.isDirectory) {
    if (!target.mkdirs()) error("Failed to create target directory.", target)
  } else {
    target.parentFile?.mkdirs()
    this.bufferedSource().copyTo(target.sink())
  }

  return target
}

/**
 * 将此文件复制到目标文件
 *
 * @param targetPath 需要复制到的目标文件的路径
 * @param overwrite 如果目标文件已存在，是否覆盖目标文件
 * @throws NoSuchFileException 如果源文件不存在
 * @throws FileAlreadyExistsException 如果目标文件存在，并且 [overwrite] 为 false
 * @throws IOException 其他的复制错误
 * @see kotlin.io.copyTo
 */
fun File.copyTo(targetPath: String, overwrite: Boolean = false): File =
  copyTo(File(targetPath), overwrite)

/**
 * 将当前目录下的所有子 [File] 复制到目标目录
 *
 * @see kotlin.io.copyRecursively 更详细的 doc
 *
 * @param target 需要复制到的目标目录
 * @param overwrite 是否允许覆盖已经存在的目标目录及文件
 * @throws NoSuchFileException 如果源文件不存在
 * @throws FileAlreadyExistsException 如果目标文件存在，并且 [overwrite] 为 false
 * @throws IOException 其他的复制错误
 * @return 一旦复制中途中出现了任何错误或终止则会返回 false, 成功了就会返回 true
 */
fun File.copyRecursively(
  target: File,
  overwrite: Boolean = false,
  onError: (File, IOException) -> OnErrorAction = { _, exception -> throw exception }
): Boolean {
  if (!exists()) {
    return onError(
      this,
      NoSuchFileException(file = this, reason = "The source file doesn't exist.")
    ) != OnErrorAction.TERMINATE
  }
  try {
    // We cannot break for loop from inside a lambda, so we have to use an exception here
    for (src in walkTopDown().onFail { f, e ->
      if (onError(f, e) == OnErrorAction.TERMINATE) throw FileSystemException(f)
    }) {
      if (!src.exists()) {
        if (onError(
            src,
            NoSuchFileException(file = src, reason = "The source file doesn't exist.")
          ) == OnErrorAction.TERMINATE
        ) return false
      } else {
        val relPath = src.toRelativeString(this)
        val dstFile = File(target, relPath)
        if (dstFile.exists() && !(src.isDirectory && dstFile.isDirectory)) {
          val stillExists = if (!overwrite) true else {
            if (dstFile.isDirectory)
              !dstFile.deleteRecursively()
            else
              !dstFile.delete()
          }

          if (stillExists) {
            if (onError(
                dstFile, FileAlreadyExistsException(
                  file = src,
                  other = dstFile,
                  reason = "The destination file already exists."
                )
              ) == OnErrorAction.TERMINATE
            ) return false

            continue
          }
        }

        if (src.isDirectory) {
          dstFile.mkdirs()
        } else {
          // 使用 okio 进行复制
          if (src.copyTo(dstFile, overwrite).length() != src.length()) {
            if (onError(
                src,
                IOException("Source file wasn't copied completely, length of destination file differs.")
              ) == OnErrorAction.TERMINATE
            ) return false
          }
        }
      }
    }
    return true
  } catch (e: FileSystemException) {
    return false
  }
}