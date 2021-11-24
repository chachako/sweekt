/*
 * Copyright (c) 2021. The Meowool Organization Open Source Project
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
@file:Suppress("MemberVisibilityCanBePrivate", "NestedLambdaShadowedImplicitParameter")

import com.meowool.sweekt.SweektCommandLineProcessor
import com.meowool.sweekt.SweektComponentRegistrar
import com.meowool.sweekt.SweektNames.Root
import com.meowool.toolkit.compiler.BuildConfig
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.Result
import com.tschuchort.compiletesting.PluginOption
import com.tschuchort.compiletesting.SourceFile
import org.intellij.lang.annotations.Language
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicInteger

private var sourcesId = AtomicInteger()

internal fun compile(
  @Language("kotlin") vararg sources: String,
  block: Result.(lineCalculator: (Int) -> Int) -> Unit = { }
): Result = KotlinCompilation().also {
  it.sources = sources.map { content ->
    SourceFile.kotlin(
      "Sources${sourcesId.incrementAndGet()}.kt",
      "package $Root\n\n" + content.trimIndent()
    )
  }
  it.compilerPlugins = listOf(SweektComponentRegistrar())
  it.commandLineProcessors = listOf(SweektCommandLineProcessor())
  it.pluginOptions = listOf(PluginOption(BuildConfig.CompilerId, "isLogging", "true"))
  it.kotlincArguments = listOf("-Xjvm-default=compatibility")
  it.inheritClassPath = true
  it.verbose = false
  it.useIR = true
}.compile().apply {
  compiledClassAndResourceFiles.forEach { println("compiled: " + it.absolutePath) }
  block { 2 + it }
}

internal fun Result.classOf(className: String, vararg values: Any?, block: ClassBlock.() -> Unit = {}) =
  ClassBlock(classLoader.loadClass("$Root.$className").getDeclaredConstructor(*values.map { Any::class.java }.toTypedArray()).newInstance(*values)).apply(block)

internal fun Result.objectOf(className: String, block: ClassBlock.() -> Unit = {}) =
  classLoader.loadClass("$Root.$className").let {
    ClassBlock(it.getDeclaredField("INSTANCE").get(null), it).apply(block)
  }

internal fun Result.interfaceOf(className: String, block: ClassBlock.() -> Unit = {}) =
  classLoader.loadClass("$Root.$className").let {
    ClassBlock(null, it).apply(block)
  }

internal class ClassBlock(val instance: Any?, val javaClass: Class<*> = instance!!.javaClass) {
  private val fields = mutableMapOf<String, Field>()
  private val methods = mutableMapOf<String, Method>()

  fun field(name: String) = fields.getOrPut(name) {
    javaClass.getDeclaredField(name).apply {
      isAccessible = true
    }
  }.get(instance)

  fun setField(name: String, value: Any) {
    fields.getOrPut(name) {
      javaClass.getDeclaredField(name).apply {
        isAccessible = true
      }
    }.set(instance, value)
  }

  fun method(name: String, vararg values: Any?) = methods.getOrPut(name) {
    javaClass.getDeclaredMethod(name, *values.map { Any::class.java }.toTypedArray()).apply {
      isAccessible = true
    }
  }.invoke(instance, *values)

  operator fun String.invoke(vararg values: Any?) = method(this, *values)

  override fun equals(other: Any?): Boolean = other == instance
  override fun hashCode(): Int = instance.hashCode()
  override fun toString(): String = instance.toString()
}
