@file:Suppress("NOTHING_TO_INLINE", "NO_ACTUAL_FOR_EXPECT")

package com.meowool.sweekt

/**
 * Returns `true` if this character is Chinese.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Char.isChinese(): Boolean = isChineseNotPunctuation() || isChinesePunctuation()

/**
 * Returns `true` if this character is Chinese and not Chinese punctuation.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
expect fun Char.isChineseNotPunctuation(): Boolean

/**
 * Returns `true` if this character is Chinese punctuation.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
expect fun Char.isChinesePunctuation(): Boolean

/**
 * Returns `true` if this character is english.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun Char.isEnglish(): Boolean = isEnglishNotPunctuation() || isEnglishPunctuation()

/**
 * Returns `true` if this character is english and not english punctuation.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
inline fun Char.isEnglishNotPunctuation(): Boolean = this in 'A'..'Z' || this in 'a'..'z'

/**
 * Returns `true` if this character is english punctuation.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun Char.isEnglishPunctuation(): Boolean = code.let {
  it in 32..47 || it in 58..64 || it in 91..96 || it in 123..126
}

/**
 * Returns `true` if this character is punctuation.
 *
 * @author 凛 (https://github.com/RinOrz)
 */
fun Char.isPunctuation(): Boolean = this.category.let {
  it == CharCategory.CONNECTOR_PUNCTUATION
    || it == CharCategory.DASH_PUNCTUATION
    || it == CharCategory.END_PUNCTUATION
    || it == CharCategory.FINAL_QUOTE_PUNCTUATION
    || it == CharCategory.INITIAL_QUOTE_PUNCTUATION
    || it == CharCategory.OTHER_PUNCTUATION
    || it == CharCategory.START_PUNCTUATION
} || isEnglishPunctuation() || isChinesePunctuation()