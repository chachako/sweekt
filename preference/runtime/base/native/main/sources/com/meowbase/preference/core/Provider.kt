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

import android.content.Context
import android.content.SharedPreferences
import com.meowbase.toolkit.appContext

/** provide [SharedPreferences] */
interface PreferencesProvider {
  /** return type must be [SharedPreferences.Editor] */
  fun get(
    context: Context?,
    name: String,
    mode: Int?,
    extras: Map<String, Any?>?
  ): SharedPreferences
}

/** internal [SharedPreferences] provider */
internal object SharedPreferencesProvider : PreferencesProvider {
  override fun get(
    context: Context?,
    name: String,
    mode: Int?,
    extras: Map<String, Any?>?
  ): SharedPreferences = (context ?: appContext).getSharedPreferences(name, mode!!)
}