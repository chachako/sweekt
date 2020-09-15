@file:Suppress("PackageDirectoryMismatch", "SpellCheckingInspection", "unused")

/*
 * author: 凛
 * date: 2020/8/12 6:49 PM
 * github: https://github.com/oh-eRin
 * description: Android 的一些公共依赖
 */
object Android {
  val tools = Tools

  object Tools {
    const val r8 = "com.android.tools:r8:_"
  }

  val build = Build

  object Build {
    const val apksig = "com.android.tools.build:apksig:_"
  }
}