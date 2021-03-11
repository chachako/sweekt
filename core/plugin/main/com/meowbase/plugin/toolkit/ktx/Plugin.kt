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

@file:Suppress("PackageDirectoryMismatch", "DefaultLocale")

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

/**
 * 注册一个 Plugin
 * @param id 插件的 ID，如 "com.meowbase.plugin.toolkit"
 * @param pluginName 插件名称，如 "toolkit"
 * @param implementationClass 插件的实现类, 如 "com.meowbase.plugin.toolkit.ToolkitPlugin"
 */
fun Project.createPlugin(
  id: String,
  pluginName: String = project.name,
  implementationClass: String = "$group.$pluginName.${pluginName.capitalize()}Plugin"
) {
  extensions.getByType<GradlePluginDevelopmentExtension>().plugins.create(pluginName) {
    this.id = id
    this.implementationClass = implementationClass
  }
}

/**
 * 快速注册一个 Plugin
 * @warn 注意插件实现类的名称必须以 [pluginName] 开头，且类路径必须是 [Project.getGroup] + [pluginName]
 */
fun Project.createPlugin(
  pluginName: String = project.name,
  implementationClass: String = "$group.$pluginName.${pluginName.capitalize()}Plugin"
) = createPlugin(
  id = "$group.$pluginName",
  pluginName = pluginName,
  implementationClass = implementationClass
)

@InternalMeowbaseApi
fun Project.createMeowbasePlugin(
  pluginName: String = project.name,
  id: String = "com.meowbase.$pluginName",
  implementationClass: String = "com.meowbase.plugin.$pluginName.${pluginName.capitalize()}Plugin"
) = createPlugin(
  pluginName = pluginName,
  id = id,
  implementationClass = implementationClass
)