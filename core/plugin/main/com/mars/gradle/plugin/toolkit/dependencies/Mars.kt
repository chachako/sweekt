@file:Suppress(
  "PackageDirectoryMismatch",
  "SpellCheckingInspection",
  "unused",
  "MemberVisibilityCanBePrivate"
)

import dependencies.DependencyNotationAndGroup

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
     *
     * NOTE: UiKit 的核心代码（必要）
     */
    val ui = Ui

    object Ui: MarsLibrary("$artifactPrefix-ui") {

      /** UiKit 的核心 Gradle 插件（必要） */
      val plugin = "$artifactPrefix-plugin:_"

      /** UiKit 的动画（可选） */
      val animation = "$artifactPrefix-animation:_"

      /** UiKit 的屏幕组件，用于取代 Activity 以解锁更多可能性（可选）*/
      val screen = "$artifactPrefix-screen:_"

      /**
       * UiKit 的第三方扩展，一些比较著名的第三方 View 的强化
       * 让布局或 View 更适合使用 UiKit 编写，可能是布局易用化也可能是复杂化...
       */
      val extension = Extension

      object Extension {
        private val artifactPrefix = "${Ui.artifactPrefix}-extension"

        /* 提供 https://github.com/cashapp/contour 在 UiKit 中的增强 */
        val contour = "$artifactPrefix-contour:_"

        /* 提供 https://github.com/coil-kt 在 UiKit 中的增强 */
        val coil = "$artifactPrefix-coil:_"

        /* 提供 https://github.com/donkingliang/ConsecutiveScroller 在 UiKit 中的增强 */
        val consecutiveScroller = "$artifactPrefix-consecutivescroller:_"
      }
    }


    /* Preference 核心运行时代码 (默认为 SharedPreference 模型)，实现灵感来源于 https://github.com/chibatching/Kotpref（必须）*/
    val preference = Preference

    object Preference: MarsLibrary("$artifactPrefix-preference") {
      /** 为所有 Preference 模型提供混淆支持（可选）*/
      val plugin = "$artifactPrefix-plugin:_"

      /** 对 https://github.com/Tencent/MMKV 提供 Preference 模型的扩展支持 */
      val mmkv = "$artifactPrefix-mmkv:_"
    }


    /** 为 KotlinCompiler 增加易用的方法（PSI 解析 Kotlin 源文件等） */
    const val kotlinCompiler = "$artifactPrefix-ktcompiler:_"

    /** 一些可以安全绕过 hidden-api 限制的 Stub */
    const val stubs = "$artifactPrefix-stubs:_"
  }

  abstract class MarsLibrary(artifact: String): DependencyNotationAndGroup(group = group, name = artifact)
}

/** 解析依赖项 */
fun marsLibrary(artifact: String, version: String = "_") = "${Mars.prefix}$artifact:$version"