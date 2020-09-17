package com.mars.gradle.plugin.toolkit.booster.transform.asm

import com.android.build.gradle.BaseExtension
import com.mars.gradle.plugin.toolkit.booster.transform.BoosterTransform
import com.mars.gradle.plugin.toolkit.booster.transform.TransformContext
import com.mars.gradle.plugin.toolkit.booster.transform.Transformer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.InputStream
import java.lang.management.ManagementFactory
import java.lang.management.ThreadMXBean
import java.util.jar.JarFile

/**
 * Represents bytecode transformer using ASM
 *
 * @author johnsonlee
 */
class AsmTransformer(private val transformers: Array<out ClassTransformer>) : Transformer {

  private val threadMxBean = ManagementFactory.getThreadMXBean()

  private val durations = mutableMapOf<ClassTransformer, Long>()

  override fun onPreTransform(context: TransformContext) {
    this.transformers.forEach { transformer ->
      this.threadMxBean.sumCpuTime(transformer) {
        transformer.onPreTransform(context)
      }
    }
  }

  override fun transform(context: TransformContext, bytecode: ByteArray): ByteArray {
    return ClassWriter(ClassWriter.COMPUTE_MAXS).also { writer ->
      this.transformers.fold(ClassNode().also { klass ->
        ClassReader(bytecode).accept(klass, 0)
      }) { klass, transformer ->
        this.threadMxBean.sumCpuTime(transformer) {
          transformer.transform(context, klass)
        }
      }.accept(writer)
    }.toByteArray()
  }

  override fun onPostTransform(context: TransformContext) {
    this.transformers.forEach { transformer ->
      this.threadMxBean.sumCpuTime(transformer) {
        transformer.onPostTransform(context)
      }
    }

    val w1 = this.durations.keys.map {
      it.javaClass.name.length
    }.max() ?: 20
    this.durations.forEach { (transformer, ns) ->
      println("${transformer.javaClass.name.padEnd(w1 + 1)}: ${ns / 1000000} ms")
    }
  }

  private fun <R> ThreadMXBean.sumCpuTime(transformer: ClassTransformer, action: () -> R): R {
    val ct0 = this.currentThreadCpuTime
    val result = action()
    val ct1 = this.currentThreadCpuTime
    durations[transformer] = durations.getOrDefault(transformer, 0) + (ct1 - ct0)
    return result
  }

}

fun JarFile.transform(
  name: String,
  consumer: (ClassNode) -> Unit
) = getJarEntry(name)?.let { entry ->
  getInputStream(entry).use { input ->
    consumer(input.asClassNode())
  }
}

fun ByteArray.asClassNode() = ClassNode().also { klass ->
  ClassReader(this).accept(klass, 0)
}

fun InputStream.asClassNode() = readBytes().asClassNode()

fun File.asClassNode(): ClassNode = readBytes().asClassNode()

fun Project.registerAsmTransforms(vararg transformers: ClassTransformer) =
  extensions.findByType<BaseExtension>()?.registerTransform(
    BoosterTransform(this, listOf(AsmTransformer(transformers)))
  ) ?: error("$name 项目不允许注册 Gradle Transformer!")