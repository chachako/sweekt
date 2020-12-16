@file:Suppress("PackageDirectoryMismatch", "SpellCheckingInspection", "unused")

/*
 * author: 凛
 * date: 2020/8/12 6:49 PM
 * github: https://github.com/RinOrz
 * description: 其他一些依赖补全
 */
object Deps {
  /* https://github.com/cashapp/contour */
  const val contour = "app.cash.contour:contour:_"

  /* https://github.com/google/gson */
  const val gson = "com.google.code.gson:gson:_"

  /* https://github.com/SalomonBrys/Kotson */
  const val kotson = "com.github.salomonbrys.kotson:kotson:_"

  /* https://github.com/airbnb/lottie-android */
  const val lottie = "com.airbnb.android:lottie:_"

  /* https://jsoup.org/ */
  const val jsoup = "org.jsoup:jsoup:_"

  /* https://github.com/JesusFreke/smali */
  const val smali = "org.smali:smali:_"
  const val baksmali = "org.smali:baksmali:_"
  const val dexlib2 = "org.smali:dexlib2:_"

  /* https://github.com/DexPatcher/multidexlib2 */
  const val multidexlib2 = "com.github.lanchon.dexpatcher:multidexlib2:_"

  const val liveEventBusX = "com.jeremyliao:live-event-bus-x:_"
  const val zipZt = "org.zeroturnaround:zt-zip:_"
  const val javassist = "org.javassist:javassist:_"
  const val overscrollDecor = "me.everything:overscroll-decor-android:_"
  const val barista = "com.schibsted.spain:barista:_"
  const val kakao = "com.agoda.kakao:kakao:_"
  const val apkSig = "com.android.tools.build:apksig:_"
  const val transitionEverywhere = "com.andkulikov:transitionseverywhere:_"

  const val transitionX = "in.arunkumarsampath:transition-x:_"

  /* https://github.com/hackware1993/MagicIndicator */
  const val magicIndicator = "com.github.hackware1993:MagicIndicator:_"

  const val expandableLayout = "net.cachapa.expandablelayout:expandablelayout:_"
  const val switcher = "com.bitvale:switcher:_"
  const val kdTabLayout = "com.github.XuQK:KDTabLayout:_"
  const val apkParser = "net.dongliu:apk-parser:_"
  const val snakeYaml = "org.yaml:snakeyaml:_"
  const val conscryptAndroid = "org.conscrypt:conscrypt-android:_"

  const val logbackClassic = "ch.qos.logback:logback-classic:_"
  const val mysqlJavaConnector = "mysql:mysql-connector-java:_"

  const val kotlinWriter = "com.chrynan.kotlin-writer:kotlin-writer:_"
  const val kotlinPoetDSL = "nl.devhaan:KotlinPoetDSL:_"
  const val kotlinpoetKtx = "com.hendraanggrian:kotlinpoet-ktx:_"

  const val freeReflection = "me.weishu:free_reflection:_"

  const val ksp = "com.google.devtools.ksp:symbol-processing:_"
  const val kspApi = "com.google.devtools.ksp:symbol-processing-api:_"

  val incap = Incap

  object Incap {
    const val core = "net.ltgt.gradle.incap:incap:_"
    const val processor = "net.ltgt.gradle.incap:incap-processor:_"
  }

  val assent = Assent

  object Assent {
    const val core = "com.afollestad.assent:core:_"
    const val rationales = "com.afollestad.assent:rationales:_"
    const val coroutines = "com.afollestad.assent:coroutines:_"
  }

  val dexMaker = DexMaker

  object DexMaker {
    const val mockito = "com.linkedin.dexmaker:dexmaker-mockito:_"
    const val core = "com.linkedin.dexmaker:dexmaker:_"
  }

  val amap = Amap

  object Amap {
    const val location = "com.amap.api:location:_"
    const val search = "com.amap.api:search:_"
  }

  val smartRefreshLayout = SmartRefreshLayout

