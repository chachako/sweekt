@file:Suppress("SpellCheckingInspection", "PackageDirectoryMismatch")

internal const val PLUGIN_DOKKA = "org.jetbrains.dokka"

/**
 * 一些通用的 Kotlin 编译器参数
 * WARN: 一些抑制可能不是你想要的！三思后使用
 * 有些版本可能在 gradle 设置抑制无效，你可能还需要在
 * Preferences | Other Settings | Kotlin Compiler | Additional command line parameters
 * 中定义
 * -Xinline-classes
 * -Xjvm-default=enable
 * -Xskip-prerelease-check
 * -Xallow-jvm-ir-dependencies
 * -Xopt-in=kotlin.RequiresOptIn
 * -Xuse-experimental=kotlin.Experimental
 * -Xuse-experimental=kotlin.ExperimentalStdlibApi
 * -Xuse-experimental=kotlin.ExperimentalUnsignedTypes
 * -Xuse-experimental=kotlin.contracts.ExperimentalContracts
 * -Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi
 */
val commonSuppressionArgs
  get() = listOf(
    "-Xinline-classes",
    "-Xjvm-default=enable",
    "-Xskip-prerelease-check",
    "-Xallow-jvm-ir-dependencies",
    "-Xopt-in=kotlin.RequiresOptIn",
    "-Xuse-experimental=kotlin.Experimental",
    "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
    "-Xuse-experimental=kotlin.ExperimentalUnsignedTypes",
    "-Xuse-experimental=kotlin.contracts.ExperimentalContracts",
    "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
  )