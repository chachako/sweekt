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
  "-Xjvm-default=enable",
  "-Xallow-jvm-ir-dependencies",
  "-Xskip-prerelease-check",
  "-Xopt-in=kotlin.RequiresOptIn",
  "-Xopt-in=kotlin.time.ExperimentalTime",
  "-Xuse-experimental=kotlin.Experimental",
  "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
  "-Xuse-experimental=kotlin.ExperimentalUnsignedTypes",
  "-Xuse-experimental=kotlin.contracts.ExperimentalContracts",
  "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
)