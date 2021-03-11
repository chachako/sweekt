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