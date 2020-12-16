@file:Suppress("PackageDirectoryMismatch", "SpellCheckingInspection", "unused", "ClassName")

/*
 * author: 凛
 * date: 2020/9/3 7:05 下午
 * github: https://github.com/RinOrz
 * description: 饿了么团队公开的依赖管理, see https://github.com/eleme
 */
object Eleme {
  val dna = Dna

  object Dna {
    const val annotations = "me.ele:dna-annotations:_"
    const val compiler = "me.ele:dna-compiler:_"
  }
}