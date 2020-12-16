package com.meowbase.toolkit.util

import android.util.Base64
import androidx.annotation.IntDef
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset



/** 编码 */

fun ByteArray.base64Encode(
  @Base64Mode flags: Int = Base64.DEFAULT,
  charset: Charset = Charsets.US_ASCII
): String = String(Base64.encode(this, flags), charset)

fun ByteArray.base64EncodeBytes(
  @Base64Mode flags: Int = Base64.DEFAULT
): ByteArray = Base64.encode(this, flags)

fun String.base64Encode(
  @Base64Mode flags: Int = Base64.DEFAULT,
  charset: Charset = Charsets.US_ASCII
): String = toByteArray().base64Encode(flags, charset)

fun String.base64EncodeBytes(
  @Base64Mode flags: Int = Base64.DEFAULT
): ByteArray = toByteArray().base64EncodeBytes(flags)


/** 解码 */

fun ByteArray.base64Decode(
  @Base64Mode flags: Int = Base64.DEFAULT,
  charset: Charset = Charsets.US_ASCII
): String = String(Base64.decode(this, flags), charset)

fun ByteArray.base64DecodeBytes(
  @Base64Mode flags: Int = Base64.DEFAULT
): ByteArray = Base64.decode(this, flags)

fun String.base64Decode(
  @Base64Mode flags: Int = Base64.DEFAULT,
  charset: Charset = Charsets.US_ASCII
): String = toByteArray().base64Decode(flags, charset)

fun String.base64DecodeBytes(
  @Base64Mode flags: Int = Base64.DEFAULT
): ByteArray = toByteArray().base64DecodeBytes(flags)


/** Url 文本的编码与解码 */

fun String.urlEncode(charset: String = "UTF-8"): String = URLEncoder.encode(this, charset)

fun String.urlDecode(charset: String = "UTF-8"): String = URLDecoder.decode(this, charset)


@IntDef(
  Base64.DEFAULT,
  Base64.NO_PADDING,
  Base64.NO_WRAP,
  Base64.CRLF,
  Base64.URL_SAFE,
  Base64.NO_CLOSE
)
@Retention(AnnotationRetention.SOURCE)
annotation class Base64Mode
