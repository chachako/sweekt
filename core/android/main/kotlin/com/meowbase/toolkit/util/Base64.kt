/*
 * Copyright (c) 2021. Rin Orz (凛)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * Github home page: https://github.com/RinOrz
 */

package com.meowbase.toolkit.util

import android.util.Base64
import androidx.annotation.IntDef
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset


/** 编码 */

fun ByteArray.encodeBase64(
  @Base64Mode flags: Int = Base64.DEFAULT,
  charset: Charset = Charsets.UTF_8
): String = String(Base64.encode(this, flags), charset)

fun ByteArray.encodeBase64Bytes(
  @Base64Mode flags: Int = Base64.DEFAULT
): ByteArray = Base64.encode(this, flags)

fun String.encodeBase64(
  @Base64Mode flags: Int = Base64.DEFAULT,
  charset: Charset = Charsets.UTF_8
): String = toByteArray().encodeBase64(flags, charset)

fun String.encodeBase64Bytes(
  @Base64Mode flags: Int = Base64.DEFAULT
): ByteArray = toByteArray().encodeBase64Bytes(flags)


/** 解码 */

fun ByteArray.decodeBase64(
  @Base64Mode flags: Int = Base64.DEFAULT,
  charset: Charset = Charsets.UTF_8
): String = String(Base64.decode(this, flags), charset)

fun ByteArray.decodeBase64Bytes(
  @Base64Mode flags: Int = Base64.DEFAULT
): ByteArray = Base64.decode(this, flags)

fun String.decodeBase64(
  @Base64Mode flags: Int = Base64.DEFAULT,
  charset: Charset = Charsets.UTF_8
): String = toByteArray().decodeBase64(flags, charset)

fun String.decodeBase64Bytes(
  @Base64Mode flags: Int = Base64.DEFAULT
): ByteArray = toByteArray().decodeBase64Bytes(flags)


/** Url 文本的编码与解码 */

fun String.encodeUrl(charset: String = "UTF-8"): String = URLEncoder.encode(this, charset)

fun String.decodeUrl(charset: String = "UTF-8"): String = URLDecoder.decode(this, charset)


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
