@file:Suppress("unused", "PackageDirectoryMismatch")

import com.meowbase.toolkit.forEach
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.findByType
import java.io.File

/**
 * 导入共享的通用依赖
 *
 * ```
 * dependencies {
 *   importSharedDependencies()
 *   // 当然也可以设置其他依赖
 *   implementationOf(
 *     "com.google.code.gson:gson:_",
 *     Square.moshi
 *   )
 * }
 * ```
 *
 * @see ToolkitOptions.sharedDependencies
 * @throws IllegalStateException 必须先定义可以公用的依赖
 */
fun Project.importSharedDependencies(dependencyHandler: DependencyHandler) =
  importSharedDependencies(dependencyHandler, "_main")

/**
 * 导入共享的区域的通用依赖
 *
 * ```
 * dependencies {
 *   importSharedDependencies(this, scope = "core-app")
 *   // 当然也可以设置其他依赖
 *   implementationOf(
 *     "com.google.code.gson:gson:_",
 *     Square.moshi
 *   )
 * }
 * ```
 *
 * @param scope 决定需要导入哪个区域分享的依赖
 * @see ToolkitOptions.sharedDependencies
 * @throws IllegalStateException 必须先定义可以公用的依赖
 */
fun Project.importSharedDependencies(dependencyHandler: DependencyHandler, scope: String) {
  rootProject.extensions.findByType<ToolkitOptions>()
    ?.sharedDependencies
    ?.get(scope)
    ?.invoke(dependencyHandler)
    ?: error(
      """
      使用 importSharedDependencies 前必须要在根目录的 settings.gradle.kts 中定义： 
      com.meowbase.gradle.plugin.toolkit.setupToolkit {
        // 定义想要让多个模块于某个区域共用的依赖
        shareDependencies(scope = $scope) {
          ... 
        }
        ...
      }
      """.trimIndent()
    )
}

/**
 * 导入任意项目
 * NOTE: 在不想被导入的文件夹中创建一个名为 ".dontimport" 的任意文件则可以让 [importProject] 强制跳过文件夹
 * @param dir 项目文件夹
 * @param path 项目路径（并非为文件夹路径，而是在 Gradle 中的路径）
 */
@Suppress("SpellCheckingInspection")
fun Settings.importProject(dir: File, path: String = ":${dir.name}") {
  if (dir.resolve("build.gradle.kts").run { parent != rootDir.absolutePath && exists() } ||
    dir.resolve("build.gradle").run { parent != rootDir.absolutePath && exists() }) {
    include(path)
    return
  }
  dir.listFiles()?.forEach {
    if (it.isDirectory &&
      !File(it, ".dontimport").exists() &&
      it.name[0] != '.' &&
      rootDir.absolutePath != it.absolutePath
    ) {
      importProject(it, "$path:${it.name}")
    }
  }
}

/**
 * 将项目 libs 目录下的所有 jar 文件作为依赖导入
 * 以 [configurationName] 形式导入
 */
fun Project.importLibs(
  configurationName: String,
  dependencyHandler: DependencyHandler,
  name: String = "libs"
) {
  dependencyHandler.add(
    configurationName,
    fileTree(mapOf("dir" to name, "include" to listOf("*.jar")))
  )
}

/**
 * 以 implementation 形式导入项目 libs 目录
 * @see importLibs
 */
fun Project.implementationLibs(dependencyHandler: DependencyHandler, name: String = "libs") =
  importLibs("implementation", dependencyHandler, name)

/**
 * 以 api 形式导入项目 libs 目录
 * @see importLibs
 */
fun Project.apiLibs(dependencyHandler: DependencyHandler, name: String = "libs") =
  importLibs("api", dependencyHandler, name)

/**
 * 以 compileOnly 形式导入项目 libs 目录
 * @see importLibs
 */
fun Project.compileOnlyLibs(dependencyHandler: DependencyHandler, name: String = "libs") =
  importLibs("compileOnly", dependencyHandler, name)


/**
 * 将给定目录下的所有 jar 文件作为依赖导入
 * 以 [configurationName] 形式导入
 */
fun Project.importJars(
  configurationName: String,
  dependencyHandler: DependencyHandler,
  fromDir: String
) {
  val implemented = mutableListOf<String>()
  File(fromDir).forEach(recursively = true) {
    if (it.name.endsWith(".jar") && !implemented.contains(it.parent)) {
      dependencyHandler.add(
        configurationName,
        fileTree(mapOf("dir" to it.parentFile.absolutePath, "include" to listOf("*.jar")))
      )
      implemented.add(it.parent)
    }
  }
}

/**
 * 以 implementation 形式导入 [fromDir] 目录
 * @see importJars
 */
fun Project.implementationJars(dependencyHandler: DependencyHandler, fromDir: String) =
  importJars("implementation", dependencyHandler, fromDir)

/**
 * 以 api 形式导入 [fromDir] 目录
 * @see importJars
 */
fun Project.apiJars(dependencyHandler: DependencyHandler, fromDir: String) =
  importJars("api", dependencyHandler, fromDir)

/**
 * 以 compileOnly 形式导入 [fromDir] 目录
 * @see importJars
 */
fun Project.compileOnlyJars(dependencyHandler: DependencyHandler, fromDir: String) =
  importJars("compileOnly", dependencyHandler, fromDir)


/**
 * 导入 FlutterStub
 * 用处是可以在任意 android-library 中调用 flutter 包，且不会打包进 aar
 */
@InternalMeowbaseApi
fun Project.importFlutterStub(dependencyHandler: DependencyHandler) = dependencyHandler.add(
  "compileOnly",
  fileTree(
    mapOf(
      "dir" to "$meowbaseInternalLocalPath.classpath",
      "include" to listOf("flutter_embedding.jar")
    )
  )
)