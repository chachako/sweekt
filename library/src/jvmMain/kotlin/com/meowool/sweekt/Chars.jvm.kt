@file:Suppress("NOTHING_TO_INLINE")

package com.meowool.sweekt

/**
 * Returns `true` if this character is Chinese and not Chinese punctuation.
 *
 * @author 凛 (RinOrz)
 */
actual inline fun Char.isChineseNotPunctuation(): Boolean = toUnicodeScript() == Character.UnicodeScript.HAN

/**
 * Returns `true` if this character is Chinese punctuation.
 *
 * @author 凛 (RinOrz)
 */
actual fun Char.isChinesePunctuation(): Boolean = toUnicodeBlock().let {
  it === Character.UnicodeBlock.GENERAL_PUNCTUATION
    || it === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
    || it === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
    || it === Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
    || it === Character.UnicodeBlock.VERTICAL_FORMS
}

/**
 * Convert [Char] to [Character.UnicodeBlock].
 *
 * @author 凛 (RinOrz)
 */
inline fun Char.toUnicodeBlockOrNull(): Character.UnicodeBlock? = Character.UnicodeBlock.of(this.code)

/**
 * Convert [Char] to [Character.UnicodeBlock].
 *
 * @author 凛 (RinOrz)
 */
fun Char.toUnicodeBlock(): Character.UnicodeBlock = toUnicodeBlockOrNull()
  ?: error("The character `$this` is not a member of a defined unicode block.")

/**
 * Convert [Char] to [Character.UnicodeScript].
 *
 * @author 凛 (RinOrz)
 */
inline fun Char.toUnicodeScript(): Character.UnicodeScript = Character.UnicodeScript.of(this.code)