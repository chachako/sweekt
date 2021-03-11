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

package com.meowbase.toolkit.okio

import com.meowbase.toolkit.long
import com.meowbase.toolkit.orElse
import com.meowbase.toolkit.safeCast
import okio.Buffer
import okio.BufferedSource
import okio.Source
import okio.buffer
import java.io.BufferedReader
import java.io.Reader

/** 类似于 KotlinIo 中的 [Reader.forEachLine] */
fun Source.forEachLine(action: (String) -> Unit) = useLines { it.forEach(action) }

/** 类似于 KotlinIo 中的 [Reader.readLines] */
fun Source.readLines(): List<String> {
  val result = arrayListOf<String>()
  forEachLine { result.add(it) }
  return result
}

/** 类似于 KotlinIo 中的 [Reader.useLines] */
inline fun <T> Source.useLines(block: (Sequence<String>) -> T): T =
  buffer().use { block(it.lineSequence()) }

/** 类似于 KotlinIo 中的 [BufferedReader.lineSequence] */
fun BufferedSource.lineSequence(): Sequence<String> = generateSequence { readUtf8Line() }

/** 类似于 KotlinIo 中的 [Reader.readText] */
fun Source.readText(): String = buffer().use { it.readUtf8() }

/** 类似于 KotlinIo 中的 [kotlin.io.forEachBlock] */
fun BufferedSource.forEachBlock(buffer: ByteArray, f: (buffer: ByteArray, bytesRead: Int) -> Unit) {
  var bytesRead = read(buffer)
  while (bytesRead != -1) {
    f(buffer, bytesRead)
    bytesRead = read(buffer)
  }
}

/** 类似于 KotlinIo 中的 [kotlin.io.forEachBlock] */
fun BufferedSource.forEachBlock(blockSize: Int, f: (buffer: ByteArray, bytesRead: Int) -> Unit) =
  forEachBlock(ByteArray(blockSize), f)

/** 类似于 KotlinIo 中的 [kotlin.io.readBytes] */
fun Source.readBytes(): ByteArray =
  this.safeCast<BufferedSource>().orElse(::buffer).use { it.readByteArray() }

/** 将这个 [Buffer] 的限定数据复制到新的缓冲区中 */
fun Buffer.copy(
  offset: Long,
  byteCount: Long = size - offset
): Buffer = Buffer().also { out ->
  this.copyTo(out, offset, byteCount)
}

/**
 * 从源创建一个新的缓冲区并使用它
 * 与 [Source.buffer] 不同的是，我们创建 [Buffer] 而不是 [BufferedSource]
 */
inline fun Source.useBuffer(block: (buffer: Buffer) -> Unit) {
  use { source ->
    Buffer().apply { writeAll(source) }.use(block)
  }
}

/** 读取 [byteCount] 个字节到新的缓冲区 */
fun Buffer.readToBuffer(byteCount: Number): Buffer = Buffer().also { read(it, byteCount.long) }

fun BufferedSource.readUInt(): UInt = readInt().toUInt()

fun BufferedSource.readUIntLe(): UInt = readIntLe().toUInt()

fun BufferedSource.readUByte(): UByte = readByte().toUByte()