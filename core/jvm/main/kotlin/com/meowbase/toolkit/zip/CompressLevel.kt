package com.meowbase.toolkit.zip

/*
 * author: 凛
 * date: 2020/9/10 下午9:59
 * github: https://github.com/RinOrz
 * description: 定义压缩等级，等级越高且压缩越强但速度也会随之降低
 */
data class CompressLevel(val level: Int) {
  companion object {
    /** 正常压缩 */
    val Default = CompressLevel(-1)

    /** 不压缩 */
    val None = CompressLevel(0)

    /** 极速压缩 */
    val Fastest = CompressLevel(1)

    /** 最佳压缩 */
    val Best = CompressLevel(9)
  }
}