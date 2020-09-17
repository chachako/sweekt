@file:Suppress("PackageDirectoryMismatch", "SpellCheckingInspection", "unused")

/*
 * author: 凛
 * date: 2020/8/12 6:49 PM
 * github: https://github.com/oh-eRin
 * description: 腾讯大厂公开的依赖管理, see https://github.com/Tencent/
 */
object Tencent {
  val mmkv = MMKV

  object MMKV {
    const val default = "com.tencent:mmkv:_"
    const val static = "com.tencent:mmkv-static:_"
  }

  const val location = "com.tencent.map.geolocation:TencentLocationSdk-openplatform:_"
}