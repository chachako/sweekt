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

package com.meowbase.preference.kotpref.impl.base

import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import com.meowbase.preference.core.get
import com.meowbase.preference.core.put
import com.meowbase.preference.kotpref.KotprefModel

class BytesPref(
  val default: ByteArray,
  override val key: String?,
  override val commitByDefault: Boolean,
  override val getterImplProvider: ((thisRef: KotprefModel, preference: SharedPreferences) -> ByteArray)? = null,
  override val putterImplProvider: ((thisRef: KotprefModel, value: ByteArray, editor: SharedPreferences.Editor) -> SharedPreferences.Editor)? = null
) : AbstractPref<ByteArray>() {
  override fun get(thisRef: KotprefModel, preference: SharedPreferences): ByteArray =
    preference.get(key, default)

  override fun put(
    thisRef: KotprefModel,
    value: ByteArray,
    editor: SharedPreferences.Editor
  ): SharedPreferences.Editor = editor.put(key, value)
}
