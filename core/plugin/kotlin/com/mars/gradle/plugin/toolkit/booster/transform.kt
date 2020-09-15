package com.mars.gradle.plugin.toolkit.booster

import com.mars.toolkit.existOrNewFile
import com.mars.toolkit.okio.bufferedSource
import com.mars.toolkit.okio.*
import com.mars.toolkit.subtree
import okio.Source
import okio.source
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.apache.commons.compress.parallel.InputStreamSupplier
import java.io.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.jar.JarFile
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

/**
 * Transform this file or directory to the output by the specified transformer
 *
 * @param output The output location
 * @param transformer The byte data transformer
 */
fun File.transform(output: File, transformer: (ByteArray) -> ByteArray = { it -> it }) {
  when {
    isDirectory -> this.toURI().let { base ->
      this.subtree().toList().parallelStream().forEach {
        it.transform(File(output, base.relativize(it.toURI()).path), transformer)
      }
    }
    isFile -> when (extension.toLowerCase(Locale.getDefault())) {
      "jar" -> JarFile(this).use {
        it.transform(output, transformer = transformer)
      }
      "class" -> this.bufferedSource().use {
        output.writeBytes(it.transform(transformer))
      }
      else -> this.copyTo(output, true)
    }
    else -> throw IOException("Unexpected file: ${this.absolutePath}")
  }
}

fun Source.transform(transformer: (ByteArray) -> ByteArray): ByteArray {
  return transformer(readBytes())
}

fun ZipFile.transform(
  output: OutputStream,
  entryFactory: (ZipEntry) -> ZipArchiveEntry = ::ZipArchiveEntry,
  transformer: (ByteArray) -> ByteArray = { it -> it }
) {
  val entries = mutableSetOf<String>()
  val creator = ParallelScatterZipCreator(
    ThreadPoolExecutor(
      NCPU,
      NCPU,
      0L,
      TimeUnit.MILLISECONDS,
      LinkedBlockingQueue(),
      Executors.defaultThreadFactory(),
      { runnable, _ ->
        runnable.run()
      })
  )

  entries().asSequence().forEach { entry ->
    if (!entries.contains(entry.name)) {
      val zae = entryFactory(entry)
      val stream = InputStreamSupplier {
        when (entry.name.substringAfterLast('.', "")) {
          "class" -> getInputStream(entry).buffered().use { src ->
            try {
              src.source().transform(transformer).inputStream()
            } catch (e: Throwable) {
              System.err.println("Broken class: ${this.name}!/${entry.name}")
              getInputStream(entry)
            }
          }
          else -> getInputStream(entry)
        }
      }

      creator.addArchiveEntry(zae, stream)
      entries.add(entry.name)
    } else {
      System.err.println("Duplicated jar entry: ${this.name}!/${entry.name}")
    }
  }

  ZipArchiveOutputStream(output).use { creator.writeTo(it) }
}

fun ZipFile.transform(
  output: File,
  entryFactory: (ZipEntry) -> ZipArchiveEntry = { ZipArchiveEntry(it) },
  transformer: (ByteArray) -> ByteArray = { it -> it }
) = output.existOrNewFile(force = true).outputStream().buffered().use {
  transform(it, entryFactory, transformer)
}

fun ZipInputStream.transform(
  output: OutputStream,
  entryFactory: (ZipEntry) -> ZipArchiveEntry = { ZipArchiveEntry(it) },
  transformer: (ByteArray) -> ByteArray
) {
  val creator = ParallelScatterZipCreator()
  val entries = mutableSetOf<String>()

  while (true) {
    val entry = nextEntry?.takeIf { true } ?: break
    if (!entries.contains(entry.name)) {
      val zae = entryFactory(entry)
      val data = readBytes()
      val stream = InputStreamSupplier {
        transformer(data).inputStream()
      }
      creator.addArchiveEntry(zae, stream)
      entries.add(entry.name)
    }
  }

  ZipArchiveOutputStream(output).use { creator.writeTo(it) }
}

fun ZipInputStream.transform(
  output: File,
  entryFactory: (ZipEntry) -> ZipArchiveEntry = { ZipArchiveEntry(it) },
  transformer: (ByteArray) -> ByteArray
) = output.existOrNewFile(force = true).outputStream().buffered().use {
  transform(it, entryFactory, transformer)
}

private const val DEFAULT_BUFFER_SIZE = 8 * 1024

private fun InputStream.readBytes(estimatedSize: Int = DEFAULT_BUFFER_SIZE): ByteArray {
  val buffer = ByteArrayOutputStream(estimatedSize.coerceAtLeast(this.available()))
  copyTo(buffer)
  return buffer.toByteArray()
}

private fun InputStream.copyTo(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
  var bytesCopied: Long = 0
  val buffer = ByteArray(bufferSize)
  var bytes = read(buffer)
  while (bytes >= 0) {
    out.write(buffer, 0, bytes)
    bytesCopied += bytes
    bytes = read(buffer)
  }
  return bytesCopied
}
