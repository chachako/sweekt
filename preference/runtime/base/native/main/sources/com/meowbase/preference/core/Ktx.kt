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

package com.meowbase.preference.core

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import java.lang.Double.doubleToRawLongBits
import java.lang.Double.longBitsToDouble

/**
 * Extension to choose between {@link SharedPreferences.Editor.commit} and {@link SharedPreferences.Editor.apply}
 * @param synchronous save to sharedPref file instantly
 */
fun Editor.execute(synchronous: Boolean) {
  if (synchronous) commit() else apply()
}


/**
 * Value transformers
 */

fun SharedPreferences.get(key: String?, default: Double) =
  longBitsToDouble(getLong(key, doubleToRawLongBits(default)))

fun Editor.put(key: String?, value: Double): Editor =
  putLong(key, doubleToRawLongBits(value))

fun SharedPreferences.get(key: String?, default: ByteArray?) =
  requireNotNull(getString(key, default?.let {
    String(it, Charsets.ISO_8859_1)
  })).toByteArray(Charsets.ISO_8859_1)

fun Editor.put(key: String?, value: ByteArray?): Editor =
  putString(key, value?.let { String(it, Charsets.ISO_8859_1) })