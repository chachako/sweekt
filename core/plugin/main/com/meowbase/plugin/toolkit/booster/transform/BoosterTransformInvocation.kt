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

package com.meowbase.plugin.toolkit.booster.transform

import com.android.build.api.transform.*
import com.android.build.api.transform.Status.*
import com.android.build.gradle.BaseExtension
import com.android.dx.command.dexer.Main
import com.meowbase.plugin.toolkit.booster.NCPU
import com.meowbase.plugin.toolkit.booster.green
import com.meowbase.plugin.toolkit.booster.red
import com.meowbase.plugin.toolkit.booster.transform
import com.meowbase.plugin.toolkit.ktx.*
import java.io.File
import java.net.URI
import java.util.concurrent.*

/**
 * Represents a delegate of TransformInvocation
 *
 * @author johnsonlee
 */
internal class BoosterTransformInvocation(
  private val delegate: TransformInvocation,
  internal val transform: BoosterTransform
) : TransformInvocation by delegate, TransformContext {

  private val project = transform.project

  private val outputs = CopyOnWriteArrayList<File>()

  override val name: String = delegate.context.variantName

  override val projectDir: File = project.projectDir

  override val buildDir: File = project.buildDir

  override val temporaryDir: File = delegate.context.temporaryDir

  override val reportsDir: File = File(buildDir, "reports").also { it.mkdirs() }

  override val bootClasspath = delegate.bootClasspath

  override val compileClasspath = delegate.compileClasspath

  override val runtimeClasspath = delegate.runtimeClasspath

  override val klassPool: AbstractKlassPool = object :
    AbstractKlassPool(compileClasspath, transform.bootKlassPool) {}

  override val applicationId = delegate.applicationId

  override val isDebuggable = variant.buildType.isDebuggable

  override val isDataBindingEnabled = delegate.isDataBindingEnabled

  override fun hasProperty(name: String) = project.hasProperty(name)

  @Suppress("UNCHECKED_CAST")
  override fun <T> getProperty(name: String, default: T): T =
    project.properties[name] as? T ?: default

  internal fun doFullTransform() = doTransform { transformFully(it) }

  internal fun doIncrementalTransform() = doTransform { transformIncrementally(it) }

  private fun onPreTransform() {
    transform.transformers.forEach {
      it.onPreTransform(this)
    }
  }

  private fun onPostTransform() {
    transform.transformers.forEach {
      it.onPostTransform(this)
    }
  }

  private fun doTransform(block: (ExecutorService) -> Iterable<Future<*>>) {
    this.outputs.clear()
    this.onPreTransform()

    val executor = Executors.newFixedThreadPool(NCPU)
    try {
      block(executor).forEach {
        it.get()
      }
    } finally {
      executor.shutdown()
      executor.awaitTermination(1, TimeUnit.HOURS)
    }

    this.onPostTransform()

    if (transform.verifyEnabled) {
      this.doVerify()
    }
  }

  private fun transformFully(executor: ExecutorService) = this.inputs.map {
    it.jarInputs + it.directoryInputs
  }.flatten().map { input ->
    executor.submit {
      val format = if (input is DirectoryInput) Format.DIRECTORY else Format.JAR
      outputProvider?.let { provider ->
        project.logger.info("Transforming ${input.file}")
        input.transform(
          provider.getContentLocation(
            input.name,
            input.contentTypes,
            input.scopes,
            format
          )
        )
      }
    }
  }

  private fun transformIncrementally(executor: ExecutorService) = this.inputs.map { input ->
    input.jarInputs.filter { it.status != NOTCHANGED }.map { jarInput ->
      executor.submit {
        doIncrementalTransform(jarInput)
      }
    } + input.directoryInputs.filter { it.changedFiles.isNotEmpty() }.map { dirInput ->
      val base = dirInput.file.toURI()
      executor.submit {
        doIncrementalTransform(dirInput, base)
      }
    }
  }.flatten()

  @Suppress("NON_EXHAUSTIVE_WHEN")
  private fun doIncrementalTransform(jarInput: JarInput) {
    when (jarInput.status) {
      REMOVED -> jarInput.file.delete()
      CHANGED, ADDED -> {
        project.logger.info("Transforming ${jarInput.file}")
        outputProvider?.let { provider ->
          jarInput.transform(
            provider.getContentLocation(
              jarInput.name,
              jarInput.contentTypes,
              jarInput.scopes,
              Format.JAR
            )
          )
        }
      }
    }
  }

  @Suppress("NON_EXHAUSTIVE_WHEN")
  private fun doIncrementalTransform(dirInput: DirectoryInput, base: URI) {
    dirInput.changedFiles.forEach { (file, status) ->
      when (status) {
        REMOVED -> {
          project.logger.info("Deleting $file")
          outputProvider?.let { provider ->
            provider.getContentLocation(
              dirInput.name,
              dirInput.contentTypes,
              dirInput.scopes,
              Format.DIRECTORY
            ).parentFile.listFiles()?.asSequence()
              ?.filter { it.isDirectory }
              ?.map { File(it, dirInput.file.toURI().relativize(file.toURI()).path) }
              ?.filter { it.exists() }
              ?.forEach { it.delete() }
          }
          file.delete()
        }
        ADDED, CHANGED -> {
          project.logger.info("Transforming $file")
          outputProvider?.let { provider ->
            val root = provider.getContentLocation(
              dirInput.name,
              dirInput.contentTypes,
              dirInput.scopes,
              Format.DIRECTORY
            )
            val output = File(root, base.relativize(file.toURI()).path)
            outputs += output
            file.transform(output) { bytecode ->
              bytecode.transform()
            }
          }
        }
      }
    }
  }

  private fun doVerify() {
    outputs.sortedBy(File::nameWithoutExtension).forEach { output ->
      val dex = temporaryDir.resolve(output.name)
      val args = Main.Arguments().apply {
        numThreads = NCPU
        debug = true
        warnings = true
        emptyOk = true
        multiDex = true
        jarOutput = true
        optimize = false
//        minSdkVersion = project.getAndroid<BaseExtension>()
//          .defaultConfig
//          .targetSdkVersion!!
//          .apiLevel
        fileNames = arrayOf(output.absolutePath)
        outName = dex.absolutePath
      }
      val rc = try {
        Main.run(args)
      } catch (t: Throwable) {
        t.printStackTrace()
        -1
      }

      println("${if (rc != 0) red("✗") else green("✓")} $output")
      dex.deleteRecursively()
    }
  }

  private fun QualifiedContent.transform(output: File) {
    outputs += output
    this.file.transform(output) { bytecode ->
      bytecode.transform()
    }
  }

  private fun ByteArray.transform(): ByteArray {
    return transform.transformers.fold(this) { bytes, transformer ->
      transformer.transform(this@BoosterTransformInvocation, bytes)
    }
  }
}
