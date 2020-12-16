package com.meowbase.gradle.plugin.preference.data

/*
 * author: 凛
 * date: 2020/9/19 下午11:44
 * github: https://github.com/RinOrz
 * description: 对名称进行模糊的参数
 */
class VagueOptions {
  /**
   * 定义可被混淆的委托
   * 这些委托属性的绑定方法的参数中必须拥有一个 key: String 参数
   * 用于定义混淆后的 preference 的 key 名称
   *
   * ```
   * fun boolean(
   *   default: Boolean = false,
   *   key: String? = null,
   *   commitByDefault: Boolean = commitAllProperties
   * ): ReadWriteProperty<*, *> { ... }
   *
   * var isNight by boolean(false)
   * ```
   * @default 默认支持的委托方法名（在 :runtime:base - com.meowbase.preference.kotpref 包中可以找到）
   */
  val delegation: MutableList<String> = mutableListOf(
    "string",
    "stringNullable",
    "int",
    "long",
    "float",
    "double",
    "bytes",
    "bytesNullable",
    "boolean",
    "stringSet",
    "enumValue",
    "enumValueNullable",
    "enumOrdinal",
  )

  /**
   * 定义混淆字典
   * 在混淆时，每个字母无论大小写都会替换为 Map 中的 value
   */
  var dictionary: MutableMap<Char, Char> = hashMapOf(
    'a' to 'ִ',
    'b' to 'ׁ',
    'c' to 'ׅ',
    'd' to 'ܼ',
    'e' to '࡛',
    'f' to 'ٖ',
    'g' to '݈',
    'h' to '˙',
    'i' to '໋',
    'j' to '֒',
    'k' to '݁',
    'l' to 'ؚ',
    'm' to '՝',
    'n' to '՛',
    'o' to '՟',
    'p' to 'ܿ',
    'q' to 'ּ',
    'r' to 'វ',
    's' to '٬',
    't' to '݇',
    'u' to '༹',
    'v' to '་',
    'w' to 'ܳ',
    'x' to '݅',
    'y' to 'ࠫ',
    'z' to 'ࠣ',
  )
}