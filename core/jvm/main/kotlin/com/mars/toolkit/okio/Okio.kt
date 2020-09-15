package com.mars.toolkit.okio

import okio.*
import okio.source
import java.io.File

/** 返回一个缓冲的 [Source] */
fun File.bufferedSource(): BufferedSource = source().buffer()

/** 返回一个缓冲的 [Sink] */
fun File.bufferedSink(append: Boolean = false): BufferedSink = sink(append).buffer()

/** 返回一个可追加的缓冲的 [Sink] */
fun File.bufferedAppendingSink(): BufferedSink = bufferedSink(true)

/**
 * 将这个源的内容复制到输出接收器 [out] 中
 * 无论是 [Source] 还是 [Sink]，它们都会在复制完成后自动关闭
 */
fun Source.copyTo(out: Sink): Long {
  val sink = out as? BufferedSink ?: out.buffer()
  return this.use { input ->
    sink.use { output ->
      output.writeAll(input)
    }
  }
}