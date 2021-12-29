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
@file:Suppress("SpellCheckingInspection")

import io.kotest.core.spec.style.StringSpec
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor as KotlinCompilerPluginCommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar as KotlinCompilerPluginComponentRegistrar

/**
 * @author 凛 (RinOrz)
 */
class IrTreePrinter : StringSpec({
  "suspend print" {
    compile(
      """
        import com.meowool.sweekt.*
        import kotlinx.coroutines.*

        interface A {
          @Suspend var name: String

          suspend fun setBackingName(name: String)
        }

        class AImpl : A {
          @Suspend override var name: String = "hello"
            set(value) = suspendSetter { field = value }
            get() {
              suspendGetter { field }
            }

          @JvmField
          var backingName = "ABC"

          override suspend fun setBackingName(name: String) = `-${'$'}suspendSetter` {
            backingName = name
          }

          suspend fun getBackingName() = backingName
        }

        suspend inline fun <R> `-${'$'}suspendGetter`(crossinline block: suspend () -> R): R = block()

        suspend inline fun `-${'$'}suspendSetter`(crossinline block: suspend () -> Unit): Unit = block()
      """.trimIndent(),
      compilerPlugins = listOf(ComponentRegistrar()),
      commandLineProcessors = listOf(CommandLineProcessor()),
      pluginOptions = emptyList()
    )
  }
}) {

  /**
   * @author 凛 (RinOrz)
   */
  class CommandLineProcessor : KotlinCompilerPluginCommandLineProcessor {
    override val pluginId: String = IrTreePrinter::class.java.name
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()
  }

  /**
   * @author 凛 (RinOrz)
   */
  class ComponentRegistrar : KotlinCompilerPluginComponentRegistrar {
    override fun registerProjectComponents(
      project: MockProject,
      configuration: CompilerConfiguration
    ) {
      IrGenerationExtension.registerExtension(
        project,
        object : IrGenerationExtension {
          override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
            moduleFragment.files.forEach {
              println("============================================================")
              println()
              println(it.dump(normalizeNames = true))
              println("============================================================")
            }
          }
        }
      )
    }
  }
}
