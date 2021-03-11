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

package com.meowbase.preference.mmkv

import com.meowbase.preference.kotpref.KotprefModel
import android.content.SharedPreferences.Editor
import com.tencent.mmkv.MMKV
import com.meowbase.preference.kotpref.impl.base.AbstractPref
import com.meowbase.preference.kotpref.impl.base.BytesNullablePref
import com.meowbase.preference.kotpref.impl.base.BytesPref
import com.meowbase.preference.kotpref.impl.base.DoublePref

/**
 * Definition the model using [MMKV]
 * @see MmkvProvider
 */
open class MmkvModel(
  /** Preference file name */
  name: String? = null,
  /** Preference single/multi process mode */
  processMode: Int = MMKV.SINGLE_PROCESS_MODE,
  /** Preference file encrypted key */
  cryptKey: String? = null,
  /** Preference file save path (must be a folder) */
  savePath: String? = null,
  /** [Editor.commit] all properties in this pref by default instead of [Editor.apply] */
  commitAllProperties: Boolean = false
) : KotprefModel(
  name = name,
  mode = processMode,
  extraArgs = mapOf(MmkvProvider.CRYPT_KEY to cryptKey, MmkvProvider.RELATIVE_PATH to savePath),
  provider = MmkvProvider,
  commitAllProperties = commitAllProperties,
  context = null
) {
  init {
    initializeMMKV()
  }

  /** Built-in preferences get/put on [MMKV] */

  override fun bytes(
    default: ByteArray,
    key: String?,
    commitByDefault: Boolean
  ): AbstractPref<ByteArray> = BytesPref(
    default, key, commitByDefault,
    getterImplProvider = { _, preferences ->
      preferences as MMKV
      preferences.getBytes(key, default)
    },
    putterImplProvider = { _, value, preferences ->
      preferences as MMKV
      preferences.putBytes(key, value)
    }
  )

  override fun bytesNullable(
    default: ByteArray?,
    key: String?,
    commitByDefault: Boolean
  ): AbstractPref<ByteArray?> = BytesNullablePref(
    default, key, commitByDefault,
    getterImplProvider = { _, preferences ->
      preferences as MMKV
      preferences.getBytes(key, default)
    },
    putterImplProvider = { _, value, preferences ->
      preferences as MMKV
      preferences.putBytes(key, value)
    }
  )

  override fun double(
    default: Double,
    key: String?,
    commitByDefault: Boolean
  ): AbstractPref<Double> = DoublePref(
    default, key, commitByDefault,
    getterImplProvider = { _, preferences ->
      preferences as MMKV
      preferences.decodeDouble(key, default)
    },
    putterImplProvider = { _, value, preferences ->
      (preferences as MMKV).apply {
        encode(key, value)
      }
    }
  )
}