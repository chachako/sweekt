@file:Suppress("PackageDirectoryMismatch", "DefaultLocale")

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

/**
 * 注册一个 Plugin
 * @param id 插件的 ID，如 "com.mars.gradle.plugin.toolkit"
 * @param implementationClass 插件的实现类, 如 "com.mars.gradle.plugin.toolkit.ToolkitPlugin"
 * @param pluginName 插件名称，如 "toolkit"
 */
fun Project.createPlugin(
  id: String,
  implementationClass: String,
  pluginName: String = project.name
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
fun Project.createPlugin(pluginName: String = project.name) = createPlugin(
  pluginName = pluginName,
  id = "$group.$pluginName",
  implementationClass = "$group.$pluginName.${pluginName.capitalize()}Plugin"
)

@InternalMarsProjectApi
fun Project.createMarsPlugin(pluginName: String = project.name) = createPlugin(
  pluginName = pluginName,
  id = "com.mars.gradle.plugin.$pluginName",
  implementationClass = "com.mars.gradle.plugin.$pluginName.${pluginName.capitalize()}Plugin"
)