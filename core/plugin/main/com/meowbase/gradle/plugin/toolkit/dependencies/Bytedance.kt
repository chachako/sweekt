@file:Suppress("PackageDirectoryMismatch", "SpellCheckingInspection", "unused")

/*
 * author: 凛
 * date: 2020/8/12 6:49 PM
 * github: https://github.com/RinOrz
 * description: 字节跳动的依赖管理, see https://github.com/bytedance/
 */
object Bytedance {
  val byteX = ByteX

  object ByteX {
    private const val artifactPrefix = "com.bytedance.android.byteX"
    const val common = "$artifactPrefix:common:_"
    const val basePlugin = "$artifactPrefix:base-plugin:_"
    const val transformEngine = "$artifactPrefix:TransformEngine:_"
    const val gradleEnvApi = "$artifactPrefix:GradleEnvApi:_"
    const val gradleToolKit = "$artifactPrefix:GradleToolKit:_"
    const val pluginConfigAnnotation = "$artifactPrefix:PluginConfigAnnotation:_"
    const val pluginConfigProcessor = "$artifactPrefix:PluginConfigProcessor:_"
  }
}