  object SmartRefreshLayout {
    const val kernel = "com.scwang.smart:refresh-layout-kernel:_" //核心必须依赖
    const val classicsHeader = "com.scwang.smart:refresh-header-classics:_" //经典刷新头
    const val radar = "com.scwang.smart:refresh-header-radar:_" //雷达刷新头
    const val falsify = "com.scwang.smart:refresh-header-falsify:_" //虚拟刷新头
    const val material = "com.scwang.smart:refresh-header-material:_" //谷歌刷新头
    const val twoLevel = "com.scwang.smart:refresh-header-two-level:_" //二级刷新头
    const val ball = "com.scwang.smart:refresh-footer-ball:_" //球脉冲加载
    const val classicsFooter = "com.scwang.smart:refresh-footer-classics:_" //经典加载
  }

  val asm = Asm

  object Asm {
    const val base = "org.ow2.asm:asm:_"
    const val commons = "org.ow2.asm:asm-commons:_"
    const val tree = "org.ow2.asm:asm-tree:_"
    const val util = "org.ow2.asm:asm-util:_"
  }

  val methodTraceMan = MethodTraceMan

  object MethodTraceMan {
    const val debug = "com.github.zhengcx:MethodTraceMan:1.0.7"
    const val release = "com.github.zhengcx:MethodTraceMan:1.0.5-noop"
  }

  /**
   * https://github.com/raphw/byte-buddy
   */
  val byteBuddy = ByteBuddy

  object ByteBuddy {
    const val all = "net.bytebuddy:byte-buddy:_"
    const val parent = "net.bytebuddy:byte-buddy-parent:_"
    const val android = "net.bytebuddy:byte-buddy-android:_"
    const val agent = "net.bytebuddy:byte-buddy-agent:_"
    const val benchmark = "net.bytebuddy:byte-buddy-benchmark:_"
    const val dep = "net.bytebuddy:byte-buddy-dep:_"
  }

  /* https://github.com/gyf-dev/ImmersionBar */
  val immersionBar = ImmersionBar

  object ImmersionBar {
    const val base = "com.gyf.immersionbar:immersionbar:_"
    const val ktx = "com.gyf.immersionbar:immersionbar-ktx:_"
  }

  val uMeng = UMeng

  object UMeng {
    /* https://github.com/umeng/MultiFunctionAndroidDemo */
    const val common = "com.umeng.umsdk:common:_"
  }

  val kaspresso = Kaspresso

  object Kaspresso {
    const val base = "com.kaspersky.android-components:kaspresso:_"
    const val uiautomatorDsl = "com.kaspersky.android-components:uiautomator-dsl:_"
  }

  val koin = Koin

  object Koin {
    val androidX = AndroidX

    object AndroidX {
      // Koin AndroidX Scope features
      const val scope = "org.koin:koin-androidx-scope:_"

      // Koin AndroidX ViewModel features
      const val viewmodel = "org.koin:koin-androidx-viewmodel:_"

      // Koin AndroidX Fragment features
      const val fragment = "org.koin:koin-androidx-fragment:_"

      // Koin AndroidX Experimental features
      const val ext = "org.koin:koin-androidx-ext:_"
    }

    val android = Android

    object Android {
      // Koin for Android
      const val core = "org.koin:koin-android:_"

      // or Koin for Lifecycle scoping
      const val scope = "org.koin:koin-android-scope:_"

      // or Koin for Android Architecture ViewModel
      const val viewmodel = "org.koin:koin-android-viewmodel:_"
    }

    // Koin for Kotlin
    const val core = "org.koin:koin-core:_"

    // Koin extended & experimental features
    const val coreExt = "org.koin:koin-core-ext:_"

    // Koin for Unit tests
    const val test = "org.koin:koin-test:_"

    // Koin for Java developers
    const val java = "org.koin:koin-java:_"

    // Koin for Ktor Kotlin
    const val ktor = "org.koin:koin-ktor:_"
  }

  val ktorm = Ktorm

  object Ktorm {
    const val core = "me.liuwj.ktorm:ktorm-core:_"
    const val supportMysql = "me.liuwj.ktorm:ktorm-support-mysql:_"
  }
}