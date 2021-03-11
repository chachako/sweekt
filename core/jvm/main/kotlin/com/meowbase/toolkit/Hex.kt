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

package com.meowbase.toolkit

import java.math.BigInteger

/* copy from: https://github.com/lulululbj/AndroidUtilCodeKTX */

private val HEX_DIGITS = charArrayOf(
  '0',
  '1',
  '2',
  '3',
  '4',
  '5',
  '6',
  '7',
  '8',
  '9',
  'a',
  'b',
  'c',
  'd',
  'e',
  'f'
)

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