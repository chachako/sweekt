package com.mars.toolkit

import java.util.regex.Pattern

/**
 * 字符串的判断符
 * 根据不同的操作符来决定用什么方法判断字符串
 *
 * @see String.contains
 * @see String.startsWith
 * @see String.endsWith
 * @see String.equals
 */
enum class StringJudge { Contains, StartsWith, EndsWith, Equals }

/**
 * 字符串是否包含纯中文（不包含中文符号）
 */
fun String.isContainPureChinese(): Boolean {
    val p = Pattern.compile("[\u4E00-\u9FA5]")
    val m = p.matcher(this)
    return m.find()
}

/**
 * 字符串是否包含中文（含中文符号）
 */
fun String.isContainChinese(): Boolean {
    val p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]")
    val m = p.matcher(this)
    return m.find()
}

/**
 * 字符串是否为纯中文（不包含中文符号）
 */
fun String.isPureChinese(): Boolean {
    val p = Pattern.compile("[\u4E00-\u9FA5]")
    val m = p.matcher(this)
    return m.matches()
}

/**
 * 字符串是否为中文（含中文符号）
 */
fun String.isChinese(): Boolean {
    val p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]")
    val m = p.matcher(this)
    return m.matches()
}

/**
 * 字符串是否包含英文
 */
fun String.isContainEnglish(): Boolean {
    val p = Pattern.compile(".*[a-zA-z].*")
    val m = p.matcher(this)
    return m.find()
}

/**
 * 字符串是否为英文
 */
fun String.isEnglish(): Boolean {
    val p = Pattern.compile(".*[a-zA-z].*")
    val m = p.matcher(this)
    return m.matches()
}

/**
 * 封装了一个判空字符串的中缀方法
 * @sample "" or "第一个字符串为空或者为 null 则使用我"
 */
infix fun String?.or(or: String) = if (isNullOrEmpty()) or else this!!