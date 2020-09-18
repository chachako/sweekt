@file:Suppress(
  "PackageDirectoryMismatch",
  "SpellCheckingInspection",
  "unused",
  "MemberVisibilityCanBePrivate"
)

/*
 * author: 凛
 * date: 2020/8/6 11:45 PM
 * github: https://github.com/oh-eRin
 * description: Mars 项目衍生出的公开依赖库
 */
object Mars {
  internal const val group = "com.mars.library"
  internal const val prefix = "$group:"

  const val conductor = prefix + "conductor:_"

  /* https://github.com/MarsPlanning/toolkit */
  val toolkit = Toolkit

  object Toolkit {
    private const val artifactPrefix = "$group:toolkit"

    /** Toolkit 库的核心 */
    val core = Core

    object Core {
      private const val artifactPrefix = "${Toolkit.artifactPrefix}-core"

      /** 仅适用于 android 的工具包 */
      const val android = "$artifactPrefix-android:_"

      /** 适用于所有 jvm 平台的工具包 */
      const val jvm = "$artifactPrefix-jvm:_"

      /** 简化 Gradle Plugin 的开发 */
      const val plugin = "com.mars.gradle.plugin:toolkit:_"
    }

    /**
     * Mars-UiKit （以 AndroidViews 衍生而来的一个强大的 UI 工具包）
     * 拥有与
     * [Compose](https://developer.android.com/jetpack/compose/),
     * [Flutter](https://flutter.dev/)
     * 类似的 UI 组合能力（由于 android 的限制，所以目前只是低配版）
     * 但核心并不会脱离于原生，较于声明式框架更适合熟悉原生 Android 开发的开发者
     * 拥有原生 Views 所具备的一切能力，当然也可以与其他 UI 框架混合使用
     */
    val ui = Ui

    object Ui {
      private const val artifactPrefix = "${Toolkit.artifactPrefix}-ui"

      /** UiKit 的核心 Gradle 插件（必要） */
      const val plugin = "$artifactPrefix-plugin:_"

      /** UiKit 的核心代码（必要） */
      const val runtime = "$artifactPrefix-runtime:_"

      /** UiKit 的动画扩展 */
      const val animation = "$artifactPrefix-animation:_"

      /**
       * UiKit 的第三方扩展，一些比较著名的第三方 View 的强化
       * 让布局或 View 更适合使用 UiKit 编写，可能是布局易用化也可能是复杂化...
       */
      val extension = Extension

      object Extension {
        private const val artifactPrefix = "${Ui.artifactPrefix}-extension"

        /* 增强 https://github.com/cashapp/contour */
        const val contour = "$artifactPrefix-contour:_"

        /* 增强 https://github.com/cashapp/contour */
        const val coil = "$artifactPrefix-coil:_"

        /* 增强 https://github.com/gyf-dev/ImmersionBar */
        const val immersionbar = "$artifactPrefix-immersionbar:_"
      }
    }

    /** 压缩工具包，适用于所有 jvm 平台 */
    const val zipper = "$artifactPrefix-zipper:_"

    /** 一些可以安全绕过 hidden-api 限制的 Stub */
    const val stubs = "$artifactPrefix-stubs:_"
  }
}

/** 解析依赖项 */
fun marsLibrary(artifact: String, version: String = "_") = "${Mars.prefix}$artifact:$version"