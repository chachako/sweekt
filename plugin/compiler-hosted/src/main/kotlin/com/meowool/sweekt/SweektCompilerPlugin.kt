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
package com.meowool.sweekt

import com.google.auto.service.AutoService
import com.intellij.mock.MockProject
import com.intellij.openapi.project.Project
import com.meowool.sweekt.info.InfoClassChecker
import com.meowool.sweekt.info.InfoClassSynthetic
import com.meowool.sweekt.info.InfoFunctionChecker
import com.meowool.sweekt.lazyinit.LazyInitChecker
import com.meowool.sweekt.lazyinit.LazyInitTransformer
import com.meowool.sweekt.lazyinit.ResetValueChecker
import com.meowool.sweekt.suspend.SuspendPropertyCallChecker
import com.meowool.sweekt.suspend.SuspendPropertyChecker
import com.meowool.toolkit.compiler_hosted.BuildConfig
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

/**
 * @author 凛 (RinOrz)
 */
@AutoService(CommandLineProcessor::class)
class SweektCommandLineProcessor : CommandLineProcessor {
  private val logging = CliOption(
    optionName = "isLogging",
    valueDescription = "true/false",
    description = "Used for compiler plugin testing",
    required = false
  )

  override val pluginId: String = BuildConfig.CompilerId
  override val pluginOptions: Collection<AbstractCliOption> = listOf(logging)

  override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
    when (option) {
      logging -> configuration.put(SweektConfigurationKeys.isLogging, value.toBoolean())
    }
  }
}

/**
 * @author 凛 (RinOrz)
 */
@AutoService(ComponentRegistrar::class)
class SweektComponentRegistrar : ComponentRegistrar {
  override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) =
    registerExtensions(project, configuration)

  /**
   * @author 凛 (RinOrz)
   */
  class StorageComponent : StorageComponentContainerContributor {
    override fun registerModuleComponents(
      container: StorageComponentContainer,
      platform: TargetPlatform,
      moduleDescriptor: ModuleDescriptor
    ) = arrayOf(
      InfoClassChecker, InfoFunctionChecker,
      LazyInitChecker, ResetValueChecker,
      SuspendPropertyChecker, SuspendPropertyCallChecker
    ).forEach(container::useInstance)
  }

  companion object {
    fun registerExtensions(project: Project, configuration: CompilerConfiguration) {
      StorageComponentContainerContributor.registerExtension(project, StorageComponent())
      SyntheticResolveExtension.registerExtension(project, InfoClassSynthetic())
      IrGenerationExtension.registerExtension(
        project,
        object : IrGenerationExtension {
          override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
            LazyInitTransformer(pluginContext, configuration).lower(moduleFragment)
//            InfoClassTransformer(pluginContext, configuration).lower(moduleFragment)
//            SuspendPropertyGeneration(pluginContext, configuration).lower(moduleFragment)
          }
        }
      )
    }
  }
}

/**
 * @author 凛 (RinOrz)
 */
object SweektConfigurationKeys {
  val isLogging: CompilerConfigurationKey<Boolean> = CompilerConfigurationKey.create("Whether to output logging")
}
