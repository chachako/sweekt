@file:Suppress("SpellCheckingInspection", "PackageDirectoryMismatch")

internal const val PLUGIN_DOKKA = "org.jetbrains.dokka"

/**
 * 一些通用的 Kotlin 编译器参数
 * WARN: 一些抑制可能不是你想要的！三思后使用
 */
val commonSuppressionArgs = mutableListOf(
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