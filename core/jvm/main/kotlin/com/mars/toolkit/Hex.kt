package com.mars.toolkit

import java.math.BigInteger

/* copy from: https://github.com/lulululbj/AndroidUtilCodeKTX */

private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun String.to16HexString(): String = BigInteger(this).toString(16)

fun String.toHexString() = toByteArray().run {
    val result = CharArray(size shl 1)
    var index = 0
    for (b in this) {
        result[index++] = HEX_DIGITS[b.toInt().shr(4) and 0xf]
        result[index++] = HEX_DIGITS[b.toInt() and 0xf]
    }
    String(result)
}

fun ByteArray.toHexString(): String {
    val result = CharArray(size shl 1)
    var index = 0
    for (b in this) {
        result[index++] = HEX_DIGITS[b.toInt().shr(4) and 0xf]
        result[index++] = HEX_DIGITS[b.toInt() and 0xf]
    }
    return String(result)
}