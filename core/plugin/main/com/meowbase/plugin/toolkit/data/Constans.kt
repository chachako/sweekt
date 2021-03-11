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

@file:Suppress("SpellCheckingInspection", "PackageDirectoryMismatch")

internal const val PluginDokka = "org.jetbrains.dokka"

internal const val BestKotlinVersion = "1.4"

internal const val BestJvmTargetVersion = "1.8"

/**
 * 一些通用的 Kotlin 编译器参数
 * WARN: 一些抑制可能不是你想要的！三思后使用
 */
val CommonSuppressionArgs = arrayOf(
  "-progressive",
  "-Xinline-classes",
  "-Xjvm-default=compatibility",
  "-Xskip-prerelease-check",
  "-Xopt-in=kotlin.RequiresOptIn",
  "-Xopt-in=kotlin.time.ExperimentalTime",
  "-Xopt-in=kotlin.Experimental",
  "-Xopt-in=kotlin.ExperimentalStdlibApi",
  "-Xopt-in=kotlin.ExperimentalUnsignedTypes",
  "-Xopt-in=kotlin.contracts.ExperimentalContracts",
  "-Xopt-in=kotlin.experimental.ExperimentalTypeInference",
  "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
)