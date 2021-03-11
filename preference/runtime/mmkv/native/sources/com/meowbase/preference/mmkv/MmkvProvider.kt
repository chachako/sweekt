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

import android.content.Context
import android.content.SharedPreferences
import com.meowbase.preference.core.PreferencesProvider
import com.tencent.mmkv.MMKV

/** internal [MMKV] provider */
internal object MmkvProvider : PreferencesProvider {
  const val CRYPT_KEY = "_"
  const val RELATIVE_PATH = "__"
  override fun get(
    context: Context?,
    name: String,
    mode: Int?,
    extras: Map<String, Any?>?
  ): SharedPreferences = MMKV.mmkvWithID(
    name,
    mode!!,
    extras!![CRYPT_KEY] as String?,
    extras[RELATIVE_PATH] as String?
  ) ?: error(
    "initialize MMKV instance failed! name: $name, mode: $mode, " +
      extras.map { "${it.key}: ${it.value}" }.joinToString()
  )
}