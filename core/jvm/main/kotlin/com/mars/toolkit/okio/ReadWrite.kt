package com.mars.toolkit.okio

import okio.*
import java.io.*

/** 类似于 KotlinIo 中的 [Reader.forEachLine] */
fun Source.forEachLine(action: (String) -> Unit): Unit = useLines { it.forEach(action) }

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

fun Source.readBytes(): ByteArray = buffer().use { it.readByteArray() }